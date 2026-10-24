package com.android.vmapp.vm

import android.os.Bundle
import android.view.ViewGroup
import com.android.vmapp.ui.base.BaseActivity
import com.android.vmcore.VMManager
import com.android.vmcore.ui.VMSurfaceView

class VMDisplayActivity : BaseActivity() {
    private var vmId: Int = 0
    private var surfaceView: VMSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vmId = intent.getIntExtra("extra_vm_id", 0)
        val vm = VMManager.getInstance(this).getVM(vmId)
        surfaceView = VMSurfaceView(this).apply {
            vmInstance = vm
        }
        setContentView(surfaceView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        vm?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
