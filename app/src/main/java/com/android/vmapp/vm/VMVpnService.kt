package com.android.vmapp.vm

import android.content.Intent
import android.net.ConnectivityManager
import android.net.VpnService
import android.os.IBinder
import com.android.vmcore.vpn.VpnTunService

class VMVpnService : VpnService() {
    override fun onBind(intent: Intent?): IBinder? = super.onBind(intent)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val builder = Builder()
        builder.addAddress("10.0.2.15", 24)
        builder.addRoute("0.0.0.0", 0)
        builder.addDnsServer("8.8.8.8")
        val pfd = builder.establish()
        pfd?.let {
            VpnTunService.setupTun(it.fd, "10.0.2.15", 1080)
        }
        return START_STICKY
    }
}
