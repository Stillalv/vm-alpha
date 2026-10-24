package com.android.vmapp.billing

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AccountInfo(
    @SerializedName("account_id") val accountId: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("is_vip") val isVip: Boolean = false,
    @SerializedName("expire_time") val expireTime: Long = 0L
)

@Keep
data class BindPurchaseRequest(
    @SerializedName("order_id") val orderId: String,
    @SerializedName("purchase_token") val purchaseToken: String,
    @SerializedName("product_id") val productId: String
)

@Keep
data class ErrorResponse(
    @SerializedName("code") val code: Int = 0,
    @SerializedName("message") val message: String = ""
)

@Keep
data class MemberStatusResponse(
    @SerializedName("member_status") val memberStatus: String = "",
    @SerializedName("membership_expires_at") val expiresAt: Long = 0L
)

interface BillingCallback {
    fun onBillingSetupFinished(success: Boolean)
    fun onPurchaseCompleted(success: Boolean, message: String)
}
