package com.android.vmcore.hal.phone

object IccUtils {
    @JvmStatic
    fun bcdToString(data: ByteArray, offset: Int, length: Int): String {
        val sb = java.lang.StringBuilder(length * 2)
        for (i in offset until offset + length) {
            val b = data[i].toInt()
            val lo = b and 0x0f
            if (lo > 9) break
            sb.append(lo)
            val hi = (b shr 4) and 0x0f
            if (hi > 9) break
            sb.append(hi)
        }
        return sb.toString()
    }

    @JvmStatic
    fun stringToBcd(s: String): ByteArray {
        val out = ByteArray((s.length + 1) / 2)
        var i = 0
        var j = 0
        while (i < s.length) {
            val c0 = s[i] - '0'
            val c1 = if (i + 1 < s.length) s[i + 1] - '0' else 0x0f
            out[j++] = ((c1 shl 4) or c0).toByte()
            i += 2
        }
        return out
    }
}
