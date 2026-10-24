package com.android.vmcore.utils

import android.system.Os
import android.system.OsConstants
import java.io.FileDescriptor

object OsExt {
    @JvmStatic
    fun setNonblock(fd: FileDescriptor, nonblock: Boolean) {
        val flags = Os.fcntlInt(fd, OsConstants.F_GETFL, 0)
        val newFlags = if (nonblock) flags or OsConstants.O_NONBLOCK else flags and OsConstants.O_NONBLOCK.inv()
        Os.fcntlInt(fd, OsConstants.F_SETFL, newFlags)
    }
}
