package com.android.vmapp.config

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsHelper {
    fun logEvent(context: Context, name: String, params: Bundle? = null) {
        try {
            FirebaseAnalytics.getInstance(context).logEvent(name, params)
        } catch (e: Throwable) {
            // Ignore
        }
    }
}
