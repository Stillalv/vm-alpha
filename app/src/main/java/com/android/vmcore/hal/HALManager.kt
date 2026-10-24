package com.android.vmcore.hal

import android.content.Context
import com.android.vmcore.KLog
import com.android.vmcore.VMInstance

class HALManager(
    val context: Context,
    val vm: VMInstance
) {
    private var mNativePtr: Long = 0L

    val batteryService = BatteryService(context, vm)
    val locationService = LocationHalService(context, vm, this)
    val sensorService = SensorHalService(context, vm, this)
    val phoneService = PhoneHalService(vm, this)
    val displayService = DisplayService(vm)
    val audioService = AudioService(context, vm)
    val netlinkManager = NetlinkManager(context, vm)

    init {
        mNativePtr = try {
            nativeSetup(vm.vmId)
        } catch (t: Throwable) {
            KLog.e("HALManager", "nativeSetup failed", t)
            0L
        }
    }

    // --- JNI Callbacks invoked by libvm.so ---

    private fun CameraConnect(cameraId: String?): Int {
        return 0
    }

    private fun CameraDisconnect(cameraId: String?): Int {
        return 0
    }

    private fun CameraFlash(cameraId: String?, mode: String?): Int {
        return 0
    }

    private fun CameraFocus(
        cameraId: String?,
        focusMode: String?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        weight: Int
    ): Int {
        return 0
    }

    private fun CameraFrame(
        cameraId: String?,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        zoom: Int,
        mode: String?
    ): Int {
        return 0
    }

    private fun CameraList(): String {
        return ""
    }

    private fun CameraStart(cameraId: String?, width: Int, height: Int, format: Int): Int {
        return 0
    }

    private fun CameraStop(cameraId: String?): Int {
        return 0
    }

    private fun CheckSensorsSupport(sensorType: Int): Boolean {
        return sensorType in 0..11
    }

    private fun DisableSensors(sensorType: Int) {
    }

    private fun EnableSensors(sensorType: Int) {
    }

    private fun ExecPhoneCommand(cmdId: Int, arg: String?): String {
        return "OK"
    }

    private fun SetDelay(sensorType: Int, delay: Int) {
    }

    private fun SetGPSStart() {
        locationService.start()
    }

    private fun SetGPSStop() {
        locationService.stop()
    }

    private fun SetWiFiStart() {
        WIFIChanged("${vm.config.wifiSsid},${vm.config.wifiBssid}")
    }

    private fun Vibrate(arg: String?): String {
        return "null"
    }

    private fun getPicture(cameraId: String?, width: Int, height: Int, format: Int): Int {
        return 0
    }

    // --- Native methods registered by libvm.so ---

    private external fun nativeCameraPicture(nativePtr: Long, cameraId: String, data: ByteArray)
    private external fun nativeCameraPreview(nativePtr: Long, cameraId: String, data: ByteArray)
    private external fun nativeDispose(nativePtr: Long)
    private external fun nativeGPSNmeaChanged(nativePtr: Long, nmea: String)
    private external fun nativePhoneUnsolicited(nativePtr: Long, response: String)
    private external fun nativeSensorChanged(
        nativePtr: Long,
        sensorType: Int,
        timestamp: Long,
        x: Float,
        y: Float,
        z: Float
    )
    private external fun nativeSetup(vmId: Int): Long
    private external fun nativeStartHALMgr(nativePtr: Long): Int
    private external fun nativeStopHALMgr(nativePtr: Long): Int
    private external fun nativeWIFIChanged(nativePtr: Long, wifiInfo: String)

    // --- Public API ---

    fun CameraPicture(cameraId: String, data: ByteArray) {
        if (mNativePtr != 0L) {
            try {
                nativeCameraPicture(mNativePtr, cameraId, data)
            } catch (_: Throwable) {
            }
        }
    }

    fun CameraPreview(cameraId: String, data: ByteArray) {
        if (mNativePtr != 0L) {
            try {
                nativeCameraPreview(mNativePtr, cameraId, data)
            } catch (_: Throwable) {
            }
        }
    }

    fun GPSNmeaChanged(nmea: String) {
        if (mNativePtr != 0L) {
            try {
                nativeGPSNmeaChanged(mNativePtr, nmea)
            } catch (_: Throwable) {
            }
        }
    }

    fun PhoneUnsolicited(response: String) {
        if (mNativePtr != 0L) {
            try {
                nativePhoneUnsolicited(mNativePtr, response)
            } catch (_: Throwable) {
            }
        }
    }

    fun SensorChanged(sensorType: Int, timestamp: Long, x: Float, y: Float, z: Float) {
        if (mNativePtr != 0L) {
            try {
                nativeSensorChanged(mNativePtr, sensorType, timestamp, x, y, z)
            } catch (_: Throwable) {
            }
        }
    }

    fun WIFIChanged(wifiInfo: String) {
        if (mNativePtr != 0L) {
            try {
                nativeWIFIChanged(mNativePtr, wifiInfo)
            } catch (_: Throwable) {
            }
        }
    }

    fun onBackground() {
    }

    fun onForeground() {
    }

    fun startHALMgr(): Int {
        KLog.d("HALManager", "Starting HALManager for VM ${vm.vmId}")
        locationService.start()
        sensorService.start()
        audioService.start()
        netlinkManager.start()
        if (mNativePtr == 0L) return 0
        return try {
            nativeStartHALMgr(mNativePtr)
        } catch (_: Throwable) {
            0
        }
    }

    fun stopHALMgr(): Int {
        KLog.d("HALManager", "Stopping HALManager for VM ${vm.vmId}")
        locationService.stop()
        sensorService.stop()
        audioService.stop()
        netlinkManager.stop()
        if (mNativePtr == 0L) return 0
        return try {
            nativeStopHALMgr(mNativePtr)
        } catch (_: Throwable) {
            0
        }
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
