package com.android.vmcore

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import java.io.InputStream
import java.util.Locale
import java.util.Random

/**
 * VMAlphaEnhancer: Core enhancement controller for VM-Alpha.
 * Provides:
 * 1. Permanent VIP / Ad-Free unlock ("Plan Mati")
 * 2. Fallback 10-ROM catalog with Play Store and Telegram mirrors
 * 3. Cross-package asset access & original app ROM resolution
 * 4. Realistic device presets & hardware anti-detection hardening
 * 5. Dynamic per-VM LAN IP, IEEE Vendor OUI MAC, Natural Wi-Fi SSID, Luhn IMEI
 * 6. Sensor & battery temperature micro-noise injection
 */
object VMAlphaEnhancer {

    private val random = Random()

    @JvmStatic
    fun isVip(): Boolean = true

    @JvmStatic
    fun isAdFree(): Boolean = true

    // IEEE OUIs for major smartphone manufacturers
    private val IEEE_OUIS = arrayOf(
        byteArrayOf(0x3C.toByte(), 0x5A.toByte(), 0x37.toByte()), // Google
        byteArrayOf(0x54.toByte(), 0x60.toByte(), 0x09.toByte()), // Google
        byteArrayOf(0x94.toByte(), 0x65.toByte(), 0x2D.toByte()), // Samsung
        byteArrayOf(0xAC.toByte(), 0x5F.toByte(), 0x3E.toByte()), // Samsung
        byteArrayOf(0xF4.toByte(), 0xD9.toByte(), 0xFB.toByte()), // Xiaomi
        byteArrayOf(0x78.toByte(), 0x11.toByte(), 0xDC.toByte()), // Xiaomi
        byteArrayOf(0x40.toByte(), 0x4E.toByte(), 0x36.toByte()), // Huawei
        byteArrayOf(0x88.toByte(), 0x86.toByte(), 0x03.toByte()), // Huawei
        byteArrayOf(0xE8.toByte(), 0xAB.toByte(), 0xFA.toByte()), // OnePlus
        byteArrayOf(0xB8.toByte(), 0xBC.toByte(), 0x5B.toByte())  // ASUS
    )

    private val WIFI_SSID_PREFIXES = arrayOf(
        "Home-WiFi-5G",
        "TP-Link_78A2",
        "ASUS_5G_Fast",
        "Netgear-Guest",
        "Linksys_99F",
        "Galaxy_AP_5G",
        "Xiaomi_Home_5G",
        "Google_Fiber_5G",
        "Tenda_AC1200",
        "D-Link_DIR882",
        "MySpectrumWiFi42",
        "ATT-Fiber-882",
        "Verizon_5G_Home"
    )

    private val TAC_PREFIXES = arrayOf(
        "35293108", // Samsung Galaxy
        "35824009", // Google Pixel
        "86498204", // Xiaomi
        "86753003", // Huawei
        "35478910", // OnePlus
        "35612309"  // Sony Xperia
    )

    @JvmStatic
    fun generateDynamicLanIp(vmId: Int = 0): String {
        val hostPart = 100 + (vmId.coerceAtLeast(0) % 140)
        return "192.168.1.$hostPart/24;192.168.1.1"
    }

    @JvmStatic
    fun generateVendorMac(): String {
        val oui = IEEE_OUIS[random.nextInt(IEEE_OUIS.size)]
        val b3 = random.nextInt(256)
        val b4 = random.nextInt(256)
        val b5 = random.nextInt(256)
        return String.format(
            Locale.US,
            "%02x:%02x:%02x:%02x:%02x:%02x",
            oui[0].toInt() and 0xFF,
            oui[1].toInt() and 0xFF,
            oui[2].toInt() and 0xFF,
            b3,
            b4,
            b5
        )
    }

    @JvmStatic
    fun generateNaturalWifiSsid(): String {
        val prefix = WIFI_SSID_PREFIXES[random.nextInt(WIFI_SSID_PREFIXES.size)]
        val suffix = random.nextInt(900) + 100
        return "${prefix}_$suffix"
    }

    @JvmStatic
    fun generateAuthenticImei(): String {
        val tac = TAC_PREFIXES[random.nextInt(TAC_PREFIXES.size)]
        val serial = String.format(Locale.US, "%06d", random.nextInt(1_000_000))
        val raw14 = "$tac$serial"
        val checksum = calculateLuhnChecksum(raw14)
        return "$raw14$checksum"
    }

