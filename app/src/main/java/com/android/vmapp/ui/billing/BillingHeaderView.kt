package com.android.vmapp.ui.billing

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.clone.android.dual.space.R
import com.google.android.material.card.MaterialCardView

class BillingHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_billing_list_header, this, true)
    }
}
