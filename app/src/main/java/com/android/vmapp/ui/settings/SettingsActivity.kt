package com.android.vmapp.ui.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.android.vmapp.config.AppPreferences
import com.android.vmapp.config.ThemeManager
import com.android.vmapp.ui.base.BaseActivity
import com.clone.android.dual.space.R
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, AppSettingsFragment())
                .commitAllowingStateLoss()
        }
    }

    class AppSettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.app_preferences, rootKey)
            val ctx = context ?: return
            val appPrefs = AppPreferences(ctx)

            findPreference<ListPreference>("night_mode")?.setOnPreferenceChangeListener { _, newValue ->
                val mode = (newValue as? String)?.toIntOrNull() ?: 0
                appPrefs.nightMode = mode
                ThemeManager.applyNightMode(mode)
                true
            }

            findPreference<ListPreference>("theme_mode")?.setOnPreferenceChangeListener { _, newValue ->
                val mode = newValue as? String ?: "classic_purple"
                appPrefs.themeMode = mode
                activity?.recreate()
                true
            }
        }
    }
}
