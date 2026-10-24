package com.android.vmcore.ui

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import com.android.vmcore.VMInstance
import com.android.vmcore.hal.InputService

interface VMSurfaceListener {
    fun onSurfaceCreated()
    fun onSurfaceDestroyed()
    fun onSurfaceChanged(width: Int, height: Int)
}

class VMSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), SurfaceHolder.Callback, View.OnTouchListener {

    private val surfaceView: SurfaceView = SurfaceView(context)
    var vmInstance: VMInstance? = null
    var surfaceListener: VMSurfaceListener? = null

    init {
        addView(surfaceView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        surfaceView.holder.addCallback(this)
        surfaceView.setOnTouchListener(this)
        keepScreenOn = true
        setBackgroundColor(0xFF000000.toInt())
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        vmInstance?.halManager?.displayService?.setSurface(holder.surface, width, height)
        surfaceListener?.onSurfaceCreated()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        vmInstance?.halManager?.displayService?.setSurface(holder.surface, width, height)
        surfaceListener?.onSurfaceChanged(width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        vmInstance?.halManager?.displayService?.clearSurface()
        surfaceListener?.onSurfaceDestroyed()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event == null) return false
        val action = event.actionMasked
        val pointerIdx = event.actionIndex
        val pointerId = event.getPointerId(pointerIdx)
        val x = event.getX(pointerIdx)
        val y = event.getY(pointerIdx)
        val pressure = event.getPressure(pointerIdx)
        val timeNanos = event.eventTime * 1_000_000L
        InputService.sendTouchEvent(action, pointerId, x, y, pressure, timeNanos)
        return true
    }
}
