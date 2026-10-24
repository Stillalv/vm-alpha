package com.android.vmcore.hal.phone

data class AdnRecord(
    var alphaTag: String = "",
    var number: String = "",
    var emails: Array<String>? = null,
    var recordNumber: Int = 0
)
