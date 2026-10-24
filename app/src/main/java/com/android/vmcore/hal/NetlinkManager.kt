package com.android.vmcore.hal

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.wifi.ScanResult
import android.os.Handler
import android.os.Looper
import com.android.vmcore.KLog
import com.android.vmcore.VMInstance
import com.android.vmcore.VMNetworkConfig
import java.io.File

class NetlinkManager @JvmOverloads constructor(
    private val mContext: Context? = null,
    private val mVM: VMInstance? = null
) : ConnectivityManager.NetworkCallback() {

    private val mCm: ConnectivityManager? = mContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    private var mNativePtr: Long = 0L
    private var mNetworkConfig: VMNetworkConfig? = null
    private var mStarted: Boolean = false
    private var mWifiConnected: Boolean = false
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    init {
        if (mVM != null) {
            mNativePtr = try {
                nativeSetup(mVM.vmId)
            } catch (t: Throwable) {
                KLog.e(TAG, "nativeSetup failed", t)
                0L
            }
        }
    }

    private external fun nativeDispose(nativePtr: Long)
    private external fun nativeOnWifiConnected(nativePtr: Long)
    private external fun nativeOnWifiDisconnected(nativePtr: Long)
    private external fun nativeOnWifiScanResultsChanged(nativePtr: Long, list: List<ScanResult>)
    private external fun nativeSetNetworkConfig(nativePtr: Long, config: VMNetworkConfig)
    private external fun nativeSetup(vmId: Int): Long
    private external fun nativeStartMgr(nativePtr: Long): Int
    private external fun nativeStopMgr(nativePtr: Long): Int

    fun connectWifi(ssid: String?) {
        mHandler.post {
            if (mNativePtr != 0L) {
                try {
                    nativeOnWifiConnected(mNativePtr)
                } catch (_: Throwable) {
                }
            }
            mWifiConnected = true
        }
    }

    fun disconnectWifi() {
        mHandler.post {
            mWifiConnected = false
            if (mNativePtr != 0L) {
                try {
                    nativeOnWifiDisconnected(mNativePtr)
                } catch (_: Throwable) {
                }
            }
        }
    }

    fun startScanWifi() {
        mHandler.post {
            if (mNativePtr != 0L) {
                try {
                    nativeOnWifiScanResultsChanged(mNativePtr, emptyList())
                } catch (_: Throwable) {
                }
            }
        }
    }

    fun stopScanWifi() {
        mHandler.post {
            if (mNativePtr != 0L) {
                try {
                    nativeOnWifiScanResultsChanged(mNativePtr, emptyList())
                } catch (_: Throwable) {
                }
            }
        }
    }

    fun onVMNetworkConfigChanged() {
        if (mStarted && mVM != null && mNativePtr != 0L) {
            val cfg = buildNetworkConfig(mVM)
            mNetworkConfig = cfg
            try {
                nativeSetNetworkConfig(mNativePtr, cfg)
            } catch (_: Throwable) {
            }
        }
    }

    private fun buildNetworkConfig(vm: VMInstance): VMNetworkConfig {
        val c = vm.config
        return VMNetworkConfig(
            ifname = "wlan0",
            mac = c.localMac,
            ip = c.localIp,
            network = c.network,
            netmask = c.netmask,
            gateway_ip = c.gatewayIp,
            ipv6_prefix = "",
            dns4_ip = c.dns4Ip,
            dns6_ip = ""
        )
    }

    fun start(): Int {
        mStarted = true
        mWifiConnected = false
        if (mVM != null) {
            val sockFile = File(mVM.rootFsDir, "/dev/netlink_client")
            sockFile.parentFile?.mkdirs()
            if (sockFile.exists()) sockFile.delete()
            onVMNetworkConfigChanged()
        }
        if (mNativePtr == 0L) return 0
        return try {
            nativeStartMgr(mNativePtr)
        } catch (_: Throwable) {
            0
        }
    }

    fun stop(): Int {
        mStarted = false
        mWifiConnected = false
        if (mVM != null) {
            val sockFile = File(mVM.rootFsDir, "/dev/netlink_client")
            if (sockFile.exists()) sockFile.delete()
        }
        if (mNativePtr == 0L) return 0
        return try {
            nativeStopMgr(mNativePtr)
        } catch (_: Throwable) {
            0
        }
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties)
    }

    protected fun finalize() {
        val ptr = mNativePtr
        if (ptr != 0L) {
            try {
                nativeDispose(ptr)
            } catch (_: Throwable) {
            }
            mNativePtr = 0L
        }
    }

    companion object {
        private const val TAG = "NetlinkManager"
    }
}
