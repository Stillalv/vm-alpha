package com.android.vmcore.utils

import android.system.Os
import android.system.OsConstants
import java.io.FileDescriptor

object OsExt {
    @JvmStatic
    fun lgetxattr(path: String, name: String): ByteArray? {
        return try {
            nativeLgetxattr(path, name)
        } catch (_: Throwable) {
            null
        }
    }

    @JvmStatic
    fun fchmodat(mode: Int, path: String) {
        try {
            nativeFchmodat(-100, path, mode, 0x100)
        } catch (_: Throwable) {
        }
    }

    @JvmStatic
    fun lsetxattr(path: String, name: String, value: ByteArray) {
        try {
            nativeLsetxattr(path, name, value, 0)
        } catch (_: Throwable) {
        }
    }

    @JvmStatic
    fun llistxattr(path: String): Array<String>? {
        return try {
            nativeLlistxattr(path)
        } catch (_: Throwable) {
            null
        }
    }

    @JvmStatic
    fun setNonblock(fd: FileDescriptor, nonblock: Boolean) {
        val flags = Os.fcntlInt(fd, OsConstants.F_GETFL, 0)
        val newFlags = if (nonblock) flags or OsConstants.O_NONBLOCK else flags and OsConstants.O_NONBLOCK.inv()
        Os.fcntlInt(fd, OsConstants.F_SETFL, newFlags)
    }

    @JvmStatic
    external fun nativeErrno(): Int

    @JvmStatic
    external fun nativeFchmodat(dirfd: Int, pathname: String, mode: Int, flags: Int): Int

    @JvmStatic
    external fun nativeGetxattr(path: String, name: String): ByteArray

    @JvmStatic
    external fun nativeLgetxattr(path: String, name: String): ByteArray

    @JvmStatic
    external fun nativeListxattr(path: String): Array<String>

    @JvmStatic
    external fun nativeLlistxattr(path: String): Array<String>

    @JvmStatic
    external fun nativeLsetxattr(path: String, name: String, value: ByteArray, flags: Int): Int

    @JvmStatic
    external fun nativeSetxattr(path: String, name: String, value: ByteArray, flags: Int): Int
}
