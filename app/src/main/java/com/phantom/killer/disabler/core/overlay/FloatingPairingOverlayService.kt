package com.phantom.killer.disabler.core.overlay

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager

class FloatingPairingOverlayService : Service() {
    private var windowManager: WindowManager? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
