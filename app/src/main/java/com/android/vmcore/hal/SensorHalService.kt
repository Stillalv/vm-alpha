package com.android.vmcore.hal

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.android.vmcore.KLog
import com.android.vmcore.VMInstance
import java.util.Random

/**
 * SensorHalService bridges host physical sensors to the guest VM and injects
 * realistic floating-point micro-noise (jitter) so that accelerometer, gyroscope,
 * magnetometer, and ambient light readings never appear static/emulated.
 */
class SensorHalService(
    val context: Context,
    val vm: VMInstance,
    val halManager: HALManager
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val random = Random()

    fun start() {
        KLog.d("SensorHalService", "SensorHalService started with micro-noise generator")
        listOf(
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_MAGNETIC_FIELD,
            Sensor.TYPE_LIGHT,
            Sensor.TYPE_PROXIMITY,
            Sensor.TYPE_PRESSURE
        ).forEach { type ->
            sensorManager.getDefaultSensor(type)?.let { sensor ->
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
            }
        }
    }

    fun stop() {
        KLog.d("SensorHalService", "SensorHalService stopped")
        sensorManager.unregisterListener(this)
    }

    /**
     * Generates realistic accelerometer XYZ readings with micro-noise around gravity (9.80665 m/s^2)
     */
    fun getSimulatedAccelerometer(baseX: Float = 0.015f, baseY: Float = 0.12f, baseZ: Float = 9.80665f): FloatArray {
        return floatArrayOf(
            baseX + nextMicroNoise(0.0035f),
            baseY + nextMicroNoise(0.0042f),
            baseZ + nextMicroNoise(0.0048f)
        )
    }

    /**
     * Generates realistic gyroscope XYZ angular velocity with subtle thermal jitter (rad/s)
     */
    fun getSimulatedGyroscope(baseX: Float = 0.0f, baseY: Float = 0.0f, baseZ: Float = 0.0f): FloatArray {
        return floatArrayOf(
            baseX + nextMicroNoise(0.00085f),
            baseY + nextMicroNoise(0.00085f),
            baseZ + nextMicroNoise(0.00085f)
        )
    }

    /**
     * Generates realistic magnetometer readings (micro-Tesla)
     */
    fun getSimulatedMagneticField(baseX: Float = 22.4f, baseY: Float = -5.3f, baseZ: Float = -41.6f): FloatArray {
        return floatArrayOf(
            baseX + nextMicroNoise(0.18f),
            baseY + nextMicroNoise(0.18f),
            baseZ + nextMicroNoise(0.22f)
        )
    }

    /**
     * Generates realistic ambient light sensor reading (lux)
     */
    fun getSimulatedLight(baseLux: Float = 245.0f): Float {
        return (baseLux + nextMicroNoise(3.5f)).coerceAtLeast(5.0f)
    }

    private fun nextMicroNoise(amplitude: Float): Float {
        return (random.nextGaussian() * amplitude).toFloat()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        // Apply micro-noise jitter so even stationary phone sensors exhibit natural variance
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                if (event.values.size >= 3) {
                    event.values[0] += nextMicroNoise(0.002f)
                    event.values[1] += nextMicroNoise(0.002f)
                    event.values[2] += nextMicroNoise(0.002f)
                }
            }
            Sensor.TYPE_GYROSCOPE -> {
                if (event.values.size >= 3) {
                    event.values[0] += nextMicroNoise(0.0005f)
                    event.values[1] += nextMicroNoise(0.0005f)
                    event.values[2] += nextMicroNoise(0.0005f)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
