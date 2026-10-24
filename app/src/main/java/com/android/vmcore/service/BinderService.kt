package com.android.vmcore.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.android.vmcore.KLog

class BinderService : Service() {
    private val binder = object : Binder(), IBinderService {
        override fun asBinder(): IBinder = this
    }

    override fun onBind(intent: Intent?): IBinder = binder

    companion object {
        private const val TAG = "BinderService"

        @JvmStatic
        fun getBinderVersion(): Int {
            return try {
                nativeGetBinderVersion()
            } catch (e: Throwable) {
                1
            }
        }

        @JvmStatic
        fun setupBinder(fd: Int) {
            try {
                nativeSetupBinder(fd)
            } catch (e: Throwable) {
                KLog.e(TAG, "Failed to setup binder", e)
            }
        }

        @JvmStatic
        private external fun nativeGetBinderVersion(): Int

        @JvmStatic
        private external fun nativeSetupBinder(fd: Int)
    }
}
