package com.android.vmcore.hal

import com.android.vmcore.KLog
import com.android.vmcore.VMInstance

class PhoneHalService(
    val vm: VMInstance,
    val halManager: HALManager
) {
    fun phoneUnsolicited(msg: String) {
        KLog.d("PhoneHalService", "Unsolicited: $msg")
    }
}
