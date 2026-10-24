package com.android.vmcore.vpn

import android.content.Context
import com.android.vmcore.KLog
import com.android.vmcore.VMInstance

class VpnManager(
    val context: Context,
    val vm: VMInstance
) {
    fun startVpn(): Boolean {
        KLog.d("VpnManager", "Starting VPN for VM ${vm.vmId}")
        return true
    }

    fun stopVpn(): Boolean {
        KLog.d("VpnManager", "Stopping VPN for VM ${vm.vmId}")
        return true
    }
}
