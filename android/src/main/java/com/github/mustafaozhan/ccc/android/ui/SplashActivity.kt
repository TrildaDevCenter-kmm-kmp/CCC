/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.github.mustafaozhan.basemob.activity.BaseActivity
import com.github.mustafaozhan.ccc.client.log.kermit
import com.github.mustafaozhan.ccc.client.viewmodel.splash.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity() {

    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kermit.d { "SplashActivity onCreate" }

        AppCompatDelegate.setDefaultNightMode(splashViewModel.getAppTheme())

        startActivity(
            Intent(
                this,
                if (splashViewModel.isFirstRun()) SliderActivity::class.java else MainActivity::class.java
            )
        )

        finish()
    }
}
