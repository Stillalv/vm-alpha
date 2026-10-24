package com.android.vmapp.config

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    var isEulaAccepted: Boolean
        get() = prefs.getBoolean("eula_accepted", false)
        set(value) = prefs.edit().putBoolean("eula_accepted", value).apply()

    var themeMode: String
        get() = prefs.getString("theme_mode", "catalog_purple") ?: "catalog_purple"
        set(value) = prefs.edit().putString("theme_mode", value).apply()

    var nightMode: Int
        get() = prefs.getInt("night_mode", 0)
        set(value) = prefs.edit().putInt("night_mode", value).apply()

    /**
     * Upgrade plan is disabled ("Plan Mati") — VIP is permanently unlocked for all features.
     */
    var isVip: Boolean
        get() = true
        set(_) {
            prefs.edit().putBoolean("is_vip", true).apply()
        }
}
