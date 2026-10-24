package com.android.vmcore.task

import com.android.vmcore.KLog
import com.android.vmcore.NativeHelper
import com.android.vmcore.VMInstance
import com.android.vmcore.utils.AntiDetectionManager

class FixFsTask(val vm: VMInstance) {
    fun execute(): Boolean {
        KLog.i("FixFsTask", "Fixing filesystem permissions & applying anti-detection props for VM ${vm.vmId}")
        AntiDetectionManager.patchGuestPropFiles(vm.rootFsDir, vm.config.properties)
        NativeHelper.chmodRecursively(vm.rootFsDir.absolutePath, 509) // 0775
        return true
    }
}
