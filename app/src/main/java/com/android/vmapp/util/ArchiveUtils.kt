package com.android.vmapp.util

import im.amomo.andun7z.AndUn7z
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.tukaani.xz.XZInputStream

object ArchiveUtils {
    fun extractTarXz(archiveFile: File, destDir: File) {
        destDir.mkdirs()
        TarArchiveInputStream(XZInputStream(FileInputStream(archiveFile))).use { tarIn ->
            var entry = tarIn.nextTarEntry
            while (entry != null) {
                val curFile = File(destDir, entry.name)
                if (entry.isDirectory) {
                    curFile.mkdirs()
                } else {
                    curFile.parentFile?.mkdirs()
                    FileOutputStream(curFile).use { out -> tarIn.copyTo(out) }
                }
                entry = tarIn.nextTarEntry
            }
        }
    }

    fun extract7z(archivePath: String, destPath: String) {
        AndUn7z.extract7z(archivePath, destPath)
    }
}