    private fun calculateLuhnChecksum(digits14: String): Int {
        var sum = 0
        for (i in 0 until digits14.length) {
            var d = digits14[i] - '0'
            if (i % 2 != 0) { // odd index (0-based) is 2nd digit in Luhn from left
                d *= 2
                if (d > 9) d = (d / 10) + (d % 10)
            }
            sum += d
        }
        val remainder = sum % 10
        return if (remainder == 0) 0 else 10 - remainder
    }

    @JvmStatic
    fun openCrossPackageAsset(context: Context, uriStr: String, assetPath: String): InputStream {
        try {
            val uri = Uri.parse(uriStr)
            val auth = uri.authority
            if (!TextUtils.isEmpty(auth) && auth != context.packageName) {
                val foreignCtx = context.createPackageContext(auth, 0)
                return foreignCtx.assets.open(assetPath)
            }
        } catch (_: Exception) {
        }
        return context.assets.open(assetPath)
    }

    @JvmStatic
    fun resolveBuiltinOrOriginalRomUri(context: Context, originalUriStr: String): String {
        try {
            val uri = Uri.parse(originalUriStr)
            val host = uri.host ?: return originalUriStr
            val path = if (uri.path?.startsWith("/") == true) uri.path!!.substring(1) else uri.path ?: ""

            // 1. Check if asset exists in current package
            try {
                context.assets.open(path).use { return originalUriStr }
            } catch (_: Exception) {
            }

            // 2. Check original package com.clone.android.dual.space
            try {
                val origCtx = context.createPackageContext("com.clone.android.dual.space", 0)
                origCtx.assets.open(path).use {
                    return "asset://com.clone.android.dual.space/rom/$host/manifest.json"
                }
            } catch (_: Exception) {
            }
        } catch (_: Exception) {
        }
        return originalUriStr
    }

