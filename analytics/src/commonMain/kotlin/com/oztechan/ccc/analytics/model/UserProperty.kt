package com.oztechan.ccc.analytics.model

sealed class UserProperty(val key: String, open val value: String) {
    data class BaseCurrency(override val value: String) : UserProperty("base_currency", value)
    data class ActiveCurrencies(override val value: String) : UserProperty("active_currencies", value)
    data class CurrencyCount(override val value: String) : UserProperty("currency_count", value)
    data class IsRooted(override val value: String) : UserProperty("is_rooted", value) // android only
    data class AppTheme(override val value: String) : UserProperty("app_theme", value)
    data class IsAdFree(override val value: String) : UserProperty("is_ad_free", value)
    data class SessionCount(override val value: String) : UserProperty("session_count", value)
    data class DevicePlatform(override val value: String) : UserProperty("device_platform", value)
}
