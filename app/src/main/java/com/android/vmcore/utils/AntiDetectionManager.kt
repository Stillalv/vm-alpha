package com.android.vmcore.utils

import java.io.File

/**
 * AntiDetectionManager sanitizes guest filesystem mounts, memory maps, and build properties
 * to prevent apps inside the VM from detecting the host container package
 * (com.clone.android.dual.space), virtualization shared libraries (libvm.so, libkr*.so),
 * or root/hooking artifacts.
 */
object AntiDetectionManager {

    private const val HOST_PACKAGE_NAME = "com.alpha.android.dual.space"
    private const val LEGACY_HOST_PACKAGE_NAME = "com.clone.android.dual.space"

    private val HIDDEN_MOUNT_KEYWORDS = listOf(
        HOST_PACKAGE_NAME,
        LEGACY_HOST_PACKAGE_NAME,
        "/virtual-simulator",
        "libvm.so",
        "libkr32",
        "libkr64",
        "libkrloader",
        "magisk",
        "superuser",
        "xposed",
        "frida",
        "substrate"
    )

    private val HIDDEN_MAP_KEYWORDS = listOf(
        HOST_PACKAGE_NAME,
        LEGACY_HOST_PACKAGE_NAME,
        "libvm.so",
        "libun7z.so",
        "libkr32.so",
        "libkr32.11.so",
        "libkr32.12.so",
        "libkr64.so",
        "libkr64.11.so",
        "libkr64.12.so",
        "libkrloader32.so",
        "libkrloader64.so",
        "frida-agent",
        "XposedBridge.jar"
    )

    /**
     * Sanitizes content from /proc/self/mounts or /proc/mounts:
     * - Strips host data path prefix (/data/user/0/com.clone.android.dual.space/vm/X/rootfs)
     * - Removes lines exposing host container internal directories
     */
    @JvmStatic
    fun sanitizeProcMounts(rawMountsContent: String, rootFsDir: File): String {
        val rootPrefix = rootFsDir.absolutePath
        return rawMountsContent.lineSequence()
            .map { line ->
                var cleaned = line.replace(rootPrefix, "")
                if (cleaned.contains(HOST_PACKAGE_NAME) || cleaned.contains(LEGACY_HOST_PACKAGE_NAME)) {
                    cleaned = cleaned.replace(
                        Regex("/data/(?:data|user/\\d+)/(?:$HOST_PACKAGE_NAME|$LEGACY_HOST_PACKAGE_NAME)/[^\\s]*"),
                        "/dev/block/bootdevice/by-name/system"
                    )
                }
                cleaned
            }
            .filter { line ->
                HIDDEN_MOUNT_KEYWORDS.none { keyword -> line.contains(keyword, ignoreCase = true) }
            }
            .joinToString("\n")
    }

    /**
     * Sanitizes content from /proc/self/maps or /proc/<pid>/maps:
     * - Removes memory mapping lines referencing host container libraries or package directory
     */
    @JvmStatic
    fun sanitizeProcMaps(rawMapsContent: String, rootFsDir: File): String {
        val rootPrefix = rootFsDir.absolutePath
        return rawMapsContent.lineSequence()
            .filter { line ->
                HIDDEN_MAP_KEYWORDS.none { keyword -> line.contains(keyword, ignoreCase = true) }
            }
            .map { line ->
                line.replace(rootPrefix, "")
            }
            .joinToString("\n")
    }

    /**
     * Generates clean anti-detection system properties for default.prop and build.prop
     */
    @JvmStatic
    fun getHardenedSystemProperties(): Map<String, String> = mapOf(
        "ro.build.tags" to "release-keys",
        "ro.build.type" to "user",
        "ro.debuggable" to "0",
        "ro.secure" to "1",
        "ro.adb.secure" to "1",
        "ro.boot.verifiedbootstate" to "green",
        "ro.boot.flash.locked" to "1",
        "ro.boot.vbmeta.device_state" to "locked",
        "ro.boot.veritymode" to "enforcing",
        "ro.warranty_bit" to "0",
        "ro.boot.warranty_bit" to "0",
        "sys.oem_unlock_allowed" to "0",
        "ro.kernel.qemu" to "0",
        "ro.boot.qemu" to "0"
    )

    /**
     * Applies anti-detection sanitization to guest rootfs prop files (/default.prop and /system/build.prop)
     */
    @JvmStatic
    fun patchGuestPropFiles(rootFsDir: File, customProps: Map<String, String>) {
        val mergedProps = HashMap<String, String>()
        mergedProps.putAll(getHardenedSystemProperties())
        customProps.forEach { (k, v) ->
            val propKey = k.removePrefix("prop.")
            if (v.isNotEmpty()) {
                mergedProps[propKey] = v
            }
        }

        listOf(
            File(rootFsDir, "default.prop"),
            File(rootFsDir, "system/build.prop"),
            File(rootFsDir, "vendor/build.prop")
        ).forEach { propFile ->
            if (propFile.exists() && propFile.canWrite()) {
                try {
                    val existingLines = propFile.readLines().toMutableList()
                    val writtenKeys = HashSet<String>()
                    val updatedLines = existingLines.map { line ->
                        val trimmed = line.trim()
                        if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains("=")) {
                            line
                        } else {
                            val key = trimmed.substringBefore("=").trim()
                            if (mergedProps.containsKey(key)) {
                                writtenKeys.add(key)
                                "$key=${mergedProps[key]}"
                            } else if (line.contains("test-keys")) {
                                line.replace("test-keys", "release-keys")
                            } else {
                                line
                            }
                        }
                    }.toMutableList()

                    mergedProps.forEach { (k, v) ->
                        if (!writtenKeys.contains(k)) {
                            updatedLines.add("$k=$v")
                        }
                    }
                    propFile.writeText(updatedLines.joinToString("\n") + "\n")
                } catch (_: Exception) {
                }
            }
        }
    }
}
