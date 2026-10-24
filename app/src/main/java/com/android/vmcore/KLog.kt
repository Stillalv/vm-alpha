package com.android.vmcore

import android.util.Log

object KLog {
    private const val PREFIX = "klog_"

    @JvmStatic
    fun v(tag: String, msg: String) = log(2, tag, msg)

    @JvmStatic
    fun d(tag: String, msg: String) = log(3, tag, msg)

    @JvmStatic
    fun i(tag: String, msg: String) = log(4, tag, msg)

    @JvmStatic
    fun w(tag: String, msg: String) = log(5, tag, msg)

    @JvmStatic
    fun e(tag: String, msg: String) = log(6, tag, msg)

    @JvmStatic
    fun e(tag: String, msg: String, tr: Throwable?) {
        val fullMsg = if (tr != null) "$msg\n${Log.getStackTraceString(tr)}" else msg
        log(6, tag, fullMsg)
    }

    @JvmStatic
    fun flush() {
        try {
            nativeFlushKLog()
        } catch (e: Throwable) {
            // Ignore if native not loaded
        }
    }

    @JvmStatic
    fun log(priority: Int, tag: String, msg: String) {
        val fullTag = "$PREFIX$tag"
        val sb = StringBuilder(msg.length)
        for (c in msg) {
            if (c.code > 127 || c == '%') {
                sb.append('*')
            } else {
                sb.append(c)
            }
        }
        try {
            nativeKLog(priority, fullTag, sb.toString())
        } catch (e: Throwable) {
            when (priority) {
                2 -> Log.v(fullTag, sb.toString())
                3 -> Log.d(fullTag, sb.toString())
                4 -> Log.i(fullTag, sb.toString())
                5 -> Log.w(fullTag, sb.toString())
                6 -> Log.e(fullTag, sb.toString())
                else -> Log.d(fullTag, sb.toString())
            }
        }
    }

    @JvmStatic
    external fun nativeKLog(priority: Int, tag: String, msg: String)

    @JvmStatic
    external fun nativeFlushKLog()
}
