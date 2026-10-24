package com.android.vmcore

import java.util.ArrayList

object NativeHelper {
    init {
        try {
            System.loadLibrary("vm")
        } catch (e: Throwable) {
            KLog.e("NativeHelper", "Failed to load libvm.so", e)
        }
    }

    @JvmStatic
    external fun buildVMProcessReport(vmId: Int, reportPath: String)

    @JvmStatic
    external fun chmodRecursively(path: String, mode: Int): Int

    @JvmStatic
    external fun clearZombieProcess(vmId: Int): Int

    @JvmStatic
    external fun deleteRecursively(path: String): Int

    @JvmStatic
    external fun getProcessList(vmId: Int): ArrayList<VMProcessInfo>

    @JvmStatic
    external fun printXAttr(path: String)

    @JvmStatic
    external fun startOS(vmId: Int, guestUid: Int, guestGid: Int, cmd: String): Int
}
