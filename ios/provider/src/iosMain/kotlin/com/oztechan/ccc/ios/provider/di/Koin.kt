/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

@file:Suppress("unused")

package com.oztechan.ccc.ios.provider.di

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.configservice.ad.di.clientConfigServiceAdModule
import com.oztechan.ccc.client.configservice.review.di.clientConfigServiceReviewModel
import com.oztechan.ccc.client.configservice.update.di.clientConfigServiceUpdateModule
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.di.getAnalyticsModule
import com.oztechan.ccc.client.core.persistence.di.NativeDependencyWrapper
import com.oztechan.ccc.client.core.persistence.di.clientCorePersistenceModule
import com.oztechan.ccc.client.core.shared.Device
import com.oztechan.ccc.client.datasource.currency.di.clientDataSourceCurrencyModule
import com.oztechan.ccc.client.datasource.watcher.di.clientDataSourceWatcherModule
import com.oztechan.ccc.client.repository.adcontrol.di.clientRepositoryAdControlModule
import com.oztechan.ccc.client.repository.appconfig.di.clientRepositoryAppConfigModule
import com.oztechan.ccc.client.service.backend.di.clientServiceBackendModule
import com.oztechan.ccc.client.storage.app.di.clientStorageAppModule
import com.oztechan.ccc.client.storage.calculation.di.clientStorageCalculationModule
import com.oztechan.ccc.client.viewmodel.calculator.di.clientViewModelCalculatorModule
import com.oztechan.ccc.client.viewmodel.currencies.di.clientViewModelCurrenciesModule
import com.oztechan.ccc.client.viewmodel.main.di.clientViewModelMainModule
import com.oztechan.ccc.client.viewmodel.selectcurrency.di.clientViewModelSelectCurrencyModule
import com.oztechan.ccc.client.viewmodel.settings.di.clientViewModelSettingsModule
import com.oztechan.ccc.client.viewmodel.watchers.di.clientViewModelWatchersModule
import com.oztechan.ccc.common.core.database.di.commonCoreDatabaseModule
import com.oztechan.ccc.common.core.infrastructure.di.commonCoreInfrastructureModule
import com.oztechan.ccc.common.core.network.di.commonCoreNetworkModule
import com.oztechan.ccc.common.datasource.conversion.di.commonDataSourceConversionModule
import com.oztechan.ccc.ios.repository.background.di.iosRepositoryBackgroundModule
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.ObjCObject
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initKoin(
    userDefaults: NSUserDefaults,
    analyticsManager: AnalyticsManager
) = startKoin {
    modules(
        getIOSModule(userDefaults),
        getAnalyticsModule(analyticsManager),

        // client
        clientCorePersistenceModule,
        clientStorageAppModule,
        clientStorageCalculationModule,
        clientServiceBackendModule,
        clientConfigServiceAdModule,
        clientConfigServiceReviewModel,
        clientConfigServiceUpdateModule,
        clientDataSourceCurrencyModule,
        clientDataSourceWatcherModule,
        clientRepositoryAdControlModule,
        iosRepositoryBackgroundModule,
        clientViewModelMainModule,
        clientViewModelCalculatorModule,
        clientViewModelCurrenciesModule,
        clientViewModelSettingsModule,
        clientViewModelSelectCurrencyModule,
        clientRepositoryAppConfigModule,
        clientViewModelWatchersModule,

        // common
        commonCoreDatabaseModule,
        commonCoreNetworkModule,
        commonCoreInfrastructureModule,
        commonDataSourceConversionModule
    )
}.also {
    Logger.i { "Koin initialised" }
}

private fun getIOSModule(userDefaults: NSUserDefaults) = module {
    single { NativeDependencyWrapper(userDefaults) }
    single<Device> { Device.IOS }
}

fun <T> Koin.getDependency(objCObject: ObjCObject): T = when (objCObject) {
    is ObjCClass -> getOriginalKotlinClass(objCObject)
    is ObjCProtocol -> getOriginalKotlinClass(objCObject)
    else -> null
}?.let {
    get(it, null) { parametersOf(it.simpleName) } as T
} ?: error("No dependency found")
