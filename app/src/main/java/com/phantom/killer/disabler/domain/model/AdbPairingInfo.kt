package com.phantom.killer.disabler.domain.model

class AdbPairingInfo @JvmOverloads constructor(
    host: String?,
    val pairingPort: Int,
    connectionPort: Int = 5555,
    val pairingCode: String?
) {
    val host: String = if (host.isNullOrEmpty()) "127.0.0.1" else host
    val connectionPort: Int = if (connectionPort <= 0) 5555 else connectionPort

    val port: Int
        get() = pairingPort

    val isValid: Boolean
        get() = pairingPort in 1..65535 && isValidPairingCode

    val isValidPairingCode: Boolean
        get() = pairingCode != null && pairingCode.trim().length == 6
}
