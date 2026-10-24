package com.android.vmcore

class VMProcessInfo(
    @JvmField var pid: Int = 0,
    @JvmField var ppid: Int = 0,
    @JvmField var name: String = "",
    @JvmField var state: String = "",
    @JvmField var exe: String = "",
    @JvmField var cwd: String = ""
)
