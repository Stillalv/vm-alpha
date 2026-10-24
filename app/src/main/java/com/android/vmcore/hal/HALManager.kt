package com.android.vmcore.hal

import android.content.Context
import com.android.vmcore.KLog
import com.android.vmcore.VMInstance

class HALManager(
    val context: Context,
    val vm: VMInstance
) {
    private var nativePtr: Long = 0

    val batteryService = BatteryService(context, vm)
    val locationService = LocationHalService(context, vm, this)
    val sensorService = SensorHalService(context, vm, this)
    val phoneService = PhoneHalService(vm, this)
    val displayService = DisplayService()
    val audioService = AudioService(context)
    val netlinkManager = NetlinkManager()

    fun startHALMgr(): Int {
        KLog.d("HALManager", "Starting HALManager for VM ${vm.vmId}")
        locationService.start()
        sensorService.start()
        audioService.start()
        netlinkManager.start()
        return 0
    }

    fun stopHALMgr(): Int {
        KLog.d("HALManager", "Stopping HALManager for VM ${vm.vmId}")
        locationService.stop()
        sensorService.stop()
        audioService.stop()
        netlinkManager.stop()
        return 0
    }
}
