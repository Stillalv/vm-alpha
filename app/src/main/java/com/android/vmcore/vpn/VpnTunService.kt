package com.android.vmcore.vpn

import com.android.vmcore.KLog

object VpnTunService {
    @JvmStatic
    fun setupTun(fd: Int, ip: String, port: Int): Int {
        KLog.d("VpnTunService", "Setting up TUN on fd=$fd to $ip:$port")
        return 0
    }
}
