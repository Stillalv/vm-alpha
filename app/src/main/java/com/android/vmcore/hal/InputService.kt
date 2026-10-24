package com.android.vmcore.hal

import android.view.MotionEvent
import com.android.vmcore.KLog

object InputService {
    @JvmStatic
    fun sendTouchEvent(action: Int, pointerId: Int, x: Float, y: Float, pressure: Float, eventTimeNanos: Long): Int {
        return nativeSendTouchEvent(action, pointerId, x, y, pressure, eventTimeNanos)
    }

    @JvmStatic
    fun sendKeyEvent(keyCode: Int, isDown: Boolean): Int {
        return nativeSendKeyEvent(keyCode, isDown)
    }

    @JvmStatic
    private external fun nativeSendTouchEvent(action: Int, pointerId: Int, x: Float, y: Float, pressure: Float, timeNanos: Long): Int

    @JvmStatic
    private external fun nativeSendKeyEvent(keyCode: Int, isDown: Boolean): Int
}
