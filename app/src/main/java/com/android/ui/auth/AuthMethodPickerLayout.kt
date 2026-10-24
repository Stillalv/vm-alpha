package com.android.ui.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.HashMap

@Parcelize
class AuthMethodPickerLayout(
    val mainLayoutRes: Int = 0,
    val buttonLayoutMap: HashMap<String, Int> = HashMap()
) : Parcelable
