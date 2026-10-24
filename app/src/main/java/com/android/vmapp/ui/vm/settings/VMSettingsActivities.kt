package com.android.vmapp.ui.vm.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.vmapp.ui.base.BaseActivity
import com.android.vmapp.ui.vm.backup.VMBackupActivity
import com.android.vmapp.ui.vm.backup.VMBackupRestoreActivity
import com.android.vmcore.DevicePreset
import com.android.vmcore.VMManager
import com.clone.android.dual.space.R
import com.google.android.material.appbar.MaterialToolbar

class VMSettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_settings)
        findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, VMMainPreferenceFragment())
                .commitAllowingStateLoss()
        }
    }
}

class VMMainPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val vmId = activity?.intent?.getIntExtra("vm_id", 0) ?: 0
        preferenceManager.sharedPreferencesName = "vm_config_$vmId"
        setPreferencesFromResource(R.xml.vm_preferences, rootKey)

        fun bindNav(key: String, target: Class<*>) {
            findPreference<Preference>(key)?.setOnPreferenceClickListener {
                val intent = Intent(requireContext(), target)
                intent.putExtra("vm_id", vmId)
                startActivity(intent)
                true
            }
        }

        bindNav("vm_pref_key_hw_info", VMHWInfoActivity::class.java)
        bindNav("vm_pref_key_sensor", VMSensorActivity::class.java)
        bindNav("vm_pref_key_location", VMLocationActivity::class.java)
        bindNav("vm_pref_key_network", VMNetworkActivity::class.java)
        bindNav("vm_pref_key_phone", VMPhoneActivity::class.java)
        bindNav("vm_pref_key_security", VMSecurityActivity::class.java)
        bindNav("vm_pref_key_notification", VMNotificationActivity::class.java)
        bindNav("vm_pref_key_resolution", VMResolutionActivity::class.java)
        bindNav("vm_pref_key_backup", VMBackupActivity::class.java)
        bindNav("vm_pref_key_restore", VMBackupRestoreActivity::class.java)

        findPreference<Preference>("vm_pref_key_delete")?.setOnPreferenceClickListener {
            VMManager.getInstance(requireContext()).deleteVM(vmId)
            activity?.finish()
            true
        }
    }
}

class VMHWInfoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_hw_info)
        findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, VMHWInfoPreferenceFragment())
                .commitAllowingStateLoss()
        }
    }
}

/**
 * Hardware Info Preference Fragment supporting:
 * - Optional Device Presets (Android 5.1, Android 7.1, Modern Flagships) that auto-populate
 *   coherent Brand, Model, Device Code, Manufacturer, Baseband, Fingerprint, MAC OUI, and GPU
 * - Manual override for all fields & 97 GPU models (Android 4.0 to Present)
 * - No VIP lock (upgrade plan disabled)
 */
class VMHWInfoPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.vm_hw_info_preferences, rootKey)

        val vmId = activity?.intent?.getIntExtra("vm_id", 0) ?: 0
        val ctx = context ?: return
        val prefs = ctx.getSharedPreferences("vm_config_$vmId", Context.MODE_PRIVATE)

        val presetPref = findPreference<ListPreference>("vm_pref_key_device_preset")
        val modelPref = findPreference<EditTextPreference>("vm_pref_key_product_model")
        val brandPref = findPreference<EditTextPreference>("vm_pref_key_product_brand")
        val namePref = findPreference<EditTextPreference>("vm_pref_key_product_name")
        val devicePref = findPreference<EditTextPreference>("vm_pref_key_product_device")
        val manufacturerPref = findPreference<EditTextPreference>("vm_pref_key_product_manufacturer")
        val serialPref = findPreference<EditTextPreference>("vm_pref_key_serial_no")
        val gpuPref = findPreference<ListPreference>("vm_pref_key_gpu_info")

        fun syncUiFromPrefs() {
            presetPref?.value = prefs.getString("device_preset_id", DevicePreset.PRESET_CUSTOM)
            modelPref?.text = prefs.getString("prop.ro.product.model", "")
            brandPref?.text = prefs.getString("prop.ro.product.brand", "")
            namePref?.text = prefs.getString("prop.ro.product.name", "")
            devicePref?.text = prefs.getString("prop.ro.product.device", "")
            manufacturerPref?.text = prefs.getString("prop.ro.product.manufacturer", "")
            serialPref?.text = prefs.getString("serial", "")
            val vendor = prefs.getString("gpu_vendor", "") ?: ""
            val renderer = prefs.getString("gpu_renderer", "") ?: ""
            gpuPref?.value = if (vendor.isNotEmpty() && renderer.isNotEmpty()) "$vendor|$renderer" else ""
        }

        syncUiFromPrefs()

        presetPref?.setOnPreferenceChangeListener { _, newValue ->
            val presetId = newValue as? String ?: DevicePreset.PRESET_CUSTOM
            if (presetId != DevicePreset.PRESET_CUSTOM) {
                VMManager.getInstance(ctx).applyPresetToVM(vmId, presetId)
                syncUiFromPrefs()
            } else {
                prefs.edit().putString("device_preset_id", DevicePreset.PRESET_CUSTOM).apply()
            }
            true
        }

        modelPref?.setOnPreferenceChangeListener { _, v ->
            prefs.edit().putString("prop.ro.product.model", v.toString()).putString("device_preset_id", DevicePreset.PRESET_CUSTOM).apply()
            true
        }
        brandPref?.setOnPreferenceChangeListener { _, v ->
            prefs.edit().putString("prop.ro.product.brand", v.toString()).putString("device_preset_id", DevicePreset.PRESET_CUSTOM).apply()
            true
        }
        namePref?.setOnPreferenceChangeListener { _, v ->
            prefs.edit().putString("prop.ro.product.name", v.toString()).putString("device_preset_id", DevicePreset.PRESET_CUSTOM).apply()
            true
        }
        devicePref?.setOnPreferenceChangeListener { _, v ->
            prefs.edit().putString("prop.ro.product.device", v.toString()).putString("device_preset_id", DevicePreset.PRESET_CUSTOM).apply()
            true
        }
        manufacturerPref?.setOnPreferenceChangeListener { _, v ->
            prefs.edit().putString("prop.ro.product.manufacturer", v.toString()).putString("device_preset_id", DevicePreset.PRESET_CUSTOM).apply()
            true
        }
        serialPref?.setOnPreferenceChangeListener { _, v ->
            prefs.edit().putString("serial", v.toString()).apply()
            true
        }
        gpuPref?.setOnPreferenceChangeListener { _, v ->
            val gpuVal = v as? String ?: ""
            val parts = gpuVal.split("|")
            if (parts.size == 2) {
                prefs.edit()
                    .putString("gpu_vendor", parts[0])
                    .putString("gpu_renderer", parts[1])
                    .apply()
            } else {
                prefs.edit()
                    .putString("gpu_vendor", "")
                    .putString("gpu_renderer", "")
                    .apply()
            }
            true
        }
    }
}

class VMLocationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_location)
        findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings_container, SimpleXmlPrefFragment.newInstance(R.xml.vm_location_preferences))
                .commitAllowingStateLoss()
        }
    }
}

class VMNetworkActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_network)
        findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings_container, SimpleXmlPrefFragment.newInstance(R.xml.vm_network_preferences))
                .commitAllowingStateLoss()
        }
    }
}

class VMNotificationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_notification)
        findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings_container, SimpleXmlPrefFragment.newInstance(R.xml.vm_notification_preferences))
                .commitAllowingStateLoss()
        }
    }
}

class VMPhoneActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_phone)
        findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings_container, SimpleXmlPrefFragment.newInstance(R.xml.vm_phone_preferences))
                .commitAllowingStateLoss()
        }
    }
}

class VMResolutionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_resolution)
        findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
    }
}

class VMSecurityActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_security)
        findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings_container, SimpleXmlPrefFragment.newInstance(R.xml.vm_security_preferences))
                .commitAllowingStateLoss()
        }
    }
}

class VMSensorActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_sensor)
        findViewById<MaterialToolbar>(R.id.toolbar)?.setNavigationOnClickListener { finish() }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings_container, SimpleXmlPrefFragment.newInstance(R.xml.vm_sensor_preferences))
                .commitAllowingStateLoss()
        }
    }
}

class SimpleXmlPrefFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val vmId = activity?.intent?.getIntExtra("vm_id", 0) ?: 0
        preferenceManager.sharedPreferencesName = "vm_config_$vmId"
        val xmlRes = arguments?.getInt("xml_res") ?: R.xml.vm_preferences
        setPreferencesFromResource(xmlRes, rootKey)
    }

    companion object {
        fun newInstance(xmlRes: Int): SimpleXmlPrefFragment {
            return SimpleXmlPrefFragment().apply {
                arguments = Bundle().apply { putInt("xml_res", xmlRes) }
            }
        }
    }
}
