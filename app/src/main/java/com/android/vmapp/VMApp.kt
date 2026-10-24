package com.android.vmapp

import android.app.Application
import android.content.Context
import android.util.SparseArray
import com.android.vmapp.vm.VMExtension
import com.android.vmcore.KLog
import me.weishu.reflection.Reflection

class VMApp : Application() {
    private val vmExtensions = SparseArray<VMExtension>()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this
        try {
            Reflection.unseal(base)
        } catch (e: Throwable) {
            KLog.e(TAG, "Failed to unseal hidden API exemptions", e)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        try {
            System.loadLibrary("vm")
        } catch (e: Throwable) {
            KLog.e(TAG, "Failed to load libvm.so in VMApp", e)
        }
    }

    fun getVMExtension(vmId: Int): VMExtension? {
        return synchronized(vmExtensions) {
            vmExtensions.get(vmId)
        }
    }

    fun setVMExtension(vmId: Int, extension: VMExtension) {
        synchronized(vmExtensions) {
            vmExtensions.put(vmId, extension)
        }
    }

    fun removeVMExtension(vmId: Int) {
        synchronized(vmExtensions) {
            vmExtensions.remove(vmId)
        }
    }

    companion object {
        private const val TAG = "VMApp"
        lateinit var instance: VMApp
            private set
    }
}
