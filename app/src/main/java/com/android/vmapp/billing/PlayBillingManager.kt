package com.android.vmapp.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*

/**
 * PlayBillingManager with Upgrade Plan Disabled ("Plan Mati").
 *
 * All VIP & Lifetime features (GPU spoofing, multi-VM, custom resolution, backup, ad-free)
 * are permanently unlocked without querying Google Play Billing or prompting for upgrades.
 */
class PlayBillingManager(val context: Context) : PurchasesUpdatedListener {

    companion object {
        const val PRODUCT_BASIC_SUBSCRIPTION = "basic_subscription"
        const val PRODUCT_LIFETIME_VIP = "lifetime_vip"

        @Volatile
        private var instance: PlayBillingManager? = null

        @JvmStatic
        fun getInstance(context: Context): PlayBillingManager {
            return instance ?: synchronized(this) {
                instance ?: PlayBillingManager(context.applicationContext).also { instance = it }
            }
        }
    }

    /**
     * Permanently true — no subscription or upgrade plan required.
     */
    fun isVip(): Boolean = true

    fun isLifetimeVip(): Boolean = true

    fun startConnection(callback: BillingCallback?) {
        // Bypass Play Store Billing connection; report setup finished with VIP unlocked
        callback?.onBillingSetupFinished(true)
    }

    fun queryAvailableProducts() {
        // Upgrade plans disabled; no SKU queries performed
    }

    fun launchBillingFlow(activity: Activity, productId: String, offerToken: String? = null): BillingResult {
        // Upgrade flow bypassed — return OK immediately
        return BillingResult.newBuilder()
            .setResponseCode(BillingClient.BillingResponseCode.OK)
            .setDebugMessage("VIP Permanently Unlocked")
            .build()
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: List<Purchase>?) {
        // No-op: upgrade plan disabled
    }

    fun getProductDetails(productId: String): ProductDetails? = null
}
