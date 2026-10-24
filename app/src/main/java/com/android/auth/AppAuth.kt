package com.android.auth

import android.content.Context
import java.util.concurrent.CopyOnWriteArrayList

class AppAuth private constructor(val context: Context) {
    var currentUser: AppUser? = null
        private set

    fun interface AuthStateListener {
        fun onAuthStateChanged(auth: AppAuth)
    }

    private val listeners = CopyOnWriteArrayList<AuthStateListener>()

    fun addAuthStateListener(listener: AuthStateListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
            listener.onAuthStateChanged(this)
        }
    }

    fun removeAuthStateListener(listener: AuthStateListener) {
        listeners.remove(listener)
    }

    fun signOut() {
        currentUser = null
        for (l in listeners) l.onAuthStateChanged(this)
    }

    companion object {
        @Volatile
        private var instance: AppAuth? = null

        @JvmStatic
        fun getInstance(): AppAuth {
            return instance ?: synchronized(this) {
                instance ?: throw IllegalStateException("AppAuth not initialized")
            }
        }

        @JvmStatic
        fun init(context: Context): AppAuth {
            return instance ?: synchronized(this) {
                instance ?: AppAuth(context.applicationContext).also { instance = it }
            }
        }
    }
}
