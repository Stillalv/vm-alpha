package com.android.vmapp.vm

import android.content.BroadcastReceiver
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.android.vmapp.VMApp
import com.android.vmcore.VMInstance

class VMExtension(
    val app: VMApp,
    val vm: VMInstance
) : BroadcastReceiver(), ClipboardManager.OnPrimaryClipChangedListener {

    override fun onReceive(context: Context?, intent: Intent?) {
        // Handle broadcast events from guest
    }

    override fun onPrimaryClipChanged() {
        // Sync clipboard to guest
    }
}
