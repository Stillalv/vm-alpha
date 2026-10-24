package com.android.vmapp.ui.points

import android.os.Bundle
import com.android.vmapp.ui.base.BaseActivity
import com.clone.android.dual.space.R

class FreeVipActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_vip)
    }
}

class WatchAdActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_watch_ad_guide)
    }
}
