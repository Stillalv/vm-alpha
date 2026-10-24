package com.android.vmapp.ui.foryou

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.android.vmapp.ui.billing.BillingActivity
import com.clone.android.dual.space.R
import com.google.android.material.card.MaterialCardView

class AccountHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_account_header, this, true)
    }
}

class BillingCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_billing_card, this, true)
        findViewById<TextView>(R.id.subtitle)?.setText(R.string.billing_card_subtitle_active)
        findViewById<Button>(R.id.action)?.setOnClickListener {
            context.startActivity(Intent(context, BillingActivity::class.java))
        }
    }
}
