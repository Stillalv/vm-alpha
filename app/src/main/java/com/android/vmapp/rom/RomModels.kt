package com.android.vmapp.rom

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Asset(
    val url: String = "",
    val size: Long = 0L,
    val hash: String = "",
    val name: String = ""
) : Parcelable

class RomException(message: String) : Exception(message)

@Keep
@Parcelize
data class RomModel(
    val rom_id: String = "",
    val rom_name: String = "",
    val rom_icon: String = "",
    val rom_version: Int = 0,
    val os_version: Int = 0,
    val support_a32: Boolean = true,
    val support_a64: Boolean = true,
    val minimum_sdk_int: Int = 21,
    val minimum_app_ver: String = "",
    val rom_asset: List<Asset> = emptyList()
) : Parcelable

/**
 * Default ROM Catalog reconstructed from n3.C4005WWWWWWWW (Play Asset Delivery & Remote Config definitions).
 */
object DefaultRomCatalog {
    val DEFAULT_ROMS: Map<String, RomModel> = linkedMapOf(
        "rom_4_2_2" to RomModel(
            rom_id = "rom_default_4.2.2",
            rom_name = "Android 4.2.2",
            rom_version = 1000,
            os_version = 4,
            support_a32 = true,
            support_a64 = false,
            minimum_sdk_int = 21,
            rom_asset = listOf(Asset(url = "pad://rom_4_2_2", size = 69156234L))
        ),
        "rom_5_1_1" to RomModel(
            rom_id = "rom_default_5.1.1",
            rom_name = "Android 5.1.1",
            rom_version = 1059,
            os_version = 5,
            support_a32 = true,
            support_a64 = true,
            minimum_sdk_int = 21,
            rom_asset = listOf(Asset(url = "pad://rom_5_1_1", size = 231894043L))
        ),
        "rom_7_1_2_32" to RomModel(
            rom_id = "rom_default_7.1.2_32",
            rom_name = "Android 7.1.2 (32-bit)",
            rom_version = 1059,
            os_version = 7,
            support_a32 = true,
            support_a64 = false,
            minimum_sdk_int = 24,
            rom_asset = listOf(Asset(url = "pad://rom_7_1_2_32", size = 246138575L))
        ),
        "rom_7_1_2" to RomModel(
            rom_id = "rom_default_7.1.2",
            rom_name = "Android 7.1.2 (64-bit)",
            rom_version = 1059,
            os_version = 7,
            support_a32 = true,
            support_a64 = true,
            minimum_sdk_int = 24,
            rom_asset = listOf(Asset(url = "pad://rom_7_1_2", size = 312103368L))
        ),
        "rom_9_0_0" to RomModel(
            rom_id = "rom_default_9.0.0",
            rom_name = "Android 9.0.0",
            rom_version = 1059,
            os_version = 9,
            support_a32 = false,
            support_a64 = true,
            minimum_sdk_int = 28,
            rom_asset = listOf(Asset(url = "pad://rom_9_0_0", size = 295470236L))
        ),
        "rom_11_0_0" to RomModel(
            rom_id = "rom_default_11.0.0",
            rom_name = "Android 11.0.0",
            rom_version = 1036,
            os_version = 11,
            support_a32 = false,
            support_a64 = true,
            minimum_sdk_int = 30,
            rom_asset = listOf(Asset(url = "pad://rom_11_0_0", size = 367928823L))
        )
    )
}
