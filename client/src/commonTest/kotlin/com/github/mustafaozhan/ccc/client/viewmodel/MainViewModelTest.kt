/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.BuildKonfig
import com.github.mustafaozhan.ccc.client.device
import com.github.mustafaozhan.ccc.client.helper.SessionManager
import com.github.mustafaozhan.ccc.client.model.Device
import com.github.mustafaozhan.ccc.client.util.after
import com.github.mustafaozhan.ccc.client.util.before
import com.github.mustafaozhan.ccc.client.util.getRandomDateLong
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainEffect
import com.github.mustafaozhan.ccc.client.viewmodel.main.MainViewModel
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.config.ConfigManager
import com.github.mustafaozhan.config.model.AppConfig
import com.github.mustafaozhan.config.model.AppUpdate
import com.github.mustafaozhan.logmob.initLogger
import com.github.mustafaozhan.scopemob.castTo
import io.mockative.ConfigurationApi
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.configure
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ConfigurationApi
@Suppress("TooManyFunctions")
class MainViewModelTest {

    @Mock
    private val settingsRepository = configure(mock(classOf<SettingsRepository>())) {
        stubsUnitByDefault = true
    }

    @Mock
    private val configManager = mock(classOf<ConfigManager>())

    @Mock
    private val sessionManager = mock(classOf<SessionManager>())

    private val viewModel: MainViewModel by lazy {
        MainViewModel(settingsRepository, configManager, sessionManager)
    }

    @BeforeTest
    fun setup() {
        initLogger(true)

        given(settingsRepository)
            .invocation { lastReviewRequest }
            .thenReturn(0)

        given(settingsRepository)
            .setter(settingsRepository::lastReviewRequest)
            .whenInvokedWith(any())
            .thenReturn(Unit)
    }

    @Test
    fun app_review_should_ask_when_device_is_google() {
        val mockSessionCount = Random.nextLong()

        given(settingsRepository)
            .invocation { sessionCount }
            .then { mockSessionCount }

        val mockConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = device.name,
                    updateForceVersion = BuildKonfig.versionCode + 1,
                    updateLatestVersion = BuildKonfig.versionCode + 1
                )
            )
        )

        given(configManager)
            .invocation { configManager.appConfig }
            .then { mockConfig }

        if (device == Device.ANDROID.GOOGLE) {
            viewModel.effect.before {
                viewModel.onResume()
            }.after {
                assertTrue { it is MainEffect.AppUpdateEffect }
                assertTrue { it?.castTo<MainEffect.AppUpdateEffect>()?.isCancelable == false }
                assertTrue { viewModel.data.isAppUpdateShown }
            }
        }

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    // SEED
    @Test
    fun check_state_is_null() {
        assertNull(viewModel.state)
    }

    // init
    @Test
    fun set_lastReviewRequest_now_if_not_initialised_before() {
        given(settingsRepository)
            .invocation { lastReviewRequest }
            .thenReturn(0)

        verify(settingsRepository)
            .invocation { lastReviewRequest = nowAsLong() }
            .wasInvoked()
    }

    // public methods
    @Test
    fun isFirstRun() {
        val boolean: Boolean = Random.nextBoolean()

        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(boolean)

        assertEquals(boolean, viewModel.isFistRun())

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun getAppTheme() {
        val int: Int = Random.nextInt()

        given(settingsRepository)
            .invocation { appTheme }
            .thenReturn(int)

        assertEquals(int, viewModel.getAppTheme())

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun isAdFree() {
        val long: Long = Random.getRandomDateLong()

        given(settingsRepository)
            .invocation { adFreeEndDate }
            .then { long }

        assertEquals(!long.isRewardExpired(), viewModel.isAdFree())
        assertEquals(long, settingsRepository.adFreeEndDate)

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()
    }

    @Test
    fun getSessionCount() {
        val mockSessionCount = Random.nextLong()

        given(settingsRepository)
            .invocation { sessionCount }
            .then { mockSessionCount }

        assertEquals(mockSessionCount, viewModel.getSessionCount())

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()
    }

    @Test
    fun checkReview() {
        if (device == Device.ANDROID.GOOGLE) {
            viewModel.effect.before {
                viewModel.checkReview(0)
            }.after {
                assertTrue { it is MainEffect.RequestReview }

                verify(settingsRepository)
                    .invocation { lastReviewRequest = nowAsLong() }
                    .wasInvoked()
            }
        }
    }

    // event
    @Test
    fun onPause() = with(viewModel) {
        event.onPause()
        assertEquals(false, data.adVisibility)
        assertEquals(true, data.adJob.isCancelled)
    }

    @Test
    fun onResume() = with(viewModel) {
        val mockConfig = AppConfig()
        val mockSessionCount = Random.nextLong()

        given(configManager)
            .invocation { configManager.appConfig }
            .then { mockConfig }

        given(settingsRepository)
            .invocation { sessionCount }
            .then { mockSessionCount }

        assertEquals(true, viewModel.data.isNewSession)

        event.onResume()
        if (device is Device.ANDROID.GOOGLE ||
            device is Device.IOS
        ) {
            assertEquals(true, data.adVisibility)
            assertEquals(true, data.adJob.isActive)
        }

        verify(settingsRepository)
            .invocation { sessionCount = mockSessionCount + 1 }
            .wasInvoked()
        assertEquals(false, viewModel.data.isNewSession)

        event.onResume()

        verify(settingsRepository)
            .invocation { sessionCount = mockSessionCount + 1 }
            .wasNotInvoked()
        assertEquals(false, viewModel.data.isNewSession)
    }
}
