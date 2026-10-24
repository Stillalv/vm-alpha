package com.phantom.killer.disabler.domain.model

import android.os.Build

enum class BrandType(
    val key: String,
    val displayName: String
) {
    XIAOMI("Xiaomi", "Xiaomi / Redmi / POCO (HyperOS / MIUI)"),
    OPPO("OPPO", "OPPO / OnePlus / realme (ColorOS)"),
    VIVO("vivo", "vivo / iQOO (OriginOS / FuntouchOS)"),
    SAMSUNG("Samsung", "Samsung (One UI)"),
    GENERIC("Generic", "Standard Android / Other");

    companion object {
        @JvmStatic
        fun detectCurrentDevice(): BrandType {
            val manufacturer = Build.MANUFACTURER?.lowercase() ?: ""
            val brand = Build.BRAND?.lowercase() ?: ""
            return when {
                manufacturer.contains("xiaomi") || brand.contains("redmi") || brand.contains("poco") -> XIAOMI
                manufacturer.contains("oppo") || brand.contains("oneplus") || brand.contains("realme") -> OPPO
                manufacturer.contains("vivo") || brand.contains("iqoo") -> VIVO
                manufacturer.contains("samsung") -> SAMSUNG
                else -> GENERIC
            }
        }

        @JvmStatic
        fun fromKey(key: String?): BrandType {
            return when {
                "xiaomi".equals(key, ignoreCase = true) -> XIAOMI
                "oppo".equals(key, ignoreCase = true) -> OPPO
                "vivo".equals(key, ignoreCase = true) -> VIVO
                "samsung".equals(key, ignoreCase = true) -> SAMSUNG
                else -> GENERIC
            }
        }
    }
}
