package com.android.vmcore

import android.content.SharedPreferences
import com.blankj.utilcode.util.ReflectUtils
import java.io.File
import java.util.HashMap

class DisplayConfig(
    var name: String = "",
    var width: Int = 720,
    var height: Int = 1280,
    var dpi: Int = 320
)

class VMConfig {
    var id: Int = 0
    var name: String = ""
    var devicePresetId: String = DevicePreset.PRESET_CUSTOM
    var romConfig: RomConfig? = null
    var serial: String = ""
    var properties: HashMap<String, String> = HashMap()
    var isGsmPhone: Boolean = true
    var imei: String = ""
    var imeiSv: String = ""
    var meid: String = ""
    var esn: String = ""
    var baseBand: String = ""
    var simEnabled: Boolean = true
    var simCarrier: String = ""
    var simSpn: String = ""
    var simMccMnc: String = ""
    var simIccid: String = ""
    var simImsi: String = ""
    var simPhoneNumber: String = ""
    var phoneNetworkCarrier: String = ""
    var phoneNetworkSpn: String = ""
    var phoneNetworkMccMnc: String = ""
    var phoneNetworkType: String = "LTE"
    var phoneSignalStrength: String = "good"
    var phoneNetworkStatus: String = "home"
    var phoneSmsOption: String = "none"
    var phoneDialOption: String = "none"
    var wifiSsid: String = ""
    var wifiBssid: String = ""
    var shareWifiList: Boolean = false
    var localMac: String = ""
    var localIp: String = ""
    var gatewayIp: String = "192.168.1.1"
    var network: String = "192.168.1.0"
    var netmask: String = "255.255.255.0"
    var ipv6Prefix: String = ""
    var dns4Ip: String = "8.8.8.8"
    var dns6Ip: String = ""
    var socks5Enabled: Boolean = false
    var socks5Server: String = ""
    var socks5Port: Int = -1
    var socks5Username: String = ""
    var socks5Password: String = ""
    var socks5DnsEnabled: Boolean = false
    var socks5Dns4: String = ""
    var socks5Dns6: String = ""
    var adbEnabled: Boolean = false
    var magiskEnabled: Boolean = false
    var rootEnabled: Boolean = false
    var xposedEnabled: Boolean = false
    var playEnabled: Boolean = false
    var displayConfig: DisplayConfig = DisplayConfig()
    var refreshRate: Int = 60
    var notchScreenEnable: Boolean = true
    var builtinNavBar: Boolean = true
    var builtinNavBarRtl: Boolean = false
    var sensorValues: HashMap<String, String> = HashMap()
    var cameraEnabled: Boolean = false
    var shareClipboard: Boolean = true
    var shareFolder: Boolean = false
    var shareNotification: Boolean = false
    var locationType: String = "device_realtime"
    var userLocation: String = ""
    var gpuVendor: String = ""
    var gpuRenderer: String = ""

