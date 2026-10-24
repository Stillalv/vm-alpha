package com.phantom.killer.disabler.internal

import android.content.pm.PackageManager
import com.phantom.killer.disabler.domain.model.ExecutionResult
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuRemoteProcess
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuCommandRunner {
    fun isShizukuAvailable(): Boolean {
        return try {
            Shizuku.pingBinder()
        } catch (e: Throwable) {
            false
        }
    }

    fun hasShizukuPermission(): Boolean {
        return try {
            if (!isShizukuAvailable()) false
            else Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } catch (e: Throwable) {
            false
        }
    }

    fun execCommand(cmd: String): ExecutionResult {
        return try {
            val newProcessMethod = Shizuku::class.java.getDeclaredMethod(
                "newProcess",
                Array<String>::class.java,
                Array<String>::class.java,
                String::class.java
            ).apply { isAccessible = true }
            val process = newProcessMethod.invoke(null, arrayOf("sh", "-c", cmd), null, null) as ShizukuRemoteProcess
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val sb = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append('\n')
            }
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                ExecutionResult.success(sb.toString().trim())
            } else {
                ExecutionResult.failure("Process exited with code $exitCode: ${sb.toString().trim()}")
            }
        } catch (e: Throwable) {
            ExecutionResult.failure(e.message ?: "Execution failed")
        }
    }
}
