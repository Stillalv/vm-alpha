package com.android.vmcore.vpn

import java.util.Objects

class VpnConfig {
    @JvmField var mtu: Int = 0
    @JvmField var addresses: MutableList<String> = ArrayList()
    @JvmField var routes: MutableList<String> = ArrayList()

    var enabled: Boolean = false
    var server: String = ""
    var port: Int = 1080
    var user: String = ""
    var pass: String = ""
    var dns4: String = "8.8.8.8"
    var dns6: String = ""

    override fun equals(other: Any?): Boolean {
        if (other != null && javaClass == other.javaClass) {
            val that = other as VpnConfig
            return mtu == that.mtu &&
                Objects.equals(addresses, that.addresses) &&
                Objects.equals(routes, that.routes)
        }
        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(mtu, addresses, routes)
    }
}
