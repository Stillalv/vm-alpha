package com.android.vmapp.data

import android.graphics.drawable.Drawable

data class AppItemModel(
    val packageName: String,
    val appName: String,
    val icon: Drawable? = null,
    val isSystemApp: Boolean = false,
    val versionName: String = "",
    val versionCode: Long = 0L
)

data class BackupItemModel(
    val backupId: String,
    val vmId: Int,
    val vmName: String,
    val date: Long,
    val sizeBytes: Long,
    val filePath: String
)

data class CarrierModel(
    val mccMnc: String,
    val carrierName: String,
    val country: String
)
