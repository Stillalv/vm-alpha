package com.android.vmcore.vpn

import com.android.vmcore.KLog

class VpnTunService {
    private external fun nativeAddVMTun(loopPtr: Long, vmId: Int, tunFd: Int): Int
    private external fun nativeDeinitTunLoop(loopPtr: Long)
    private external fun nativeDelVMTun(loopPtr: Long, vmId: Int, tunFd: Int): Int
    private external fun nativeInitTunLoop(hostTunFd: Int, vmIds: IntArray, vmTunFds: IntArray): Long
    private external fun nativeStartTunLoop(loopPtr: Long): Int
    private external fun nativeStopTunLoop(loopPtr: Long): Int

    companion object {
        @Volatile
        private var instance: VpnTunService? = null

        @JvmStatic
        fun getInstance(): VpnTunService {
            return instance ?: synchronized(VpnTunService::class.java) {
                instance ?: VpnTunService().also { instance = it }
            }
        }

        @JvmStatic
        fun setupTun(fd: Int, ip: String, port: Int): Int {
            KLog.d("VpnTunService", "Setting up TUN on fd=$fd to $ip:$port")
            return 0
        }
    }
}
