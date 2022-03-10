/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.changebase

import android.os.Bundle
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.github.mustafaozhan.basemob.bottomsheet.BaseVBBottomSheetDialogFragment
import com.oztechan.ccc.analytics.AnalyticsManager
import com.oztechan.ccc.analytics.model.EventParam
import com.oztechan.ccc.analytics.model.FirebaseEvent
import com.oztechan.ccc.android.ui.calculator.CalculatorFragment.Companion.CHANGE_BASE_EVENT
import com.oztechan.ccc.android.util.setNavigationResult
import com.oztechan.ccc.android.util.showLoading
import com.oztechan.ccc.android.util.visibleIf
import com.oztechan.ccc.client.viewmodel.changebase.ChangeBaseEffect
import com.oztechan.ccc.client.viewmodel.changebase.ChangeBaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.BottomSheetChangeBaseBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeBaseBottomSheet : BaseVBBottomSheetDialogFragment<BottomSheetChangeBaseBinding>() {

    private val analyticsManager: AnalyticsManager by inject()
    private val changeBaseViewModel: ChangeBaseViewModel by viewModel()

    private val changeBaseAdapter: ChangeBaseAdapter by lazy {
        ChangeBaseAdapter(changeBaseViewModel.event)
    }

    override fun getViewBinding() = BottomSheetChangeBaseBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "ChangeBaserBottomSheet onViewCreated" }
        initViews()
        observeStates()
        observeEffects()
        setListeners()
    }

    override fun onDestroyView() {
        Logger.i { "ChangeBaseBottomSheet onDestroyView" }
        binding.recyclerViewChangeBase.adapter = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        analyticsManager.trackScreen(this::class.simpleName.toString())
    }

    private fun initViews() {
        binding.recyclerViewChangeBase.adapter = changeBaseAdapter
    }

    private fun observeStates() = changeBaseViewModel.state
        .flowWithLifecycle(lifecycle)
        .onEach {
            with(it) {
                changeBaseAdapter.submitList(currencyList)

                with(binding) {
                    loadingView.showLoading(loading)

                    recyclerViewChangeBase.visibleIf(enoughCurrency)
                    txtNoEnoughCurrency.visibleIf(!enoughCurrency)
                    btnSelect.visibleIf(!enoughCurrency)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun observeEffects() = changeBaseViewModel.effect
        .flowWithLifecycle(lifecycle)
        .onEach { viewEffect ->
            Logger.i { "ChangeBaseBottomSheet observeEffects ${viewEffect::class.simpleName}" }
            when (viewEffect) {
                is ChangeBaseEffect.BaseChange -> {
                    analyticsManager.trackEvent(
                        FirebaseEvent.BASE_CHANGE,
                        mapOf(EventParam.BASE to viewEffect.newBase)
                    )
                    setNavigationResult(
                        R.id.calculatorFragment,
                        viewEffect.newBase,
                        CHANGE_BASE_EVENT
                    )
                    dismissDialog()
                }
                ChangeBaseEffect.OpenCurrencies -> navigate(
                    R.id.changeBaseBottomSheet,
                    ChangeBaseBottomSheetDirections.actionChangeBaseBottomSheetToCurrenciesFragment()
                )
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    private fun setListeners() = binding.btnSelect.setOnClickListener {
        changeBaseViewModel.event.onSelectClick()
    }
}
