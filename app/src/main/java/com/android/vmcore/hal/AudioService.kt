package com.android.vmcore.hal

import android.content.Context
import android.media.AudioTrack
import com.android.vmcore.KLog

class AudioService(val context: Context) {
    private var isPlaying = false

    fun start() {
        isPlaying = true
        KLog.d("AudioService", "AudioService started")
    }

    fun stop() {
        isPlaying = false
        KLog.d("AudioService", "AudioService stopped")
    }
}
