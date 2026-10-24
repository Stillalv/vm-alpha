package com.android.vmcore

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.io.File
import java.util.Locale
import java.util.Random
import java.util.concurrent.ConcurrentHashMap

/**
 * VMManager manages the creation, configuration, and lifecycle of virtual machine instances.
 *
 * Enhanced with:
 * - Authentic Device Presets (Android 5.1 & 7.1 + Modern Flagships) locking GPU, Baseband, CPU, & Fingerprint
 * - IEEE Registered Vendor OUI MAC addresses & natural Wi-Fi SSIDs
 * - Dynamic per-instance LAN IP allocation (192.168.1.X)
 * - Full hardware sensor & anti-detection defaults
 */
class VMManager private constructor(val context: Context) {
    private val vms = ConcurrentHashMap<Int, VMInstance>()
    private var nextVmId = 0

    init {
        loadExistingVMs()
    }

    private fun loadExistingVMs() {
        val prefsDir = File(context.applicationInfo.dataDir, "shared_prefs")
        if (prefsDir.exists()) {
            val configFiles = prefsDir.listFiles { _, name -> name.startsWith("vm_config_") && name.endsWith(".xml") }
            configFiles?.forEach { file ->
                val idStr = file.name.removePrefix("vm_config_").removeSuffix(".xml")
                idStr.toIntOrNull()?.let { id ->
                    val prefs = context.getSharedPreferences("vm_config_$id", Context.MODE_PRIVATE)
                    val inst = VMInstance(context, id)
                    inst.config = VMConfig.fromPreferences(prefs)
                    vms[id] = inst
                    if (id >= nextVmId) nextVmId = id + 1
                }
            }
        }
    }

    fun getAllVMs(): List<VMInstance> = vms.values.toList()

    fun getVM(vmId: Int): VMInstance? = vms[vmId]

    /**
     * Creates a new VM instance with coherent real-device hardware parameters.
     *
     * @param name Custom VM name
     * @param romConfig Selected ROM configuration (Android 5.1 or 7.1.2)
     * @param displayConfig Selected screen resolution & DPI
     * @param presetId Optional DevicePreset ID; if null, selects a matching Android 5.1/7.1 preset
     */
    @JvmOverloads
    fun createVM(
        name: String,
        romConfig: RomConfig?,
        displayConfig: DisplayConfig? = null,
        presetId: String? = null
    ): VMInstance {
        val id = nextVmId++
        val prefs = context.getSharedPreferences("vm_config_$id", Context.MODE_PRIVATE)
        val finalName = if (TextUtils.isEmpty(name)) {
            "${romConfig?.displayName ?: "Android"}_$id"
        } else {
            name
        }

        // Select DevicePreset (optional preset or matched to ROM version 5.1 / 7.1)
        val preset = if (presetId == DevicePreset.PRESET_CUSTOM) {
            null
        } else {
            DevicePreset.getById(presetId) ?: DevicePreset.getRandomForRom(romConfig?.displayName)
        }

        val carrierInfo = detectCarrier()
        val random = Random()

        // Dynamic non-colliding IP per VM instance (192.168.1.15 .. 192.168.1.240)
        val dynamicHostOctet = 15 + ((id * 19 + random.nextInt(15)) % 225)
        val dynamicLocalIp = "192.168.1.$dynamicHostOctet"

        prefs.edit().apply {
            // Basic & ROM
            putString("name", finalName)
            putString("serial", generateSerial(preset?.brand))
            romConfig?.let {
                putString("rom_config", it.toJson())
                putString("create_rom", it.toJson())
            }
            putLong("create_time", System.currentTimeMillis())

            // Apply DevicePreset (Brand, Model, Device, Board, Baseband, GPU, Fingerprint)
            if (preset != null) {
                preset.applyToPreferences(this)
            } else {
                putString("device_preset_id", DevicePreset.PRESET_CUSTOM)
                putString("base_band", "8996-012901-1702171013")
                putString("prop.ro.build.tags", "release-keys")
                putString("prop.ro.build.type", "user")
                putString("prop.ro.debuggable", "0")
                putString("prop.ro.secure", "1")
            }

            // Hardware & Telephony (Luhn-valid IMEI using preset's authentic TAC prefix)
            putBoolean("is_gsm_phone", true)
            putString("imei", generateImei(preset?.tacPrefix ?: "352931"))
            putString("imei_sv", String.format(Locale.US, "%02d", 1 + random.nextInt(25)))
            putBoolean("sim_enabled", true)
            putString("sim_carrier", carrierInfo[0])
            putString("sim_spn", carrierInfo[1])
            putString("sim_mccmnc", carrierInfo[2])
            putString("sim_iccid", generateIccid())
            putString("sim_imsi", generateImsi(carrierInfo[2]))

            // Cellular Network
            putString("phone_network_carrier", carrierInfo[0])
            putString("phone_network_spn", carrierInfo[1])
            putString("phone_network_mccmnc", carrierInfo[2])
            putString("phone_network_type", "LTE")
            putString("phone_signal_strength", "good")
            putString("phone_network_status", "home")
            putString("phone_sms_option", "none")
            putString("phone_dial_option", "none")

            // Wi-Fi & LAN Network Hardware (IEEE Valid Vendor OUI + Distinct Router BSSID + Dynamic IP)
            val deviceMac = DevicePreset.generateVendorMac(preset?.macOui)
            val routerBssid = DevicePreset.generateRouterBssid()
            putString("wifi_ssid", DevicePreset.generateNaturalWifiSsid())
            putString("wifi_bssid", routerBssid)
            putString("local_mac", deviceMac)
            putString("local_ip", dynamicLocalIp)
            putString("gateway_ip", "192.168.1.1")
            putString("network", "192.168.1.0")
            putString("netmask", "255.255.255.0")
            putString("dns4_ip", "8.8.8.8")

            // Display & Hardware Screen
            val disp = displayConfig ?: DisplayConfig(name = "Standard 720p", width = 720, height = 1280, dpi = 320)
            putString("display_config_name", disp.name)
            putInt("display_width", disp.width)
            putInt("display_height", disp.height)
            putInt("display_dpi", disp.dpi)
            putInt("refresh_rate", 60)
            putBoolean("notch_screen_enable", true)
            putBoolean("builtin_nav_bar", true)
            putBoolean("builtin_nav_bar_rtl", false)

            // Full Sensor Support
            putBoolean("gyro_enabled", true)
            putBoolean("accelerometer_enabled", true)
            putStringSet(
                "sensor_values",
                setOf(
                    "android.sensor.accelerometer=full_support",
                    "android.sensor.gyroscope=full_support",
                    "android.sensor.magnetic_field=full_support",
                    "android.sensor.light=full_support",
                    "android.sensor.proximity=full_support",
                    "android.sensor.pressure=full_support"
                )
            )

            // Peripherals & Features
            putBoolean("camera_enabled", false)
            putBoolean("share_clipboard", true)
            putBoolean("share_folder", false)
            putBoolean("share_notification", false)
            putString("location_type", "device_realtime")

            apply()
        }

        val inst = VMInstance(context, id)
        inst.config = VMConfig.fromPreferences(prefs)
        vms[id] = inst
        return inst
    }

