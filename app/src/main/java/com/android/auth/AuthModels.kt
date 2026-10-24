package com.android.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class AuthCredential(val provider: String) : Parcelable

@Parcelize
class EmailAuthCredential(val email: String, val password: String?) : AuthCredential("password")

@Parcelize
class GoogleAuthCredential(val idToken: String?, val accessToken: String?) : AuthCredential("google.com")

@Parcelize
class FacebookAuthCredential(val token: String) : AuthCredential("facebook.com")

@Parcelize
class TwitterAuthCredential(val token: String, val secret: String) : AuthCredential("twitter.com")

@Parcelize
class GithubAuthCredential(val token: String) : AuthCredential("github.com")

@Parcelize
class PhoneAuthCredential(val verificationId: String, val smsCode: String) : AuthCredential("phone")

@Parcelize
class OAuthCredential(val providerId: String, val idToken: String?, val accessToken: String?, val secret: String?) : AuthCredential(providerId)

interface UserInfo {
    val uid: String
    val providerId: String
    val displayName: String?
    val email: String?
    val phoneNumber: String?
    val photoUrl: String?
    val isEmailVerified: Boolean
}

class AppUserMetadata(
    val creationTimestamp: Long = 0,
    val lastSignInTimestamp: Long = 0
)

abstract class AppUser : UserInfo {
    abstract val isAnonymous: Boolean
    abstract val metadata: AppUserMetadata?
}

interface AdditionalUserInfo {
    val providerId: String
    val isNewUser: Boolean
    val profile: Map<String, Any?>?
    val username: String?
}

interface AuthResult {
    val user: AppUser?
    val additionalUserInfo: AdditionalUserInfo?
    val credential: AuthCredential?
}

class ActionCodeSettings private constructor(
    val url: String?,
    val iosBundleId: String?,
    val androidPackageName: String?,
    val androidMinimumVersion: String?,
    val androidInstallIfNotAvailable: Boolean,
    val handleCodeInApp: Boolean
) {
    class Builder {
        private var url: String? = null
        private var iosBundleId: String? = null
        private var androidPackageName: String? = null
        private var androidMinimumVersion: String? = null
        private var androidInstallIfNotAvailable = false
        private var handleCodeInApp = false

        fun setUrl(url: String) = apply { this.url = url }
        fun setIOSBundleId(id: String) = apply { this.iosBundleId = id }
        fun setAndroidPackageName(pkg: String, install: Boolean, minVer: String?) = apply {
            this.androidPackageName = pkg
            this.androidInstallIfNotAvailable = install
            this.androidMinimumVersion = minVer
        }
        fun setHandleCodeInApp(handle: Boolean) = apply { this.handleCodeInApp = handle }
        fun build() = ActionCodeSettings(url, iosBundleId, androidPackageName, androidMinimumVersion, androidInstallIfNotAvailable, handleCodeInApp)
    }
}
