package com.phantom.killer.disabler

import android.content.Context
import android.content.Intent
import android.os.Build
import com.phantom.killer.disabler.domain.model.UnlockStatus
import com.phantom.killer.disabler.internal.PhantomAdbManager
import com.phantom.killer.disabler.ui.MainActivity
import java.util.concurrent.Executors

object DisablerLauncher {
    private val executor = Executors.newSingleThreadExecutor()

    fun interface StatusCallback {
        fun onResult(status: UnlockStatus)
    }

    fun interface UnlockCallback {
        fun onResult(success: Boolean, message: String)
    }

    @JvmStatic
    fun checkStatus(context: Context?, callback: StatusCallback?) {
        if (context == null || callback == null) return
        executor.execute {
            val mgr = PhantomAdbManager(context.applicationContext)
            val status = mgr.checkPhantomStatus()
            callback.onResult(status)
        }
    }

    @JvmStatic
    fun disablePhantomProcessKiller(context: Context?, callback: UnlockCallback?) {
        if (context == null || callback == null) return
        executor.execute {
            val mgr = PhantomAdbManager(context.applicationContext)
            val res = mgr.disablePhantomProcessKiller()
            callback.onResult(res.isSuccess, res.output)
        }
    }

    @JvmStatic
    fun launch(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
