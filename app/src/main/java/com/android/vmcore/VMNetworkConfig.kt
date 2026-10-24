package com.android.vmcore

import java.util.Objects

class VMNetworkConfig(
    @JvmField var ifname: String? = null,
    @JvmField var mac: String? = null,
    @JvmField var ip: String? = null,
    @JvmField var network: String? = null,
    @JvmField var netmask: String? = null,
    @JvmField var gateway_ip: String? = null,
    @JvmField var ipv6_prefix: String? = null,
    @JvmField var dns4_ip: String? = null,
    @JvmField var dns6_ip: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as VMNetworkConfig
        return Objects.equals(ifname, that.ifname) &&
            Objects.equals(mac, that.mac) &&
            Objects.equals(ip, that.ip) &&
            Objects.equals(network, that.network) &&
            Objects.equals(netmask, that.netmask) &&
            Objects.equals(gateway_ip, that.gateway_ip) &&
            Objects.equals(ipv6_prefix, that.ipv6_prefix) &&
            Objects.equals(dns4_ip, that.dns4_ip) &&
            Objects.equals(dns6_ip, that.dns6_ip)
    }

    override fun hashCode(): Int {
        return Objects.hash(ifname, mac, ip, network, netmask, gateway_ip, ipv6_prefix, dns4_ip, dns6_ip)
    }

    override fun toString(): String {
        return "VMNetworkConfig{ifname='$ifname', mac='$mac', ip='$ip', network='$network', netmask='$netmask', gateway_ip='$gateway_ip', ipv6_prefix='$ipv6_prefix', dns4_ip='$dns4_ip', dns6_ip='$dns6_ip'}"
    }
}
