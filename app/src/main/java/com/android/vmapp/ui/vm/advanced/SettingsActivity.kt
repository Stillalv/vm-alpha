package com.android.vmapp.ui.vm.advanced

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.android.vmapp.ui.base.BaseActivity
import com.clone.android.dual.space.R
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_options)
        findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, AdvancedOptionsFragment())
                .commitAllowingStateLoss()
        }
    }

    class AdvancedOptionsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.advanced_options_preferences, rootKey)
        }
    }
}
