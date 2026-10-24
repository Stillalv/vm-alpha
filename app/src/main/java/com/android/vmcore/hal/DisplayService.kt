package com.android.vmcore.hal

import android.view.Surface
import com.android.vmcore.KLog

class DisplayService {
    private var surface: Surface? = null

    fun setSurface(surface: Surface?, width: Int, height: Int) {
        this.surface = surface
        KLog.d("DisplayService", "Display surface updated: ${width}x$height")
    }

    fun clearSurface() {
        this.surface = null
        KLog.d("DisplayService", "Display surface cleared")
    }
}
