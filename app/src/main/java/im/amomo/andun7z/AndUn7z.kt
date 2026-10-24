package im.amomo.andun7z

import android.text.TextUtils
import android.util.Log
import android.util.Pair
import java.io.File
import java.io.FileFilter
import java.util.ArrayList

object AndUn7z {
    private const val TAG = "AndUn7z"

    val isLibraryLoaded: Boolean

    init {
        var loaded = false
        try {
            System.loadLibrary("un7z")
            loaded = true
        } catch (e: UnsatisfiedLinkError) {
            Log.d(TAG, "System.loadLibrary failed", e)
        }
        isLibraryLoaded = loaded
    }

    @JvmStatic
    @Throws(Exception::class)
    fun extract7z(
        filePath: String?,
        outDir: String?,
        password: String? = null,
        filter: FileFilter? = null,
        outList: ArrayList<Pair<String, String>> = ArrayList()
    ) {
        if (!isLibraryLoaded) {
            throw Exception("un7z lib can NOT loaded")
        }
        if (TextUtils.isEmpty(filePath)) {
            throw IllegalArgumentException("FilePath can NOT be null")
        }
        val targetDir = if (TextUtils.isEmpty(outDir)) {
            File(filePath!!).parentFile ?: File(".")
        } else {
            File(outDir!!)
        }
        if (!targetDir.exists() || !targetDir.isDirectory) {
            targetDir.mkdirs()
        }

        val result = un7zip(filePath!!, targetDir.absolutePath, password, filter, outList)
        if (result == -1) {
            throw Exception("un7z lib can NOT loaded")
        }
        if (result == 255) {
            throw Exception("setup jni failed")
        }
        if (result != 0) {
            val lastPair = if (outList.isNotEmpty()) outList[outList.size - 1] else null
            if (lastPair != null && !TextUtils.isEmpty(lastPair.second)) {
                throw Exception(lastPair.second)
            }
            val errorMsg = when (result) {
                0 -> "SZ_OK"
                1 -> "SZ_ERROR_DATA"
                2 -> "SZ_ERROR_MEM"
                3 -> "SZ_ERROR_CRC"
                4 -> "SZ_ERROR_UNSUPPORTED"
                5 -> "SZ_ERROR_PARAM"
                6 -> "SZ_ERROR_INPUT_EOF"
                7 -> "SZ_ERROR_OUTPUT_EOF"
                8 -> "SZ_ERROR_READ"
                9 -> "SZ_ERROR_WRITE"
                10 -> "SZ_ERROR_PROGRESS"
                11 -> "SZ_ERROR_FAIL"
                12 -> "SZ_ERROR_THREAD"
                16 -> "SZ_ERROR_ARCHIVE"
                17 -> "SZ_ERROR_NO_ARCHIVE"
                18 -> "SZ_ERROR_READ_ASSET"
                100 -> "SZ_ERROR_OPEN_FAILED"
                else -> "SZ_ERROR_UNKNOWN ($result)"
            }
            throw Exception("extract7z failed with $errorMsg")
        }
    }

    @JvmStatic
    external fun un7zip(
        filePath: String,
        outPath: String,
        password: String?,
        filter: FileFilter?,
        outList: ArrayList<Pair<String, String>>
    ): Int
}
