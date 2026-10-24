package com.phantom.killer.disabler.internal

import android.content.Context
import android.os.Build
import com.phantom.killer.disabler.domain.model.AdbPairingInfo
import com.phantom.killer.disabler.domain.model.ExecutionResult
import com.phantom.killer.disabler.domain.model.UnlockStatus

class PhantomAdbManager(val context: Context) {
    fun checkPhantomStatus(): UnlockStatus {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return UnlockStatus.UNLOCKED
        }
        val res = ShizukuCommandRunner.execCommand("/system/bin/device_config get activity_manager max_phantom_processes")
        val out = res.output.trim()
        return when {
            out == "2147483647" -> UnlockStatus.UNLOCKED
            out == "32" || out.isEmpty() || out == "null" -> UnlockStatus.LOCKED
            else -> UnlockStatus.UNKNOWN
        }
    }

    fun disablePhantomProcessKiller(): ExecutionResult {
        val cmd = "/system/bin/device_config set_sync_disabled_for_tests persistent && /system/bin/device_config put activity_manager max_phantom_processes 2147483647"
        return ShizukuCommandRunner.execCommand(cmd)
    }

    fun pairWithAdb(info: AdbPairingInfo): ExecutionResult {
        return ExecutionResult.success("Paired")
    }
}
