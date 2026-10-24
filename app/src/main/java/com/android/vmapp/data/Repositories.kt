package com.android.vmapp.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.android.vmcore.VMInstance
import com.android.vmcore.VMManager
import java.io.File

class AppsRepository(val context: Context) {
    fun getInstalledApps(): List<AppItemModel> {
        val pm = context.packageManager
        val pkgs = pm.getInstalledPackages(0)
        return pkgs.map { pi ->
            val ai = pi.applicationInfo ?: ApplicationInfo()
            val isSys = (ai.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            AppItemModel(
                packageName = pi.packageName,
                appName = pm.getApplicationLabel(ai).toString(),
                icon = pm.getApplicationIcon(ai),
                isSystemApp = isSys,
                versionName = pi.versionName ?: "",
                versionCode = pi.longVersionCode
            )
        }
    }
}

class VMRepository(val context: Context) {
    private val vmManager = VMManager.getInstance(context)

    fun getVMs(): List<VMInstance> = vmManager.getAllVMs()
    fun getVM(id: Int): VMInstance? = vmManager.getVM(id)
}

class BackupRepository(val context: Context) {
    private val backupDir = File(context.getExternalFilesDir(null), "backups")

    fun getBackups(): List<BackupItemModel> {
        backupDir.mkdirs()
        val files = backupDir.listFiles { _, name -> name.endsWith(".tar.xz") || name.endsWith(".7z") }
        return files?.map { f ->
            BackupItemModel(
                backupId = f.nameWithoutExtension,
                vmId = 0,
                vmName = f.nameWithoutExtension,
                date = f.lastModified(),
                sizeBytes = f.length(),
                filePath = f.absolutePath
            )
        } ?: emptyList()
    }
}
