package com.android.vmcore.vpn

data class VpnConfig(
    var enabled: Boolean = false,
    var server: String = "",
    var port: Int = 1080,
    var user: String = "",
    var pass: String = "",
    var dns4: String = "8.8.8.8",
    var dns6: String = ""
)
