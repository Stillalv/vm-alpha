package com.phantom.killer.disabler.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.clone.android.dual.space.R
import com.phantom.killer.disabler.DisablerLauncher
import com.phantom.killer.disabler.ui.shizuku.ShizukuActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tvStatus: TextView
    private lateinit var btnUnlock: View
    private lateinit var btnShizuku: View
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_gp_m3_main)

        tvStatus = findViewById(R.id.tv_gp_status_pill)
        btnUnlock = findViewById(R.id.btn_gp_primary_cta)
        btnShizuku = findViewById(R.id.btn_gp_fallback_shizuku)
        progressBar = findViewById(R.id.pb_gp_cta_loading)

        btnUnlock.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            DisablerLauncher.disablePhantomProcessKiller(this) { _, _ ->
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    refreshStatus()
                }
            }
        }

        btnShizuku.setOnClickListener {
            startActivity(Intent(this, ShizukuActivity::class.java))
        }

        refreshStatus()
    }

    private fun refreshStatus() {
        progressBar.visibility = View.VISIBLE
        DisablerLauncher.checkStatus(this) { status ->
            runOnUiThread {
                progressBar.visibility = View.GONE
                tvStatus.text = status.description
            }
        }
    }
}
