package com.android.vmapp.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.vmapp.config.ThemeManager

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.applyTheme(this)
    }
}
