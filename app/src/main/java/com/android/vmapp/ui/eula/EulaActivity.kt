package com.android.vmapp.ui.eula

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.android.vmapp.config.AppPreferences
import com.android.vmapp.ui.MainActivity
import com.android.vmapp.ui.base.BaseActivity
import com.clone.android.dual.space.R

class EulaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_eula)
        findViewById<Button>(R.id.agree)?.setOnClickListener {
            AppPreferences(this).isEulaAccepted = true
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
