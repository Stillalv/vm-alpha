package com.android.vmcore

interface VMStateListener {
    fun onStateChanged(vmId: Int, newState: Int, oldState: Int)
    fun onError(vmId: Int, errorCode: Int, message: String)
}

object VMState {
    const val STATE_STOPPED = 0
    const val STATE_STARTING = 1
    const val STATE_RUNNING = 2
    const val STATE_STOPPING = 3
    const val STATE_ERROR = 4
}
