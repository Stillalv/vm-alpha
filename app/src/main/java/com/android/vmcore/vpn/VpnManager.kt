package com.android.vmcore.vpn

import android.content.Context
import com.android.vmcore.KLog
import com.android.vmcore.VMInstance
import java.io.File

class VpnManager(
    private val mContext: Context,
    private val mVM: VMInstance
) {
    private var mNativePtr: Long = 0L

    init {
        mNativePtr = try {
            nativeSetup(mVM.vmId)
        } catch (t: Throwable) {
            KLog.e("VpnManager", "nativeSetup failed", t)
            0L
        }
    }

    private external fun nativeDispose(nativePtr: Long)
    private external fun nativeOnVpnTunClosed(nativePtr: Long, tunFd: Int)
    private external fun nativeOnVpnTunOpened(nativePtr: Long, tunFd: Int)
    private external fun nativeSetup(vmId: Int): Long
    private external fun nativeStartMgr(nativePtr: Long): Int
    private external fun nativeStopMgr(nativePtr: Long): Int

    fun onVpnTunClosed(tunFd: Int) {
        if (mNativePtr != 0L) {
            try {
                nativeOnVpnTunClosed(mNativePtr, tunFd)
            } catch (_: Throwable) {
            }
        }
    }

    fun onVpnTunOpened(tunFd: Int) {
        if (mNativePtr != 0L) {
            try {
                nativeOnVpnTunOpened(mNativePtr, tunFd)
            } catch (_: Throwable) {
            }
        }
    }

    fun start(): Int {
        val file = File(mVM.rootFsDir, "/dev/tun.ctrl_client")
        file.parentFile?.mkdirs()
        if (file.exists()) file.delete()
        if (mNativePtr == 0L) return 0
        return try {
            nativeStartMgr(mNativePtr)
        } catch (_: Throwable) {
            0
        }
    }

    fun stop(): Int {
        val file = File(mVM.rootFsDir, "/dev/tun.ctrl_client")
        if (file.exists()) file.delete()
        if (mNativePtr == 0L) return 0
        return try {
            nativeStopMgr(mNativePtr)
        } catch (_: Throwable) {
            0
        }
    }

    fun startVpnConnection(tunFd: Int, vpnConfig: VpnConfig) {
        if (tunFd < 0) return
        KLog.d("VpnManager", "startVpnConnection fd=$tunFd")
    }

    fun stopVpnConnection(tunFd: Int) {
        if (tunFd < 0) return
        KLog.d("VpnManager", "stopVpnConnection fd=$tunFd")
    }

    fun startVpn(): Boolean {
        start()
        return true
    }

    fun stopVpn(): Boolean {
        stop()
        return true
    }

    protected fun finalize() {
        val ptr = mNativePtr
        if (ptr != 0L) {
            try {
                nativeDispose(ptr)
            } catch (_: Throwable) {
            }
            mNativePtr = 0L
        }
    }
}
