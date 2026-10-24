package com.android.vmcore.task

import android.content.Context
import com.android.vmcore.KLog
import com.android.vmcore.VMInstance
import im.amomo.andun7z.AndUn7z
import java.io.File

interface VMTask {
    val name: String
    fun execute(context: Context, vm: VMInstance): Boolean
}

class BuildExecPathTask : VMTask {
    override val name = "BuildExecPathTask"
    override fun execute(context: Context, vm: VMInstance): Boolean {
        File(vm.rootDir, "bin").mkdirs()
        File(vm.rootDir, "tmp").mkdirs()
        return true
    }
}

class PluginInstallerTask(val pluginZipName: String, val destDirName: String) : VMTask {
    override val name = "PluginInstallerTask($pluginZipName)"
    override fun execute(context: Context, vm: VMInstance): Boolean {
        try {
            val assetFile = File(context.cacheDir, pluginZipName)
            context.assets.open("plugins/$pluginZipName").use { input ->
                assetFile.outputStream().use { output -> input.copyTo(output) }
            }
            val targetDir = File(vm.rootFsDir, destDirName)
            targetDir.mkdirs()
            AndUn7z.extract7z(assetFile.absolutePath, targetDir.absolutePath)
            assetFile.delete()
            return true
        } catch (e: Throwable) {
            KLog.e("PluginInstallerTask", "Failed to extract $pluginZipName", e)
            return false
        }
    }
}
