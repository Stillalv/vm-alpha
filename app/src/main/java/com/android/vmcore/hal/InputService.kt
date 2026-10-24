package com.android.vmcore.hal

import com.android.vmcore.KLog
import com.android.vmcore.VMInstance

class InputService @JvmOverloads constructor(
    private val vm: VMInstance? = null
) {
    private var mNativePtr: Long = 0L

    init {
        if (vm != null) {
            mNativePtr = try {
                nativeSetup(vm.vmId, false)
            } catch (t: Throwable) {
                KLog.e("InputService", "nativeSetup failed", t)
                0L
            }
        }
    }

    private external fun nativeDispose(nativePtr: Long)
    private external fun nativeOnTouchEvent(
        nativePtr: Long,
        action: Int,
        pointerId: Int,
        eventTimeNanos: Long,
        x: Float,
        y: Float
    ): Boolean

    private external fun nativeSetup(vmId: Int, isMultiTouchV2: Boolean): Long
    private external fun nativeStartService(nativePtr: Long): Int
    private external fun nativeStopService(nativePtr: Long): Int

    fun start(): Int {
        if (mNativePtr == 0L) return 0
        return try {
            nativeStartService(mNativePtr)
        } catch (_: Throwable) {
            0
        }
    }

    fun onTouchEvent(action: Int, pointerId: Int, eventTimeNanos: Long, x: Float, y: Float) {
        if (mNativePtr != 0L) {
            try {
                nativeOnTouchEvent(mNativePtr, action, pointerId, eventTimeNanos, x, y)
            } catch (_: Throwable) {
            }
        }
    }

    fun stop() {
        if (mNativePtr != 0L) {
            try {
                nativeStopService(mNativePtr)
            } catch (_: Throwable) {
            }
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

    companion object {
        @JvmStatic
        fun sendTouchEvent(action: Int, pointerId: Int, x: Float, y: Float, pressure: Float, eventTimeNanos: Long): Int {
            return 0
        }

        @JvmStatic
        fun sendKeyEvent(keyCode: Int, isDown: Boolean): Int {
            return 0
        }
    }
}
