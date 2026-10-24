package com.android.vmcore

import java.util.Objects

data class VMNetworkConfig(
    var ifname: String? = null,
    var mac: String? = null,
    var ip: String? = null,
    var network: String? = null,
    var netmask: String? = null,
    var gateway_ip: String? = null,
    var ipv6_prefix: String? = null,
    var dns4_ip: String? = null,
    var dns6_ip: String? = null
)
