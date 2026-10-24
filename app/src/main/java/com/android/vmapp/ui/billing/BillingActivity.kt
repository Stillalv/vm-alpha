package com.android.vmapp.ui.billing

import android.os.Bundle
import com.android.vmapp.ui.base.BaseActivity

/**
 * BillingActivity neutralized — Upgrade plan is disabled ("Plan Mati") and all VIP features are active.
 * Finishes immediately if invoked.
 */
class BillingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Upgrade plan disabled; close immediately
        finish()
    }
}
