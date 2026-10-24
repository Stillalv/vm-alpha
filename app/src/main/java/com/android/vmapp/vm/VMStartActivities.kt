package com.android.vmapp.vm

import android.content.Intent
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import com.android.vmapp.ui.base.BaseActivity
import com.clone.android.dual.space.R

open class BaseVMStartActivity(val vmId: Int) : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_start)
        findViewById<LottieAnimationView>(R.id.loading_animation)?.playAnimation()
        window.decorView.postDelayed({
            val intent = Intent(this, VMDisplayActivity::class.java).apply {
                putExtra("extra_vm_id", vmId)
            }
            startActivity(intent)
            finish()
        }, 1500)
    }
}

class VMStartActivity0 : BaseVMStartActivity(0)
class VMStartActivity1 : BaseVMStartActivity(1)
class VMStartActivity2 : BaseVMStartActivity(2)
class VMStartActivity3 : BaseVMStartActivity(3)

class VMStopActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}

class VMReportActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}
