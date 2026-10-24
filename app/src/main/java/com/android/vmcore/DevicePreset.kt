package com.android.vmcore

import android.content.SharedPreferences
import java.util.Locale
import java.util.Random

/**
 * Bundled authentic hardware device presets locking together:
 * Brand, Model, Device Code, Manufacturer, Board, Platform, Baseband, GPU Vendor/Renderer,
 * Build Fingerprint, IEEE MAC OUI, and IMEI TAC prefix.
 *
 * Primarily tailored for Android 5.1 (Lollipop) and Android 7.1.2 (Nougat) guest ROMs,
 * plus optional modern flagship profiles.
 */
data class DevicePreset(
    val id: String,
    val displayName: String,
    val androidVersion: String,
    val sdkInt: Int,
    val brand: String,
    val manufacturer: String,
    val model: String,
    val productName: String,
    val deviceCode: String,
    val board: String,
    val hardware: String,
    val platform: String,
    val cpuAbiList: String,
    val baseBand: String,
    val gpuVendor: String,
    val gpuRenderer: String,
    val buildId: String,
    val fingerprint: String,
    val securityPatch: String,
    val macOui: String,
    val tacPrefix: String
) {
    val gpuConfigValue: String
        get() = "$gpuVendor|$gpuRenderer"

    val name: String get() = productName
    val device: String get() = deviceCode
    val baseband: String get() = baseBand

    /**
     * Applies this preset's complete hardware & build.prop identity to a VM's SharedPreferences.
     */
    fun applyToPreferences(editor: SharedPreferences.Editor) {
        editor.putString("device_preset_id", id)
        editor.putString("prop.ro.product.brand", brand)
        editor.putString("prop.ro.product.manufacturer", manufacturer)
        editor.putString("prop.ro.product.model", model)
        editor.putString("prop.ro.product.name", productName)
        editor.putString("prop.ro.product.device", deviceCode)
        editor.putString("prop.ro.product.board", board)
        editor.putString("prop.ro.hardware", hardware)
        editor.putString("prop.ro.board.platform", platform)
        editor.putString("prop.ro.product.cpu.abilist", cpuAbiList)
        editor.putString("prop.ro.build.id", buildId)
        editor.putString("prop.ro.build.fingerprint", fingerprint)
        editor.putString("prop.ro.bootimage.build.fingerprint", fingerprint)
        editor.putString("prop.ro.vendor.build.fingerprint", fingerprint)
        editor.putString("prop.ro.build.version.security_patch", securityPatch)
        editor.putString("prop.ro.build.tags", "release-keys")
        editor.putString("prop.ro.build.type", "user")
        editor.putString("prop.ro.debuggable", "0")
        editor.putString("prop.ro.secure", "1")
        editor.putString("prop.ro.boot.verifiedbootstate", "green")
        editor.putString("prop.ro.boot.flash.locked", "1")
        editor.putString("base_band", baseBand)
        editor.putString("gpu_vendor", gpuVendor)
        editor.putString("gpu_renderer", gpuRenderer)
    }

    companion object {
        const val PRESET_CUSTOM = "custom"

        /**
         * Authentic IEEE Registered Vendor OUIs (Unicast first byte)
         */
        val VENDOR_MAC_OUIS = listOf(
            "34:23:87", // Samsung Electronics
            "D0:22:BE", // Samsung Electronics
            "F8:CF:C5", // Google Inc.
            "3C:5A:B4", // Google Inc.
            "64:CC:2E", // Xiaomi Communications
            "28:6C:07", // Xiaomi Communications
            "94:65:2D", // OnePlus Tech
            "A0:E4:53", // Sony Mobile
            "00:34:DA", // LG Electronics
            "48:DB:50", // Huawei Tech
            "8C:0E:E3", // Guangdong Oppo Mobile
            "E8:91:20", // Motorola Mobility
            "04:D9:F5"  // ASUSTek Computer
        )

        /**
         * Authentic Router BSSID OUIs (TP-Link, Asus, Netgear, Huawei, Ubiquiti, Cisco)
         */
        val ROUTER_MAC_OUIS = listOf(
            "50:C7:BF", // TP-Link
            "14:CC:20", // TP-Link
            "04:D9:F5", // ASUS Router
            "9C:3D:CF", // Netgear
            "88:28:B3", // Huawei Home Gateway
            "78:8A:20", // Ubiquiti Networks
            "00:24:D7", // Intel / Home AP
            "C8:3A:35"  // Tenda
        )

        /**
         * Complete catalog of Android 7.1, Android 5.1, and Optional Modern Flagship Presets
         */
        val ALL_PRESETS: List<DevicePreset> = listOf(
            // =========================================================================
            // ANDROID 7.1.x (NOUGAT) AUTHENTIC FLAGSHIP PRESETS
            // =========================================================================
            DevicePreset(
                id = "pixel_xl_7_1",
                displayName = "Google Pixel XL (Android 7.1.2)",
                androidVersion = "7.1.2",
                sdkInt = 25,
                brand = "google",
                manufacturer = "Google",
                model = "Pixel XL",
                productName = "marlin",
                deviceCode = "marlin",
                board = "marlin",
                hardware = "marlin",
                platform = "msm8996",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "8996-012901-1702171013",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 530",
                buildId = "NJH47F",
                fingerprint = "google/marlin/marlin:7.1.2/NJH47F/4146041:user/release-keys",
                securityPatch = "2017-10-05",
                macOui = "F8:CF:C5",
                tacPrefix = "352931"
            ),
            DevicePreset(
                id = "pixel_7_1",
                displayName = "Google Pixel (Android 7.1.2)",
                androidVersion = "7.1.2",
                sdkInt = 25,
                brand = "google",
                manufacturer = "Google",
                model = "Pixel",
                productName = "sailfish",
                deviceCode = "sailfish",
                board = "sailfish",
                hardware = "sailfish",
                platform = "msm8996",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "8996-012901-1702171013",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 530",
                buildId = "NJH47F",
                fingerprint = "google/sailfish/sailfish:7.1.2/NJH47F/4146041:user/release-keys",
                securityPatch = "2017-10-05",
                macOui = "3C:5A:B4",
                tacPrefix = "351615"
            ),
            DevicePreset(
                id = "galaxy_s8_7_1",
                displayName = "Samsung Galaxy S8 (Android 7.1)",
                androidVersion = "7.1.2",
                sdkInt = 25,
                brand = "samsung",
                manufacturer = "samsung",
                model = "SM-G950F",
                productName = "dreamltexx",
                deviceCode = "dreamlte",
                board = "universal8895",
                hardware = "samsungexynos8895",
                platform = "exynos5",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "G950FXXU1AQJ5",
                gpuVendor = "ARM",
                gpuRenderer = "Mali-G71",
                buildId = "NRD90M",
                fingerprint = "samsung/dreamltexx/dreamlte:7.1.2/NRD90M/G950FXXU1AQJ5:user/release-keys",
                securityPatch = "2017-10-01",
                macOui = "34:23:87",
                tacPrefix = "356419"
            ),
            DevicePreset(
                id = "galaxy_s7_edge_7_1",
                displayName = "Samsung Galaxy S7 Edge (Android 7.1)",
                androidVersion = "7.1.2",
                sdkInt = 25,
                brand = "samsung",
                manufacturer = "samsung",
                model = "SM-G935F",
                productName = "hero2ltexx",
                deviceCode = "hero2lte",
                board = "universal8890",
                hardware = "samsungexynos8890",
                platform = "exynos5",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "G935FXXU1DQJ1",
                gpuVendor = "ARM",
                gpuRenderer = "Mali-T880",
                buildId = "NRD90M",
                fingerprint = "samsung/hero2ltexx/hero2lte:7.1.2/NRD90M/G935FXXU1DQJ1:user/release-keys",
                securityPatch = "2017-09-01",
                macOui = "D0:22:BE",
                tacPrefix = "357329"
            ),
            DevicePreset(
                id = "oneplus_5_7_1",
                displayName = "OnePlus 5 (Android 7.1.1)",
                androidVersion = "7.1.1",
                sdkInt = 25,
                brand = "OnePlus",
                manufacturer = "OnePlus",
                model = "ONEPLUS A5000",
                productName = "OnePlus5",
                deviceCode = "OnePlus5",
                board = "msm8998",
                hardware = "qcom",
                platform = "msm8998",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "MPSS.AT.2.0.c4-00060",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 540",
                buildId = "NMF26X",
                fingerprint = "OnePlus/OnePlus5/OnePlus5:7.1.1/NMF26X/10231025:user/release-keys",
                securityPatch = "2017-10-01",
                macOui = "94:65:2D",
                tacPrefix = "864691"
            ),
            DevicePreset(
                id = "xiaomi_mi6_7_1",
                displayName = "Xiaomi Mi 6 (Android 7.1.1)",
                androidVersion = "7.1.1",
                sdkInt = 25,
                brand = "Xiaomi",
                manufacturer = "Xiaomi",
                model = "MI 6",
                productName = "sagit",
                deviceCode = "sagit",
                board = "msm8998",
                hardware = "qcom",
                platform = "msm8998",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "AT20-0307_2214_3754711",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 540",
                buildId = "NMF26X",
                fingerprint = "Xiaomi/sagit/sagit:7.1.1/NMF26X/V9.2.3.0.NCACNEK:user/release-keys",
                securityPatch = "2017-11-01",
                macOui = "64:CC:2E",
                tacPrefix = "864266"
            ),
            DevicePreset(
                id = "nexus_6p_7_1",
                displayName = "Huawei Nexus 6P (Android 7.1.2)",
                androidVersion = "7.1.2",
                sdkInt = 25,
                brand = "google",
                manufacturer = "Huawei",
                model = "Nexus 6P",
                productName = "angler",
                deviceCode = "angler",
                board = "angler",
                hardware = "angler",
                platform = "msm8994",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "angler-03.81",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 430",
                buildId = "N2G48C",
                fingerprint = "google/angler/angler:7.1.2/N2G48C/4104010:user/release-keys",
                securityPatch = "2017-08-05",
                macOui = "48:DB:50",
                tacPrefix = "354143"
            ),

            // =========================================================================
            // ANDROID 5.1.x (LOLLIPOP) AUTHENTIC FLAGSHIP PRESETS
            // =========================================================================
            DevicePreset(
                id = "galaxy_s6_5_1",
                displayName = "Samsung Galaxy S6 (Android 5.1.1)",
                androidVersion = "5.1.1",
                sdkInt = 22,
                brand = "samsung",
                manufacturer = "samsung",
                model = "SM-G920F",
                productName = "zerofltexx",
                deviceCode = "zeroflte",
                board = "universal7420",
                hardware = "samsungexynos7420",
                platform = "exynos5",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "G920FXXU3COI9",
                gpuVendor = "ARM",
                gpuRenderer = "Mali-T760",
                buildId = "LMY47X",
                fingerprint = "samsung/zerofltexx/zeroflte:5.1.1/LMY47X/G920FXXU3COI9:user/release-keys",
                securityPatch = "2015-11-01",
                macOui = "34:23:87",
                tacPrefix = "359528"
            ),
            DevicePreset(
                id = "galaxy_note5_5_1",
                displayName = "Samsung Galaxy Note 5 (Android 5.1.1)",
                androidVersion = "5.1.1",
                sdkInt = 22,
                brand = "samsung",
                manufacturer = "samsung",
                model = "SM-N920C",
                productName = "nobleltexx",
                deviceCode = "noblelte",
                board = "universal7420",
                hardware = "samsungexynos7420",
                platform = "exynos5",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "N920CXXU2AOJ5",
                gpuVendor = "ARM",
                gpuRenderer = "Mali-T760",
                buildId = "LMY47X",
                fingerprint = "samsung/nobleltexx/noblelte:5.1.1/LMY47X/N920CXXU2AOJ5:user/release-keys",
                securityPatch = "2015-11-01",
                macOui = "D0:22:BE",
                tacPrefix = "353410"
            ),
            DevicePreset(
                id = "nexus_6_5_1",
                displayName = "Motorola Nexus 6 (Android 5.1.1)",
                androidVersion = "5.1.1",
                sdkInt = 22,
                brand = "google",
                manufacturer = "motorola",
                model = "Nexus 6",
                productName = "shamu",
                deviceCode = "shamu",
                board = "shamu",
                hardware = "shamu",
                platform = "msm8084",
                cpuAbiList = "armeabi-v7a,armeabi",
                baseBand = "MDM9625_104662.22.05.27R",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 420",
                buildId = "LMY48Y",
                fingerprint = "google/shamu/shamu:5.1.1/LMY48Y/2364368:user/release-keys",
                securityPatch = "2015-11-01",
                macOui = "E8:91:20",
                tacPrefix = "355470"
            ),
            DevicePreset(
                id = "nexus_5_5_1",
                displayName = "LG Nexus 5 (Android 5.1.1)",
                androidVersion = "5.1.1",
                sdkInt = 22,
                brand = "google",
                manufacturer = "LGE",
                model = "Nexus 5",
                productName = "hammerhead",
                deviceCode = "hammerhead",
                board = "hammerhead",
                hardware = "hammerhead",
                platform = "msm8974",
                cpuAbiList = "armeabi-v7a,armeabi",
                baseBand = "M8974A-2.0.50.2.26",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 330",
                buildId = "LMY48M",
                fingerprint = "google/hammerhead/hammerhead:5.1.1/LMY48M/2167285:user/release-keys",
                securityPatch = "2015-10-01",
                macOui = "00:34:DA",
                tacPrefix = "358239"
            ),
            DevicePreset(
                id = "lg_g4_5_1",
                displayName = "LG G4 H815 (Android 5.1)",
                androidVersion = "5.1",
                sdkInt = 22,
                brand = "lge",
                manufacturer = "LGE",
                model = "LG-H815",
                productName = "p1_global_com",
                deviceCode = "p1",
                board = "msm8992",
                hardware = "p1",
                platform = "msm8992",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "MPSS.BO.2.5.c3-00101",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 418",
                buildId = "LMY47D",
                fingerprint = "lge/p1_global_com/p1:5.1/LMY47D/151421542291c:user/release-keys",
                securityPatch = "2015-09-01",
                macOui = "00:34:DA",
                tacPrefix = "359688"
            ),
            DevicePreset(
                id = "xperia_z5_5_1",
                displayName = "Sony Xperia Z5 (Android 5.1.1)",
                androidVersion = "5.1.1",
                sdkInt = 22,
                brand = "Sony",
                manufacturer = "Sony",
                model = "E6653",
                productName = "E6653",
                deviceCode = "E6653",
                board = "msm8994",
                hardware = "qcom",
                platform = "msm8994",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "8994-FAAAANAZQ-00028-06",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 430",
                buildId = "32.0.A.6.200",
                fingerprint = "Sony/E6653/E6653:5.1.1/32.0.A.6.200/2073272275:user/release-keys",
                securityPatch = "2015-11-01",
                macOui = "A0:E4:53",
                tacPrefix = "352946"
            ),
            DevicePreset(
                id = "redmi_note3_5_1",
                displayName = "Xiaomi Redmi Note 3 Pro (Android 5.1.1)",
                androidVersion = "5.1.1",
                sdkInt = 22,
                brand = "Xiaomi",
                manufacturer = "Xiaomi",
                model = "Redmi Note 3",
                productName = "kenzo",
                deviceCode = "kenzo",
                board = "msm8952",
                hardware = "qcom",
                platform = "msm8952",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "8976_GEN_PACK-1.69049.1",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 510",
                buildId = "LMY47V",
                fingerprint = "Xiaomi/kenzo/kenzo:5.1.1/LMY47V/V7.3.2.0.LHOCNDD:user/release-keys",
                securityPatch = "2016-05-01",
                macOui = "28:6C:07",
                tacPrefix = "869740"
            ),

            // =========================================================================
            // OPTIONAL MODERN FLAGSHIP PRESETS (High-End Gaming / Unlocks)
            // =========================================================================
            DevicePreset(
                id = "galaxy_s23_ultra",
                displayName = "Samsung Galaxy S23 Ultra (Modern Flagship)",
                androidVersion = "13",
                sdkInt = 33,
                brand = "samsung",
                manufacturer = "samsung",
                model = "SM-S918B",
                productName = "dm3qxxx",
                deviceCode = "dm3q",
                board = "kalama",
                hardware = "qcom",
                platform = "kalama",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "S918BXXU1AWBD",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 740",
                buildId = "TP1A.220624.014",
                fingerprint = "samsung/dm3qxxx/dm3q:13/TP1A.220624.014/S918BXXU1AWBD:user/release-keys",
                securityPatch = "2023-04-01",
                macOui = "34:23:87",
                tacPrefix = "350964"
            ),
            DevicePreset(
                id = "xiaomi_13_pro",
                displayName = "Xiaomi 13 Pro (Modern Flagship)",
                androidVersion = "13",
                sdkInt = 33,
                brand = "Xiaomi",
                manufacturer = "Xiaomi",
                model = "2210132G",
                productName = "nuwa_global",
                deviceCode = "nuwa",
                board = "kalama",
                hardware = "qcom",
                platform = "kalama",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "4.0.c5.6-00066-1220_2148",
                gpuVendor = "Qualcomm",
                gpuRenderer = "Adreno (TM) 740",
                buildId = "TKQ1.220829.002",
                fingerprint = "Xiaomi/nuwa_global/nuwa:13/TKQ1.220829.002/V14.0.7.0.TMBMIXM:user/release-keys",
                securityPatch = "2023-03-01",
                macOui = "64:CC:2E",
                tacPrefix = "863195"
            ),
            DevicePreset(
                id = "pixel_7_pro",
                displayName = "Google Pixel 7 Pro (Modern Flagship)",
                androidVersion = "13",
                sdkInt = 33,
                brand = "google",
                manufacturer = "Google",
                model = "Pixel 7 Pro",
                productName = "cheetah",
                deviceCode = "cheetah",
                board = "cheetah",
                hardware = "cheetah",
                platform = "gs201",
                cpuAbiList = "arm64-v8a,armeabi-v7a,armeabi",
                baseBand = "g5300g-221229-230210-B-9589401",
                gpuVendor = "ARM",
                gpuRenderer = "Mali-G710",
                buildId = "TQ2A.230505.002",
                fingerprint = "google/cheetah/cheetah:13/TQ2A.230505.002/9891397:user/release-keys",
                securityPatch = "2023-05-05",
                macOui = "F8:CF:C5",
                tacPrefix = "357690"
            )
        )

        @JvmField
        val DEFAULT_PRESET: DevicePreset = ALL_PRESETS[0]

        @JvmStatic
        fun getPresetById(id: String?): DevicePreset? = getById(id)

        @JvmStatic
        fun getById(id: String?): DevicePreset? {
            if (id.isNullOrEmpty() || id == PRESET_CUSTOM) return null
            return ALL_PRESETS.find { it.id == id }
        }

        /**
         * Selects an authentic preset matching the ROM's Android version (5.1 vs 7.1).
         */
        @JvmStatic
        fun getRandomForRom(romNameOrVersion: String?): DevicePreset {
            val isLollipop = romNameOrVersion?.contains("5.1") == true || romNameOrVersion?.contains("22") == true
            val pool = if (isLollipop) {
                ALL_PRESETS.filter { it.sdkInt == 22 }
            } else {
                ALL_PRESETS.filter { it.sdkInt == 25 }
            }
            val list = pool.ifEmpty { ALL_PRESETS }
            return list[Random().nextInt(list.size)]
        }

        /**
         * Generates a valid IEEE unicast MAC address using an authentic vendor OUI.
         */
        @JvmStatic
        fun generateVendorMac(preferredOui: String? = null): String {
            val random = Random()
            val oui = if (!preferredOui.isNullOrEmpty() && preferredOui.count { it == ':' } == 2) {
                preferredOui.lowercase(Locale.US)
            } else {
                VENDOR_MAC_OUIS[random.nextInt(VENDOR_MAC_OUIS.size)].lowercase(Locale.US)
            }
            return String.format(
                Locale.US,
                "%s:%02x:%02x:%02x",
                oui,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
            )
        }

        /**
         * Generates a valid IEEE router BSSID MAC address (distinct from client MAC).
         */
        @JvmStatic
        fun generateRouterBssid(): String {
            val random = Random()
            val oui = ROUTER_MAC_OUIS[random.nextInt(ROUTER_MAC_OUIS.size)].lowercase(Locale.US)
            return String.format(
                Locale.US,
                "%s:%02x:%02x:%02x",
                oui,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
            )
        }

        /**
         * Generates natural home/router Wi-Fi SSIDs without the predictable '_WIFI' suffix.
         */
        @JvmStatic
        fun generateNaturalWifiSsid(): String {
            val random = Random()
            val hex4 = String.format(Locale.US, "%04X", random.nextInt(0xFFFF))
            val num3 = String.format(Locale.US, "%03d", random.nextInt(1000))
            val templates = listOf(
                "TP-Link_$hex4",
                "TP-Link_5G_$hex4",
                "ASUS_${hex4.take(2)}_5G",
                "IndiHome_$hex4",
                "MyRepublic_$hex4",
                "Biznet_Home_$num3",
                "Huawei-WiFi-$hex4",
                "Netgear_$num3",
                "Xiaomi_${hex4}_5G",
                "Home_Network_$num3"
            )
            return templates[random.nextInt(templates.size)]
        }
    }
}
