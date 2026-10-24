package com.phantom.killer.disabler.domain.model

import androidx.annotation.StringRes
import com.clone.android.dual.space.R

enum class AdbErrorCode(@StringRes val defaultMessageResId: Int) {
    NONE(0),
    INVALID_PAIRING_CODE(R.string.error_invalid_pairing_code),
    MDNS_PORT_NOT_FOUND(R.string.error_mdns_port_detect_failed),
    PAIRING_FAILED(R.string.error_pairing_handshake_failed),
    PAIRING_CODE_EXPIRED(R.string.error_pairing_handshake_failed),
    CONNECT_TIMEOUT(R.string.error_autoconnect_failed_guide),
    MDNS_DISCOVERY_TIMEOUT(R.string.error_mdns_timeout_guide),
    AUTH_UNAUTHORIZED_NEED_REPAIR(R.string.dialog_auth_failed_msg),
    COMMAND_FAILED(R.string.error_unlock_execution_failed),
    DEVICE_NOT_CONNECTED(R.string.error_adb_not_connected),
    UNKNOWN_ERROR(R.string.error_unknown)
}
