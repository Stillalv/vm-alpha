package com.phantom.killer.disabler.domain.model

enum class UnlockStatus(val description: String, val isUnlocked: Boolean) {
    LOCKED("32 (Restricted)", false),
    UNLOCKED("2,147,483,647 (Unlimited)", true),
    A14_NATIVE_DISABLED("Native Disabled (Unlimited)", true),
    UNKNOWN("Unknown", false)
}
