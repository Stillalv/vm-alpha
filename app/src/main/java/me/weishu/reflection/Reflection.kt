package me.weishu.reflection

import android.content.Context
import android.os.Build
import java.lang.reflect.Method

/**
 * Clean-room implementation of FreeReflection hidden API exemption bypass for Android P+ (API 28+).
 */
object Reflection {
    private const val ERROR_SET_APPLICATION_FAILED = -20
    private var unsealed = false

    @JvmStatic
    fun unseal(context: Context?): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return 0
        }
        if (unsealed) {
            return 0
        }
        return try {
            exemptAll()
            unsealed = true
            0
        } catch (t: Throwable) {
            ERROR_SET_APPLICATION_FAILED
        }
    }

    @JvmStatic
    fun exemptAll(): Boolean {
        return exempt("L")
    }

    @JvmStatic
    fun exempt(vararg methods: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return true
        }
        return try {
            val forName = Class::class.java.getDeclaredMethod("forName", String::class.java)
            val getDeclaredMethod = Class::class.java.getDeclaredMethod(
                "getDeclaredMethod",
                String::class.java,
                arrayOf<Class<*>>()::class.java
            )

            val vmRuntimeClass = forName.invoke(null, "dalvik.system.VMRuntime") as Class<*>
            val getRuntime = getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null) as Method
            val setHiddenApiExemptions = getDeclaredMethod.invoke(
                vmRuntimeClass,
                "setHiddenApiExemptions",
                arrayOf<Class<*>>(Array<String>::class.java)
            ) as Method

            val vmRuntime = getRuntime.invoke(null)
            setHiddenApiExemptions.invoke(vmRuntime, arrayOf<Any>(methods))
            true
        } catch (t: Throwable) {
            false
        }
    }
}
