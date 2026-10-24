package com.android.vmcore

import org.json.JSONArray
import org.json.JSONObject

class RomConfig {
    var id: String = ""
    var displayName: String = ""
    var romVersion: Int = 0
    var osVersion: Int = 0
    var supportA32: Boolean = true
    var supportA64: Boolean = true
    var minimumSdkInt: Int = 0
    var minimumAppVer: String = ""
    var romUri: Array<String> = emptyArray()
    var overlayUri: Array<String> = emptyArray()
    var magiskUri: String = ""
    var suUri: String = ""
    var xposedUri: String = ""
    var playUri: String = ""

    companion object {
        private fun parseStringArray(json: JSONObject, key: String): Array<String> {
            val opt = json.opt(key) ?: return emptyArray()
            return when (opt) {
                is JSONArray -> Array(opt.length()) { opt.getString(it) }
                else -> arrayOf(opt.toString())
            }
        }

        @JvmStatic
        fun fromJson(jsonStr: String): RomConfig {
            val cfg = RomConfig()
            val obj = JSONObject(jsonStr)
            cfg.id = obj.optString("id", "")
            cfg.displayName = obj.optString("display_name", "")
            cfg.romVersion = obj.optInt("rom_version", 0)
            cfg.osVersion = obj.optInt("os_version", 0)
            cfg.supportA32 = obj.optBoolean("support_a32", true)
            cfg.supportA64 = obj.optBoolean("support_a64", true)
            cfg.minimumSdkInt = obj.optInt("minimum_sdk_int", 0)
            cfg.minimumAppVer = obj.optString("minimum_app_ver", "")
            cfg.romUri = parseStringArray(obj, "rom_uri")
            cfg.overlayUri = parseStringArray(obj, "overlay_uri")
            cfg.magiskUri = obj.optString("magisk_uri", "")
            cfg.suUri = obj.optString("su_uri", "")
            cfg.xposedUri = obj.optString("xposed_uri", "")
            cfg.playUri = obj.optString("play_uri", "")
            return cfg
        }
    }

    fun toJson(): String {
        val obj = JSONObject()
        obj.put("id", id)
        obj.put("display_name", displayName)
        obj.put("rom_version", romVersion)
        obj.put("os_version", osVersion)
        obj.put("support_a32", supportA32)
        obj.put("support_a64", supportA64)
        obj.put("minimum_sdk_int", minimumSdkInt)
        obj.put("minimum_app_ver", minimumAppVer)
        obj.put("rom_uri", JSONArray(romUri))
        obj.put("overlay_uri", JSONArray(overlayUri))
        obj.put("magisk_uri", magiskUri)
        obj.put("su_uri", suUri)
        obj.put("xposed_uri", xposedUri)
        obj.put("play_uri", playUri)
        return obj.toString()
    }
}
