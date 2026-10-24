package com.phantom.killer.disabler.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.clone.android.dual.space.R
import com.phantom.killer.disabler.DisablerLauncher
import com.phantom.killer.disabler.domain.model.UnlockStatus
import com.phantom.killer.disabler.ui.shizuku.ShizukuActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tvStatus: TextView
    private lateinit var btnUnlock: Button
    private lateinit var btnShizuku: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phantom_killer_disabler)

        tvStatus = findViewById(R.id.tv_status)
        btnUnlock = findViewById(R.id.btn_disable)
        btnShizuku = findViewById(R.id.btn_shizuku)
        progressBar = findViewById(R.id.progress_bar)

        btnUnlock.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            DisablerLauncher.disablePhantomProcessKiller(this) { success, msg ->
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
