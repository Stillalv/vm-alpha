package com.android.vmcore.service

import android.os.IInterface

interface IBinderService : IInterface {
    companion object {
        const val DESCRIPTOR = "com.android.vmcore.service.IBinderService"
    }
}
