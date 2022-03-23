/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.client.viewmodel.calculator

import co.touchlab.kermit.Logger
import com.github.submob.scopemob.mapTo
import com.github.submob.scopemob.whether
import com.github.submob.scopemob.whetherNot
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.helper.SessionManager
import com.oztechan.ccc.client.mapper.toRates
import com.oztechan.ccc.client.mapper.toTodayResponse
import com.oztechan.ccc.client.mapper.toUIModelList
import com.oztechan.ccc.client.model.Currency
import com.oztechan.ccc.client.model.RateState
import com.oztechan.ccc.client.util.calculateResult
import com.oztechan.ccc.client.util.getCurrencyConversionByRate
import com.oztechan.ccc.client.util.getFormatted
import com.oztechan.ccc.client.util.launchIgnored
import com.oztechan.ccc.client.util.toStandardDigits
import com.oztechan.ccc.client.util.toSupportedCharacters
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.CHAR_DOT
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_AC
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.KEY_DEL
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.MAXIMUM_INPUT
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.MAXIMUM_OUTPUT
import com.oztechan.ccc.client.viewmodel.calculator.CalculatorData.Companion.PRECISION
import com.oztechan.ccc.client.viewmodel.currencies.CurrenciesData.Companion.MINIMUM_ACTIVE_CURRENCY
import com.oztechan.ccc.common.api.repo.ApiRepository
import com.oztechan.ccc.common.db.currency.CurrencyRepository
import com.oztechan.ccc.common.db.offlinerates.OfflineRatesRepository
import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.model.Rates
import com.oztechan.ccc.common.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class CalculatorViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiRepository: ApiRepository,
    private val currencyRepository: CurrencyRepository,
    private val offlineRatesRepository: OfflineRatesRepository,
    private val sessionManager: SessionManager
) : BaseSEEDViewModel(), CalculatorEvent {
    // region SEED
    private val _state = MutableStateFlow(CalculatorState())
    override val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CalculatorEffect>()
    override val effect = _effect.asSharedFlow()

    override val event = this as CalculatorEvent

    override val data = CalculatorData()
    // endregion

    init {
        _state.update(base = settingsRepository.currentBase, input = "")

        state.map { it.base }
            .distinctUntilChanged()
            .onEach {
                Logger.d { "CalculatorViewModel base changed $it" }
                currentBaseChanged(it)
            }
            .launchIn(clientScope)

        state.map { it.input }
            .distinctUntilChanged()
            .onEach {
                Logger.d { "CalculatorViewModel input changed $it" }
                calculateOutput(it)
            }
            .launchIn(clientScope)

        currencyRepository.collectActiveCurrencies()
            .onEach {
                Logger.d { "CalculatorViewModel currencyList changed\n${it.joinToString("\n")}" }
                _state.update(currencyList = it.toUIModelList())
            }
            .launchIn(clientScope)
    }

    private fun getRates() = data.rates?.let { rates ->
        calculateConversions(rates)
        _state.update(rateState = RateState.Cached(rates.date))
    } ?: clientScope.launch {
        runCatching { apiRepository.getRatesByBackend(settingsRepository.currentBase) }
            .onFailure(::getRatesFailed)
            .onSuccess(::getRatesSuccess)
    }

    private fun getRatesSuccess(currencyResponse: CurrencyResponse) = currencyResponse
        .toRates().let {
            data.rates = it
            calculateConversions(it)
            _state.update(rateState = RateState.Online(it.date))
        }.also {
            offlineRatesRepository.insertOfflineRates(currencyResponse.toTodayResponse())
        }

    private fun getRatesFailed(t: Throwable) {
        Logger.w(t) { "CalculatorViewModel getRatesFailed" }
        offlineRatesRepository.getOfflineRatesByBase(
            settingsRepository.currentBase
        )?.let { offlineRates ->
            calculateConversions(offlineRates)
            _state.update(rateState = RateState.Offline(offlineRates.date))
        } ?: clientScope.launch {
            Logger.w(Exception("No offline rates")) { this@CalculatorViewModel::class.simpleName.toString() }

            state.value.currencyList.size
                .whether { it > 1 }
                ?.let { _effect.emit(CalculatorEffect.Error) }

            _state.update(
                rateState = RateState.Error,
                loading = false
            )
        }
    }

    private fun calculateOutput(input: String) = clientScope.launch {
        _state.update(loading = true)
        data.parser
            .calculate(input.toSupportedCharacters(), PRECISION)
            .mapTo { if (isFinite()) getFormatted() else "" }
            .whether(
                { output -> output.length <= MAXIMUM_OUTPUT },
                { input.length <= MAXIMUM_INPUT }
            )?.let { output ->
                _state.update(output = output)
                state.value.currencyList.size
                    .whether { it < MINIMUM_ACTIVE_CURRENCY }
                    ?.whetherNot { state.value.input.isEmpty() }
                    ?.let { _effect.emit(CalculatorEffect.FewCurrency) }
                    ?: run { getRates() }
            } ?: run {
            _effect.emit(CalculatorEffect.MaximumInput)
            _state.update(
                input = input.dropLast(1),
                loading = false
            )
        }
    }

    private fun calculateConversions(rates: Rates?) = _state.update(
        currencyList = _state.value.currencyList.onEach {
            it.rate = rates.calculateResult(it.name, _state.value.output)
        },
        loading = false
    )

    private fun currentBaseChanged(newBase: String) {
        data.rates = null
        settingsRepository.currentBase = newBase
        _state.update(
            base = newBase,
            input = _state.value.input,
            symbol = currencyRepository.getCurrencyByName(newBase)?.symbol ?: ""
        )
    }

    fun shouldShowBannerAd() = sessionManager.shouldShowBannerAd()

    // region Event
    override fun onKeyPress(key: String) {
        Logger.d { "CalculatorViewModel onKeyPress $key" }
        when (key) {
            KEY_AC -> _state.update(input = "")
            KEY_DEL ->
                state.value.input
                    .whetherNot { isEmpty() }
                    ?.apply {
                        _state.update(input = substring(0, length - 1))
                    }
            else -> _state.update(input = if (key.isEmpty()) "" else state.value.input + key)
        }
    }

    override fun onItemClick(currency: Currency) {
        Logger.d { "CalculatorViewModel onItemClick ${currency.name}" }
        var finalResult = currency.rate
            .getFormatted()
            .toStandardDigits()
            .toSupportedCharacters()

        while (finalResult.length >= MAXIMUM_OUTPUT || finalResult.length >= MAXIMUM_INPUT) {
            finalResult = finalResult.dropLast(1)
        }

        if (finalResult.last() == CHAR_DOT) {
            finalResult = finalResult.dropLast(1)
        }

        _state.update(
            base = currency.name,
            input = finalResult
        )
    }

    override fun onItemLongClick(currency: Currency): Boolean {
        Logger.d { "CalculatorViewModel onItemLongClick ${currency.name}" }
        clientScope.launch {
            _effect.emit(
                CalculatorEffect.ShowRate(
                    currency.getCurrencyConversionByRate(
                        settingsRepository.currentBase,
                        data.rates
                    ),
                    currency.name
                )
            )
        }
        return true
    }

    override fun onBarClick() = clientScope.launchIgnored {
        Logger.d { "CalculatorViewModel onBarClick" }
        _effect.emit(CalculatorEffect.OpenBar)
    }

    override fun onSettingsClicked() = clientScope.launchIgnored {
        Logger.d { "CalculatorViewModel onSettingsClicked" }
        _effect.emit(CalculatorEffect.OpenSettings)
    }

    override fun onBaseChange(base: String) {
        Logger.d { "CalculatorViewModel onBaseChange $base" }
        currentBaseChanged(base)
        calculateOutput(_state.value.input)
    }
    // endregion
}
