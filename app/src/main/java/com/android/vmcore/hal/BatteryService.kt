package com.android.vmcore.hal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.android.vmcore.KLog
import com.android.vmcore.VMInstance
import java.util.Random

/**
 * BatteryService bridges host battery telemetry to the guest VM and adds realistic
 * temperature (29.2°C - 35.6°C) and voltage (3850mV - 4250mV) fluctuations so that
 * anti-detection checks never see a static 50% / 0.0°C emulator battery.
 */
class BatteryService(
    val context: Context,
    val vm: VMInstance
) : BroadcastReceiver() {

    private val random = Random()
    var isRegistered: Boolean = false
    var currentLevel: Int = 72 + random.nextInt(22)
    var currentScale: Int = 100
    var currentStatus: Int = BatteryManager.BATTERY_STATUS_DISCHARGING
    var currentHealth: Int = BatteryManager.BATTERY_HEALTH_GOOD
    var currentTemperatureTenths: Int = 305 + random.nextInt(38) // 30.5°C - 34.2°C
    var currentVoltageMv: Int = 3920 + random.nextInt(260)      // 3920mV - 4180mV
    var technology: String = "Li-poly"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        val rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val rawScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
        val rawStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_DISCHARGING)
        val rawTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
        val rawVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)

        // Avoid static 50% emulator signature
        currentLevel = when {
            rawLevel in 1..100 && rawLevel != 50 -> rawLevel
            rawLevel == 50 -> 53
            else -> currentLevel
        }
        currentScale = if (rawScale > 0) rawScale else 100
        currentStatus = rawStatus

        // Fluctuate temperature slightly around host temperature or realistic 30.0°C - 35.5°C
        val jitterTemp = random.nextInt(7) - 3
        currentTemperatureTenths = if (rawTemp in 200..450) {
            (rawTemp + jitterTemp).coerceIn(265, 395)
        } else {
            (currentTemperatureTenths + jitterTemp).coerceIn(292, 356)
        }

        // Fluctuate voltage slightly around 3850mV - 4250mV
        val jitterVolt = random.nextInt(15) - 7
        currentVoltageMv = if (rawVoltage in 3500..4450) {
            (rawVoltage + jitterVolt).coerceIn(3650, 4350)
        } else {
            (currentVoltageMv + jitterVolt).coerceIn(3850, 4250)
        }

        KLog.d(
            "BatteryService",
            "Battery updated: level=$currentLevel/$currentScale, temp=${currentTemperatureTenths / 10.0}C, volt=${currentVoltageMv}mV"
        )
    }
}
