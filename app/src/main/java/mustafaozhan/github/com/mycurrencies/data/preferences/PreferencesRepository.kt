/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.data.preferences

import android.content.Context
import com.github.mustafaozhan.basemob.data.preferences.BasePreferencesRepository
import com.github.mustafaozhan.scopemob.whether
import mustafaozhan.github.com.mycurrencies.di.ApplicationContext
import mustafaozhan.github.com.mycurrencies.model.Currencies
import mustafaozhan.github.com.mycurrencies.util.OldPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository
@Inject constructor(
    @ApplicationContext context: Context
) : BasePreferencesRepository(context) {
    companion object {
        private const val KEY_APPLICATION_PREFERENCES = "application_preferences"
        private const val KEY_FIRST_RUN = "firs_run"
        private const val KEY_CURRENT_BASE = "current_base"
        private const val KEY_AD_FREE_DATE = "ad_free_date"

        private const val DAY = (24 * 60 * 60 * 1000).toLong()
    }

    override val preferencesName: String
        get() = KEY_APPLICATION_PREFERENCES

    var firstRun
        get() = getValue(KEY_FIRST_RUN, true)
        internal set(value) = setValue(KEY_FIRST_RUN, value)

    var currentBase
        get() = getValue(KEY_CURRENT_BASE, Currencies.NULL.toString())
        internal set(value) = setValue(KEY_CURRENT_BASE, value)

    private var adFreeActivatedDate
        get() = getValue(KEY_AD_FREE_DATE, 0.toLong())
        private set(value) = setValue(KEY_AD_FREE_DATE, value)

    fun setAdFreeActivation() {
        adFreeActivatedDate = System.currentTimeMillis()
    }

    fun isRewardExpired() = System.currentTimeMillis() - adFreeActivatedDate >= DAY

    fun syncPreferences() = OldPreferences(context)
        .whether { isOldPreferencesExist() }
        ?.let { oldPreferences ->
            firstRun = oldPreferences.getOldFirstRun() == true.toString()
            currentBase = oldPreferences.getOldBaseCurrency() ?: Currencies.NULL.toString()
            oldPreferences.removeOldPreferences()
        }
}