    @JvmStatic
    fun enhanceNewVMPreferences(context: Context, vmId: Int) {
        try {
            val prefsName = "vm_config_$vmId"
            val sp: SharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            val editor = sp.edit()

            val romConfigStr = sp.getString("rom_config", "") ?: ""
            val isAndroid5 = romConfigStr.contains("\"os_version\":5") || romConfigStr.contains("android5") || romConfigStr.contains("5.1")
            val isAndroid7 = romConfigStr.contains("\"os_version\":7") || romConfigStr.contains("android7") || romConfigStr.contains("7.1")

            val preset = when {
                isAndroid5 -> DevicePreset.getPresetById("galaxy_s6") ?: DevicePreset.DEFAULT_PRESET
                isAndroid7 -> DevicePreset.getPresetById("galaxy_s7") ?: DevicePreset.DEFAULT_PRESET
                else -> DevicePreset.getPresetById("xiaomi_13") ?: DevicePreset.DEFAULT_PRESET
            }

            // Apply DevicePreset hardware info
            editor.putString("device_preset_id", preset.id)
            editor.putString("prop.ro.product.model", preset.model)
            editor.putString("prop.ro.product.brand", preset.brand)
            editor.putString("prop.ro.product.name", preset.name)
            editor.putString("prop.ro.product.device", preset.device)
            editor.putString("prop.ro.product.manufacturer", preset.manufacturer)
            editor.putString("prop.ro.build.fingerprint", preset.fingerprint)
            editor.putString("base_band", preset.baseband)
            editor.putString("gpu_vendor", preset.gpuVendor)
            editor.putString("gpu_renderer", preset.gpuRenderer)

            // Dynamic Networking & Authenticity
            editor.putString("local_ip", generateDynamicLanIp(vmId))
            editor.putString("local_mac", generateVendorMac())
            editor.putString("wifi_bssid", generateVendorMac())
            editor.putString("wifi_ssid", generateNaturalWifiSsid())
            editor.putString("imei", generateAuthenticImei())

            // Anti-detection OS Build Properties
            editor.putString("prop.ro.debuggable", "0")
            editor.putString("prop.ro.secure", "1")
            editor.putString("prop.ro.build.type", "user")
            editor.putString("prop.ro.build.tags", "release-keys")

            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun setupDevicePresetPreference(fragmentObj: Any, vmInstanceObj: Any?) {
        try {
            val fragment = fragmentObj as? PreferenceFragmentCompat ?: return
            val context = fragment.context ?: return

            // Reflectively extract vmId from VMInstance (Lq4/WWoڢWWo࢞ڢ;)
            val vmId = try {
                val fld = vmInstanceObj?.javaClass?.getField("WWWW̏WWWWβ̏")
                    ?: vmInstanceObj?.javaClass?.getDeclaredField("WWWW̏WWWWβ̏")
                fld?.isAccessible = true
                fld?.getInt(vmInstanceObj) ?: 0
            } catch (_: Exception) {
                0
            }

            val prefs = context.getSharedPreferences("vm_config_$vmId", Context.MODE_PRIVATE)

            val presetPref = fragment.findPreference<ListPreference>("vm_pref_key_device_preset") ?: run {
                val newPref = ListPreference(context).apply {
                    key = "vm_pref_key_device_preset"
                    title = "Device Preset"
                    dialogTitle = "Select Device Preset"
                    order = -100
                }
                fragment.preferenceScreen.addPreference(newPref)
                newPref
            }

            val presets = DevicePreset.ALL_PRESETS
            presetPref.entries = presets.map { "${it.displayName} (${it.androidVersion})" }.toTypedArray()
            presetPref.entryValues = presets.map { it.id }.toTypedArray()

            val curPresetId = prefs.getString("device_preset_id", "")
            presetPref.value = curPresetId
            presetPref.summary = presets.firstOrNull { it.id == curPresetId }?.displayName ?: "Custom / Manual"

            presetPref.setOnPreferenceChangeListener { _, newValue ->
                val pId = newValue as? String ?: return@setOnPreferenceChangeListener true
                val selected = DevicePreset.getPresetById(pId)
                if (selected != null) {
                    prefs.edit()
                        .putString("device_preset_id", selected.id)
                        .putString("prop.ro.product.model", selected.model)
                        .putString("prop.ro.product.brand", selected.brand)
                        .putString("prop.ro.product.name", selected.name)
                        .putString("prop.ro.product.device", selected.device)
                        .putString("prop.ro.product.manufacturer", selected.manufacturer)
                        .putString("prop.ro.build.fingerprint", selected.fingerprint)
                        .putString("base_band", selected.baseband)
                        .putString("gpu_vendor", selected.gpuVendor)
                        .putString("gpu_renderer", selected.gpuRenderer)
                        .apply()

                    presetPref.summary = selected.displayName
                    fragment.findPreference<Preference>("vm_pref_key_product_model")?.summary = selected.model
                    fragment.findPreference<Preference>("vm_pref_key_product_brand")?.summary = selected.brand
                    fragment.findPreference<Preference>("vm_pref_key_product_name")?.summary = selected.name
                    fragment.findPreference<Preference>("vm_pref_key_product_device")?.summary = selected.device
                    fragment.findPreference<Preference>("vm_pref_key_product_manufacturer")?.summary = selected.manufacturer
                    fragment.findPreference<Preference>("vm_pref_key_base_band")?.summary = selected.baseband
                    fragment.findPreference<Preference>("vm_pref_key_gpu_info")?.summary = "${selected.gpuVendor} / ${selected.gpuRenderer}"
                }
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun addSensorMicroNoise(value: Float, sensorIndex: Int): Float {
        // Micro-noise: ±0.0008f to simulate real hardware analog jitter
        val noise = (random.nextFloat() - 0.5f) * 0.0016f
        return value + noise
    }

    @JvmStatic
    fun getNoisyBatteryTemp(baseTemp: Int): Int {
        // Battery temp is reported in tenths of a degree C (e.g. 359 = 35.9 C)
        val delta = random.nextInt(3) - 1 // -1, 0, +1
        return (baseTemp + delta).coerceIn(280, 450)
    }

    @JvmStatic
    fun getFallbackRomConfigsJson(): String {
        return """
[
  {
    "rom_id": "rom_default_4.2.2",
    "rom_name": "Android 4.2.2",
    "rom_icon": "",
    "rom_version": 1000,
    "os_version": 4,
    "support_a32": true,
    "support_a64": false,
    "minimum_sdk_int": 21,
    "minimum_app_ver": "3.2.29",
    "rom_asset": [
      {
        "uri": "https://t.me/virtualmaster_officialgroup/80605",
        "size": 69146848,
        "md5": "aeae4b2a9f56c1ebf9fb4e86c108a400"
      }
    ]
  },
  {
    "rom_id": "rom_default_5.1.1_32",
    "rom_name": "Android 5.1.1 32",
    "rom_icon": "",
    "rom_version": 1000,
    "os_version": 5,
    "support_a32": true,
    "support_a64": false,
    "minimum_sdk_int": 21,
    "minimum_app_ver": "3.2.29",
    "rom_asset": [
      {
        "uri": "https://t.me/virtualmaster_officialgroup/80606",
        "size": 178716260,
        "md5": "0e633d7d743a1a6bfad0987c33e859b4"
      }
    ]
  },
  {
    "rom_id": "rom_default_5.1.1",
    "rom_name": "Android 5.1.1",
    "rom_icon": "",
    "rom_version": 1000,
    "os_version": 5,
    "support_a32": true,
    "support_a64": true,
    "minimum_sdk_int": 21,
    "minimum_app_ver": "3.2.29",
    "rom_asset": [
      {
        "uri": "https://t.me/virtualmaster_officialgroup/80607",
        "size": 222956277,
        "md5": "aa8cfa2b6e5684534a755d04dd266498"
      }
    ]
  },
  {
    "rom_id": "rom_default_7.1.2_32",
    "rom_name": "Android 7.1.2 32",
    "rom_icon": "",
    "rom_version": 1000,
    "os_version": 7,
    "support_a32": true,
    "support_a64": false,
    "minimum_sdk_int": 24,
    "minimum_app_ver": "3.2.51",
    "rom_asset": [
      {
        "uri": "https://t.me/virtualmaster_officialgroup/80608",
        "size": 249796832,
        "md5": "fb77e4db86a2d9df1b124421037baff4"
      }
    ]
  },
  {
    "rom_id": "rom_default_7.1.2",
    "rom_name": "Android 7.1.2",
    "rom_icon": "",
    "rom_version": 1000,
    "os_version": 7,
    "support_a32": true,
    "support_a64": true,
    "minimum_sdk_int": 24,
    "minimum_app_ver": "3.2.51",
    "rom_asset": [
      {
        "uri": "asset:///rom/android710_arm64/manifest.json",
        "size": 314572800,
        "md5": ""
      },
      {
        "uri": "https://t.me/virtualmaster_officialgroup/80609",
        "size": 331484812,
        "md5": "a02df149495bcaae484a0cf66fc5b7df"
      }
    ]
  },
  {
    "rom_id": "rom_default_9.0.0_32",
    "rom_name": "Android 9.0.0 32",
    "rom_icon": "",
    "rom_version": 1000,
    "os_version": 9,
    "support_a32": true,
    "support_a64": false,
    "minimum_sdk_int": 28,
    "minimum_app_ver": "3.2.29",
    "rom_asset": [
      {
        "uri": "https://t.me/virtualmaster_officialgroup/80612",
        "size": 371745423,
        "md5": "89e5fc81ab46ae726eb9753dfae08447"
      }
    ]
  },
  {
    "rom_id": "rom_default_9.0.0",
    "rom_name": "Android 9.0.0",
    "rom_icon": "",
    "rom_version": 1000,
    "os_version": 9,
    "support_a32": false,
    "support_a64": true,
    "minimum_sdk_int": 28,
    "minimum_app_ver": "3.2.29",
    "rom_asset": [
      {
        "uri": "https://t.me/virtualmaster_officialgroup/80614",
        "size": 412499696,
        "md5": "e0bfa9da75841cbfb9d0344d5db2f4a1"
      }
    ]
  },
  {
    "rom_id": "rom_default_11.0.0_32",
    "rom_name": "Android 11.0.0 32",
    "rom_icon": "",
    "rom_version": 1000,
    "os_version": 11,
    "support_a32": true,
    "support_a64": false,
    "minimum_sdk_int": 30,
    "minimum_app_ver": "3.2.29",
    "rom_asset": [
      {
        "uri": "https://t.me/virtualmaster_officialgroup/80616",
        "size": 435158220,
        "md5": "6e902b4e85743b8be923838dcab3b4e6"
      }
    ]
  },
  {
    "rom_id": "rom_default_11.0.0",
    "rom_name": "Android 11.0.0",
    "rom_icon": "",
    "rom_version": 1000,
    "os_version": 11,
    "support_a32": false,
    "support_a64": true,
    "minimum_sdk_int": 30,
    "minimum_app_ver": "3.2.29",
    "rom_asset": [
      {
        "uri": "https://t.me/virtualmaster_officialgroup/80617",
        "size": 472195044,
        "md5": "7cae18fc5cf8ebf5c78f16b2d2f78b0d"
      }
    ]
  },
  {
    "rom_id": "rom_default_12.1.0",
    "rom_name": "Android 12.1.0",
    "rom_icon": "",
    "rom_version": 1000,
    "os_version": 12,
    "support_a32": false,
    "support_a64": true,
    "minimum_sdk_int": 31,
    "minimum_app_ver": "3.2.29",
    "rom_asset": [
      {
        "uri": "https://t.me/virtualmaster_officialgroup/80619",
        "size": 498124800,
        "md5": "452bfe359a0f44bb59a385e494bcf9a2"
      }
    ]
  }
]
""".trimIndent()
    }
}
