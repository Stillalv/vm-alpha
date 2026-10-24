package com.android.vmapp.ui.vm.privacy

import android.os.Bundle
import com.android.vmapp.ui.base.BaseActivity
import com.clone.android.dual.space.R

class PrivacyMainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_main)
    }
}

class PrivacyAddActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_add)
    }
}

class PrivacySettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_setting)
    }
}
