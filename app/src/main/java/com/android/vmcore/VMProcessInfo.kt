package com.android.vmcore

data class VMProcessInfo(
    var pid: Int = 0,
    var ppid: Int = 0,
    var name: String = "",
    var state: String = "",
    var exe: String = "",
    var cwd: String = ""
)
