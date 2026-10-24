package com.android.vmcore.hal

import android.view.Surface
import com.android.vmcore.KLog
import com.android.vmcore.VMInstance

class DisplayService @JvmOverloads constructor(
    private val vm: VMInstance? = null
) {
    private var mNativePtr: Long = 0L

    init {
        if (vm != null) {
            mNativePtr = try {
                nativeSetup(vm.vmId, 7, 1)
            } catch (t: Throwable) {
                KLog.e("DisplayService", "nativeSetup failed", t)
                0L
            }
        }
    }

    private external fun nativeAddSurface(
        nativePtr: Long,
        surfaceId: Int,
        surface: Surface,
        width: Int,
        height: Int,
        rotation: Float
    ): Boolean

    private external fun nativeDispose(nativePtr: Long)
    private external fun nativeGetFPS(nativePtr: Long): Float
    private external fun nativeRemoveSurface(nativePtr: Long, surfaceId: Int): Boolean
    private external fun nativeSetup(vmId: Int, sdkVersion: Int, subVersion: Int): Long
    private external fun nativeStartService(nativePtr: Long, width: Int, height: Int): Int
    private external fun nativeStopService(nativePtr: Long): Int

    fun addSurface(surfaceId: Int, surface: Surface, width: Int, height: Int, rotation: Float = 0f) {
        if (mNativePtr != 0L) {
            try {
                nativeAddSurface(mNativePtr, surfaceId, surface, width, height, rotation)
            } catch (_: Throwable) {
            }
        }
    }

    fun removeSurface(surfaceId: Int) {
        if (mNativePtr != 0L) {
            try {
                nativeRemoveSurface(mNativePtr, surfaceId)
            } catch (_: Throwable) {
            }
        }
    }

    fun setSurface(surface: Surface?, width: Int, height: Int) {
        if (surface != null && surface.isValid) {
            addSurface(surface.hashCode(), surface, width, height, 0f)
        }
    }

    fun clearSurface() {
        if (mNativePtr != 0L) {
            removeSurface(0)
        }
    }

    fun start(width: Int, height: Int): Int {
        if (mNativePtr == 0L) return 0
        return try {
            nativeStartService(mNativePtr, width, height)
        } catch (_: Throwable) {
            0
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

    fun getFPS(): Float {
        if (mNativePtr == 0L) return 0f
        return try {
            nativeGetFPS(mNativePtr)
        } catch (_: Throwable) {
            0f
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