    companion object {
        @JvmStatic
        fun fromPreferences(prefs: SharedPreferences): VMConfig {
            val cfg = VMConfig()
            cfg.name = prefs.getString("name", "") ?: ""
            cfg.devicePresetId = prefs.getString("device_preset_id", DevicePreset.PRESET_CUSTOM) ?: DevicePreset.PRESET_CUSTOM
            val romJson = prefs.getString("rom_config", "") ?: ""
            if (romJson.isNotEmpty()) {
                cfg.romConfig = RomConfig.fromJson(romJson)
                cfg.romConfig?.let { r ->
                    if ("asset:///rom/rom_7_1_2/magisk.zip" == r.magiskUri) r.magiskUri = "asset:///plugins/magisk.zip"
                    if ("asset:///rom/rom_7_1_2/superuser.zip" == r.suUri) r.suUri = "asset:///plugins/superuser.zip"
                    if ("asset:///rom/rom_7_1_2/xposed.zip" == r.xposedUri) r.xposedUri = "asset:///plugins/xposed.zip"
                    if ("asset:///rom/rom_7_1_2/play.zip" == r.playUri) r.playUri = "asset:///plugins/play.zip"
                }
            }
            cfg.serial = prefs.getString("serial", "") ?: ""
            val all = prefs.all
            for ((key, value) in all) {
                if (key.startsWith("prop.") && value != null) {
                    cfg.properties[key] = value.toString()
                }
            }
            cfg.isGsmPhone = prefs.getBoolean("is_gsm_phone", true)
            cfg.imei = prefs.getString("imei", "") ?: ""
            cfg.imeiSv = prefs.getString("imei_sv", "") ?: ""
            cfg.meid = prefs.getString("meid", "") ?: ""
            cfg.esn = prefs.getString("esn", "") ?: ""
            cfg.baseBand = prefs.getString("base_band", "") ?: ""
            cfg.simEnabled = prefs.getBoolean("sim_enabled", true)
            cfg.simCarrier = prefs.getString("sim_carrier", "") ?: ""
            cfg.simSpn = prefs.getString("sim_spn", "") ?: ""
            cfg.simMccMnc = prefs.getString("sim_mccmnc", "") ?: ""
            cfg.simIccid = prefs.getString("sim_iccid", "") ?: ""
            cfg.simImsi = prefs.getString("sim_imsi", "") ?: ""
            cfg.simPhoneNumber = prefs.getString("sim_phone_number", "") ?: ""
            cfg.phoneNetworkCarrier = prefs.getString("phone_network_carrier", "") ?: ""
            cfg.phoneNetworkSpn = prefs.getString("phone_network_spn", "") ?: ""
            cfg.phoneNetworkMccMnc = prefs.getString("phone_network_mccmnc", "") ?: ""
            cfg.phoneNetworkType = prefs.getString("phone_network_type", "LTE") ?: "LTE"
            cfg.phoneSignalStrength = prefs.getString("phone_signal_strength", "good") ?: "good"
            cfg.phoneNetworkStatus = prefs.getString("phone_network_status", "home") ?: "home"
            cfg.phoneSmsOption = prefs.getString("phone_sms_option", "none") ?: "none"
            cfg.phoneDialOption = prefs.getString("phone_dial_option", "none") ?: "none"
            cfg.wifiSsid = prefs.getString("wifi_ssid", "") ?: ""
            cfg.wifiBssid = prefs.getString("wifi_bssid", "") ?: ""
            cfg.shareWifiList = prefs.getBoolean("share_wifi_list", false)
            cfg.localMac = prefs.getString("local_mac", "") ?: ""
            cfg.localIp = prefs.getString("local_ip", "192.168.1.25") ?: "192.168.1.25"
            cfg.gatewayIp = prefs.getString("gateway_ip", "192.168.1.1") ?: "192.168.1.1"
            cfg.network = prefs.getString("network", "192.168.1.0") ?: "192.168.1.0"
            cfg.netmask = prefs.getString("netmask", "255.255.255.0") ?: "255.255.255.0"
            cfg.ipv6Prefix = prefs.getString("ipv6_prefix", "") ?: ""
            cfg.dns4Ip = prefs.getString("dns4_ip", "8.8.8.8") ?: "8.8.8.8"
            cfg.dns6Ip = prefs.getString("dns6_ip", "") ?: ""
            cfg.socks5Enabled = prefs.getBoolean("socks5_enabled", false)
            cfg.socks5Server = prefs.getString("socks5_server", "") ?: ""
            cfg.socks5Port = prefs.getInt("socks5_port", -1)
            cfg.socks5Username = prefs.getString("socks5_username", "") ?: ""
            cfg.socks5Password = prefs.getString("socks5_password", "") ?: ""
            cfg.socks5DnsEnabled = prefs.getBoolean("socks5_dns_enabled", false)
            cfg.socks5Dns4 = prefs.getString("socks5_dns4", "") ?: ""
            cfg.socks5Dns6 = prefs.getString("socks5_dns6", "") ?: ""
            cfg.adbEnabled = prefs.getBoolean("adb_enabled", false)
            cfg.magiskEnabled = prefs.getBoolean("magisk_enabled", false)
            cfg.rootEnabled = prefs.getBoolean("root_enabled", false)
            cfg.xposedEnabled = prefs.getBoolean("xposed_enabled", false)
            cfg.playEnabled = prefs.getBoolean("play_enabled", false)

            cfg.displayConfig = DisplayConfig(
                name = prefs.getString("display_config_name", "") ?: "",
                width = prefs.getInt("display_width", 720),
                height = prefs.getInt("display_height", 1280),
                dpi = prefs.getInt("display_dpi", 320)
            )
            cfg.refreshRate = prefs.getInt("refresh_rate", 60)
            cfg.notchScreenEnable = prefs.getBoolean("notch_screen_enable", true)
            cfg.builtinNavBar = prefs.getBoolean("builtin_nav_bar", true)
            cfg.builtinNavBarRtl = prefs.getBoolean("builtin_nav_bar_rtl", false)

            val sensorSet = prefs.getStringSet("sensor_values", null)
            if (sensorSet != null) {
                for (entry in sensorSet) {
                    val parts = entry.split("=")
                    if (parts.size == 2) cfg.sensorValues[parts[0]] = parts[1]
                }
            } else {
                cfg.sensorValues["android.sensor.gyroscope"] = if (prefs.getBoolean("gyro_enabled", true)) "full_support" else "not_support"
                cfg.sensorValues["android.sensor.accelerometer"] = if (prefs.getBoolean("accelerometer_enabled", true)) "full_support" else "not_support"
                cfg.sensorValues["android.sensor.light"] = "full_support"
                cfg.sensorValues["android.sensor.proximity"] = "full_support"
                cfg.sensorValues["android.sensor.magnetic_field"] = "full_support"
            }

            cfg.cameraEnabled = prefs.getBoolean("camera_enabled", false)
            cfg.shareClipboard = prefs.getBoolean("share_clipboard", true)
            cfg.shareFolder = prefs.getBoolean("share_folder", false)
            cfg.shareNotification = prefs.getBoolean("share_notification", false)
            cfg.locationType = prefs.getString("location_type", "device_realtime") ?: "device_realtime"
            cfg.userLocation = prefs.getString("user_location", "") ?: ""
            cfg.gpuVendor = prefs.getString("gpu_vendor", "") ?: ""
            cfg.gpuRenderer = prefs.getString("gpu_renderer", "") ?: ""
            return cfg
        }

        @JvmStatic
        fun fromFile(file: File): VMConfig {
            val prefs = ReflectUtils.reflect("android.app.SharedPreferencesImpl").newInstance(file, 0).get<SharedPreferences>()
            return fromPreferences(prefs)
        }
    }
}