    /**
     * Applies a DevicePreset to an existing VM instance.
     */
    fun applyPresetToVM(vmId: Int, presetId: String): Boolean {
        val inst = vms[vmId] ?: return false
        val preset = DevicePreset.getById(presetId) ?: return false
        val prefs = context.getSharedPreferences("vm_config_$vmId", Context.MODE_PRIVATE)
        prefs.edit().apply {
            preset.applyToPreferences(this)
            putString("local_mac", DevicePreset.generateVendorMac(preset.macOui))
            putString("imei", generateImei(preset.tacPrefix))
            apply()
        }
        inst.config = VMConfig.fromPreferences(prefs)
        return true
    }

    fun deleteVM(vmId: Int): Boolean {
        val inst = vms.remove(vmId) ?: return false
        inst.stop()
        val vmDir = inst.rootDir
        if (vmDir.exists()) {
            NativeHelper.deleteRecursively(vmDir.absolutePath)
        }
        val prefs = context.getSharedPreferences("vm_config_$vmId", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        return true
    }

    private fun detectCarrier(): Array<String> {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        var carrierId = 0
        var carrierName = ""
        var operator = ""

        if (tm != null && Build.VERSION.SDK_INT >= 28) {
            try {
                carrierId = tm.simCarrierId
                carrierName = tm.simOperatorName ?: ""
                operator = tm.simOperator ?: ""
            } catch (_: Exception) {}
        }

        val valid = carrierId != 0 && carrierName.isNotEmpty() && operator.isNotEmpty()
        val idStr = if (valid) carrierId.toString() else "1"
        val nameStr = if (valid) carrierName else "T-Mobile"
        val opStr = if (valid) operator else "310260"
        return arrayOf(idStr, nameStr, opStr)
    }

    private fun generateSerial(brand: String? = null): String {
        val chars = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ"
        val prefix = when (brand?.lowercase(Locale.US)) {
            "samsung" -> "R58M"
            "google" -> "HT75"
            "xiaomi" -> "4852"
            "oneplus" -> "ONEP"
            "sony" -> "CB5A"
            else -> "G020"
        }
        val sb = StringBuilder(prefix)
        val random = Random()
        repeat(8) {
            sb.append(chars[random.nextInt(chars.length)])
        }
        return sb.toString()
    }

    private fun generateImei(tacPrefix: String = "352931"): String {
        val random = Random()
        val cleanTac = if (tacPrefix.length == 6 && tacPrefix.all { it.isDigit() }) tacPrefix else "352931"
        val sb = StringBuilder(cleanTac)
        sb.append(String.format(Locale.US, "%02d", random.nextInt(100)))
        sb.append(String.format(Locale.US, "%06d", random.nextInt(1000000)))
        sb.append(calculateLuhnChecksum(sb.toString()))
        return sb.toString()
    }

    private fun generateIccid(): String {
        val random = Random()
        val sb = StringBuilder("89")
        sb.append(String.format(Locale.US, "%02d", random.nextInt(100)))
        sb.append(String.format(Locale.US, "%02d", random.nextInt(100)))
        sb.append(String.format(Locale.US, "%012d", random.nextInt(1000000000)))
        sb.append(calculateLuhnChecksum(sb.toString()))
        return sb.toString()
    }

    private fun generateImsi(mccmnc: String): String {
        val random = Random()
        val sb = StringBuilder(mccmnc)
        if (mccmnc.length == 5) {
            sb.append(String.format(Locale.US, "%010d", random.nextInt(1000000000)))
        } else {
            sb.append(String.format(Locale.US, "%09d", random.nextInt(100000000)))
        }
        return sb.toString()
    }

    private fun calculateLuhnChecksum(numberStr: String): Int {
        var sum = 0
        for (i in numberStr.indices) {
            var digit = numberStr[i].digitToIntOrNull() ?: 0
            if (i % 2 == 0) {
                digit *= 2
                if (digit > 9) digit = (digit / 10) + (digit % 10)
            }
            sum += digit
        }
        return (10 - (sum % 10)) % 10
    }

    companion object {
        @Volatile
        private var instance: VMManager? = null

        @JvmStatic
        fun getInstance(context: Context): VMManager {
            return instance ?: synchronized(this) {
                instance ?: VMManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
