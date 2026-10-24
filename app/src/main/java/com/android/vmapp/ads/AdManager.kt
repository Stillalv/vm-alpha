package com.android.vmapp.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardedAd

/**
 * AdManager with ads & upgrade prompts disabled (VIP permanently active).
 */
object AdManager {
    fun init(context: Context) {
        // Ads disabled
    }

    fun loadInterstitial(activity: Activity, adUnitId: String, callback: (InterstitialAd?) -> Unit) {
        callback(null)
    }

    fun loadRewarded(activity: Activity, adUnitId: String, callback: (RewardedAd?) -> Unit) {
        callback(null)
    }
}
