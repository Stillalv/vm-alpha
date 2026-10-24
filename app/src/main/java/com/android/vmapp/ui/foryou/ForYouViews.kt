package com.android.vmapp.ui.foryou

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
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
        LayoutInflater.from(context).inflate(R.layout.view_billing_list_header, this, true)
    }
}
