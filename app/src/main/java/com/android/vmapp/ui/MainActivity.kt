package com.android.vmapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.vmapp.config.AppPreferences
import com.android.vmapp.ui.base.BaseActivity
import com.android.vmapp.ui.eula.EulaActivity
import com.android.vmapp.ui.foryou.YouFragment
import com.android.vmapp.ui.vm.main.VMFragment
import com.clone.android.dual.space.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = AppPreferences(this)
        if (!prefs.isEulaAccepted) {
            startActivity(Intent(this, EulaActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.nav_bar)
        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.action_vm -> VMFragment()
                R.id.action_you -> YouFragment()
                else -> VMFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_container, fragment)
                .commit()
            true
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_container, VMFragment())
                .commit()
        }
    }
}
