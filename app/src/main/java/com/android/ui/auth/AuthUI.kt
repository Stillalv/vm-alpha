package com.android.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class AuthUI private constructor() {
    @Parcelize
    class IdpConfig(val providerId: String, val params: Bundle = Bundle()) : Parcelable {
        class Builder(private val providerId: String) {
            private val params = Bundle()
            fun build() = IdpConfig(providerId, params)
        }
    }

    class SignInIntentBuilder {
        private val providers = mutableListOf<IdpConfig>()
        private var theme: Int = 0
        private var logo: Int = 0
        private var tosUrl: String? = null
        private var privacyPolicyUrl: String? = null

        fun setAvailableProviders(providers: List<IdpConfig>) = apply {
            this.providers.clear()
            this.providers.addAll(providers)
        }

        fun setTheme(theme: Int) = apply { this.theme = theme }
        fun setLogo(logo: Int) = apply { this.logo = logo }
        fun setTosAndPrivacyPolicyUrls(tosUrl: String, privacyUrl: String) = apply {
            this.tosUrl = tosUrl
            this.privacyPolicyUrl = privacyUrl
        }

        fun build(context: Context): Intent {
            return Intent(context, com.android.ui.auth.KickoffActivity::class.java).apply {
                putParcelableArrayListExtra("extra_providers", ArrayList(providers))
                putExtra("extra_theme", theme)
            }
        }
    }

    fun createSignInIntentBuilder(): SignInIntentBuilder = SignInIntentBuilder()

    companion object {
        @JvmStatic
        fun getInstance(): AuthUI = AuthUI()
    }
}
