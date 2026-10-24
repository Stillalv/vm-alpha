package com.phantom.killer.disabler.core.notification

import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.phantom.killer.disabler.DisablerLauncher
import com.phantom.killer.disabler.domain.model.AdbPairingInfo
import com.phantom.killer.disabler.internal.PhantomAdbManager

class PairingNotificationReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_DIRECT_UNLOCK = "com.phantom.killer.disabler.ACTION_DIRECT_UNLOCK"
        const val ACTION_SUBMIT_PAIRING_CODE = "com.phantom.killer.disabler.ACTION_SUBMIT_PAIRING_CODE"
        const val ACTION_AUTO_RETRY = "com.phantom.killer.disabler.ACTION_AUTO_RETRY"
        const val KEY_PAIRING_CODE = "key_pairing_code_reply"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        val action = intent.action ?: return
        val mgr = PhantomAdbManager(context)

        when (action) {
            ACTION_DIRECT_UNLOCK -> {
                mgr.disablePhantomProcessKiller()
            }
            ACTION_SUBMIT_PAIRING_CODE -> {
                val results = RemoteInput.getResultsFromIntent(intent)
                val code = results?.getCharSequence(KEY_PAIRING_CODE)?.toString()
                if (!code.isNullOrEmpty()) {
                    mgr.pairWithAdb(AdbPairingInfo(code = code, port = 0))
                }
            }
            ACTION_AUTO_RETRY -> {
                mgr.disablePhantomProcessKiller()
            }
        }
    }
}
