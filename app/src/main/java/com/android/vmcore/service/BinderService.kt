package com.android.vmcore.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.util.SparseArray
import com.android.vmcore.KLog

class BinderService : Service() {
    private val binder = object : Binder(), IBinderService {
        init {
            attachInterface(this, DESCRIPTOR)
        }

        override fun asBinder(): IBinder = this

        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            if (code in 1..16777215) {
                data.enforceInterface(DESCRIPTOR)
            }
            if (code == 1598968902) {
                reply?.writeString(DESCRIPTOR)
                return true
            }
            return when (code) {
                1 -> {
                    val vmId = data.readInt()
                    val slot = data.readInt()
                    val remote = data.readStrongBinder()
                    when (slot) {
                        0 -> slot0.put(vmId, remote)
                        1 -> slot1.put(vmId, remote)
                        2 -> slot2.put(vmId, remote)
                    }
                    reply?.writeNoException()
                    true
                }
                2 -> {
                    val vmId = data.readInt()
                    val slot = data.readInt()
                    val remote = when (slot) {
                        0 -> slot0.get(vmId)
                        1 -> slot1.get(vmId)
                        2 -> slot2.get(vmId)
                        else -> null
                    }
                    reply?.writeNoException()
                    reply?.writeStrongBinder(remote)
                    true
                }
                else -> super.onTransact(code, data, reply, flags)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        KLog.i(TAG, "[onBind]")
        return binder
    }

    override fun onCreate() {
        KLog.i(TAG, "[onCreate]")
        super.onCreate()
    }

    override fun onDestroy() {
        KLog.i(TAG, "[onDestroy]")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        KLog.i(TAG, "[onStartCommand]")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        KLog.i(TAG, "[onUnbind]")
        return super.onUnbind(intent)
    }

    companion object {
        private const val TAG = "BinderService"
        private const val DESCRIPTOR = "com.android.vmcore.service.IBinderService"

        @JvmField val slot0 = SparseArray<IBinder>()
        @JvmField val slot1 = SparseArray<IBinder>()
        @JvmField val slot2 = SparseArray<IBinder>()

        @JvmStatic
        external fun getBinderVersion(): Int

        @JvmStatic
        external fun setupBinder(
            vmId: Int,
            peekCode: Int,
            setCode: Int,
            getCode: Int,
            descriptor: String,
            data: ByteArray
        ): Int

        @JvmStatic
        fun publishBinder(vmId: Int, peekCode: Int, data: ByteArray): Int {
            return try {
                setupBinder(vmId, peekCode, 1, 2, DESCRIPTOR, data)
            } catch (t: Throwable) {
                KLog.e(TAG, "setupBinder failed", t)
                -1
            }
        }
    }
}
