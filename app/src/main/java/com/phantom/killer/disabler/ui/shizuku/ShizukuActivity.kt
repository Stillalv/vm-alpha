package com.phantom.killer.disabler.ui.shizuku

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.clone.android.dual.space.R
import com.phantom.killer.disabler.internal.ShizukuCommandRunner

class ShizukuActivity : AppCompatActivity() {
    private lateinit var tvShizukuStatus: TextView
    private lateinit var btnRequestPermission: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shizuku)

        tvShizukuStatus = findViewById(R.id.tv_shizuku_status)
        btnRequestPermission = findViewById(R.id.btn_shizuku_action)
        progressBar = findViewById(R.id.progress_bar)

        updateStatus()
    }

    private fun updateStatus() {
        val available = ShizukuCommandRunner.isShizukuAvailable()
        val hasPerm = ShizukuCommandRunner.hasShizukuPermission()
        tvShizukuStatus.text = when {
            !available -> getString(R.string.shizuku_not_running)
            !hasPerm -> getString(R.string.shizuku_permission_not_granted)
            else -> getString(R.string.shizuku_authorized)
        }
    }
}
