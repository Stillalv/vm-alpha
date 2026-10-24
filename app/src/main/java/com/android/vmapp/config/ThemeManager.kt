package com.android.vmapp.config

import android.app.Activity
import androidx.appcompat.app.AppCompatDelegate
import com.clone.android.dual.space.R
import com.google.android.material.color.DynamicColors

object ThemeManager {
    fun applyTheme(activity: Activity) {
        val prefs = AppPreferences(activity)
        when (prefs.themeMode) {
            "green", "catalog_green" -> activity.setTheme(R.style.Theme_Catalog_Green)
            "dynamic", "catalog_dynamic" -> {
                activity.setTheme(R.style.Theme_Catalog_Purple)
                DynamicColors.applyToActivityIfAvailable(activity)
            }
            else -> activity.setTheme(R.style.Theme_Catalog_Purple)
        }
    }

    fun applyNightMode(mode: Int) {
        val delegateMode = when (mode) {
            1 -> AppCompatDelegate.MODE_NIGHT_NO
            2 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(delegateMode)
    }
}
