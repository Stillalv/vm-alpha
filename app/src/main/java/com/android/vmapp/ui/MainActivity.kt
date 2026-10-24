package com.android.vmapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.android.vmapp.config.AppPreferences
import com.android.vmapp.ui.base.BaseActivity
import com.android.vmapp.ui.eula.EulaActivity
import com.clone.android.dual.space.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            installSplashScreen()
        } catch (_: Throwable) {
        }
        super.onCreate(savedInstanceState)

        val prefs = AppPreferences(this)
        if (!prefs.isEulaAccepted) {
            startActivity(Intent(this, EulaActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.nav_bar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as? NavHostFragment
        val navController = navHostFragment?.navController
        if (bottomNav != null && navController != null) {
            NavigationUI.setupWithNavController(bottomNav, navController)
        }
    }
}
