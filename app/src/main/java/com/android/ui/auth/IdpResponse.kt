package com.android.ui.auth

import android.content.Intent
import android.os.Parcelable
import com.android.auth.AuthCredential
import kotlinx.parcelize.Parcelize

@Parcelize
class IdpResponse(
    val email: String? = null,
    val token: String? = null,
    val secret: String? = null,
    val isNewUser: Boolean = false,
    val error: AppAuthUiException? = null,
    val credential: AuthCredential? = null
) : Parcelable {
    val isSuccessful: Boolean get() = error == null

    companion object {
        @JvmStatic
        fun fromResultIntent(intent: Intent?): IdpResponse? {
            return intent?.getParcelableExtra("extra_idp_response")
        }
    }
}
