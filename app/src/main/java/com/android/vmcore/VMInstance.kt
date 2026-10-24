package com.android.vmcore

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.android.vmcore.hal.HALManager
import com.android.vmcore.ui.VMSurfaceView
import com.android.vmcore.vpn.VpnManager
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

class VMInstance(
    val context: Context,
    val vmId: Int
) {
    var config: VMConfig = VMConfig()
    var state: Int = VMState.STATE_STOPPED
        private set

    val halManager: HALManager = HALManager(context, this)
    val vpnManager: VpnManager = VpnManager(context, this)
    var surfaceView: VMSurfaceView? = null

    val rootDir: File = File(context.applicationInfo.dataDir, "vm/vm$vmId")
    val rootFsDir: File = File(rootDir, "rootfs")
    val dataDir: File = File(rootDir, "data")
    val sdcardDir: File = File(rootDir, "sdcard")

    private val stateListeners = CopyOnWriteArrayList<VMStateListener>()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun addStateListener(listener: VMStateListener) {
        if (!stateListeners.contains(listener)) stateListeners.add(listener)
    }

    fun removeStateListener(listener: VMStateListener) {
        stateListeners.remove(listener)
    }

    fun notifyState(newState: Int) {
        val old = state
        state = newState
        mainHandler.post {
            for (l in stateListeners) {
                l.onStateChanged(vmId, newState, old)
            }
        }
    }

    fun start(): Int {
        if (state == VMState.STATE_RUNNING || state == VMState.STATE_STARTING) return 0
        notifyState(VMState.STATE_STARTING)
        KLog.i("VMInstance", "Starting VM $vmId...")
        halManager.startHALMgr()
        vpnManager.startVpn()
        notifyState(VMState.STATE_RUNNING)
        return 0
    }

    fun stop(): Int {
        if (state == VMState.STATE_STOPPED || state == VMState.STATE_STOPPING) return 0
        notifyState(VMState.STATE_STOPPING)
        KLog.i("VMInstance", "Stopping VM $vmId...")
        NativeHelper.clearZombieProcess(vmId)
        halManager.stopHALMgr()
        vpnManager.stopVpn()
        notifyState(VMState.STATE_STOPPED)
        return 0
    }

    fun restart() {
        stop()
        start()
    }
}
