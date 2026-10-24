package com.android.vmapp.ui.eula

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.android.vmapp.config.AppPreferences
import com.android.vmapp.ui.MainActivity
import com.android.vmapp.ui.base.BaseActivity
import com.clone.android.dual.space.R

class EulaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_eula)

        findViewById<TextView>(R.id.content)?.text =
            "Welcome to VM Alpha (Virtual Android Simulator).\n\n" +
            "By using this application, you agree to the Terms of Service and Privacy Policy. " +
            "All virtual machine data is stored locally inside the isolated container environment on your device."

        findViewById<Button>(R.id.agree)?.setOnClickListener {
            AppPreferences(this).isEulaAccepted = true
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.disagree)?.setOnClickListener {
            finishAffinity()
        }
    }
}
