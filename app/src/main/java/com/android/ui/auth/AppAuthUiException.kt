package com.android.ui.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class AppAuthUiException(val errorCode: Int, override val message: String? = null) : Exception(message), Parcelable
class AppAuthAnonymousUpgradeException(code: Int, val response: IdpResponse) : AppAuthUiException(code)
class AppAuthUiUserCollisionException(code: Int, msg: String, val email: String) : AppAuthUiException(code, msg)
