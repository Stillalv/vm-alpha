package com.android.vmcore.event

import android.content.Context
import android.content.Intent
import com.android.vmcore.KLog
import java.util.concurrent.CopyOnWriteArrayList

interface VMEventListener {
    fun onEvent(action: String, extras: Map<String, Any?>)
}

object VMEventManager {
    private const val TAG = "VMEventManager"
    private val listeners = CopyOnWriteArrayList<VMEventListener>()

    fun registerListener(listener: VMEventListener) {
        if (!listeners.contains(listener)) listeners.add(listener)
    }

    fun unregisterListener(listener: VMEventListener) {
        listeners.remove(listener)
    }

    fun dispatchEvent(action: String, extras: Map<String, Any?> = emptyMap()) {
        KLog.d(TAG, "Dispatching event: $action")
        for (l in listeners) {
            try {
                l.onEvent(action, extras)
            } catch (e: Throwable) {
                KLog.e(TAG, "Error in event listener", e)
            }
        }
    }
}
