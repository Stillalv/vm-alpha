package com.android.vmcore.hal

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.android.vmcore.KLog
import com.android.vmcore.VMInstance

class LocationHalService(
    val context: Context,
    val vm: VMInstance,
    val halManager: HALManager
) : LocationListener {

    fun start() {
        KLog.d("LocationHalService", "LocationHalService started")
    }

    fun stop() {
        KLog.d("LocationHalService", "LocationHalService stopped")
    }

    override fun onLocationChanged(location: Location) {
        KLog.d("LocationHalService", "Location: ${location.latitude}, ${location.longitude}")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}
