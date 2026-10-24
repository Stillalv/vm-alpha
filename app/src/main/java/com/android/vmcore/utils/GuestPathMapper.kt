package com.android.vmcore.utils

import java.io.File

object GuestPathMapper {
    private const val HOST_PACKAGE = "com.alpha.android.dual.space"
    private const val LEGACY_HOST_PACKAGE = "com.clone.android.dual.space"

    @JvmStatic
    fun hostToGuest(hostPath: String, rootFsDir: File): String {
        val prefix = rootFsDir.absolutePath
        val mapped = if (hostPath.startsWith(prefix)) {
            hostPath.removePrefix(prefix).ifEmpty { "/" }
        } else {
            hostPath
        }
        // Mask any lingering host package references
        return if (mapped.contains(HOST_PACKAGE) || mapped.contains(LEGACY_HOST_PACKAGE)) {
            mapped.replace(Regex("/data/(?:data|user/\\d+)/(?:$HOST_PACKAGE|$LEGACY_HOST_PACKAGE)/[^/]*"), "")
                .ifEmpty { "/" }
        } else {
            mapped
        }
    }

    @JvmStatic
    fun guestToHost(guestPath: String, rootFsDir: File): String {
        return File(rootFsDir, guestPath.trimStart('/')).absolutePath
    }

    @JvmStatic
    fun sanitizeMounts(content: String, rootFsDir: File): String {
        return AntiDetectionManager.sanitizeProcMounts(content, rootFsDir)
    }

    @JvmStatic
    fun sanitizeMaps(content: String, rootFsDir: File): String {
        return AntiDetectionManager.sanitizeProcMaps(content, rootFsDir)
    }
}
