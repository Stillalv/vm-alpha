package com.android.vmapp.ui.base.preference

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.SwitchPreferenceCompat

abstract class BasePreferenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

class EditTextPreferenceEX @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.editTextPreferenceStyle,
    defStyleRes: Int = 0
) : EditTextPreference(context, attrs, defStyleAttr, defStyleRes)

class ListPreferenceEx @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.dialogPreferenceStyle,
    defStyleRes: Int = 0
) : ListPreference(context, attrs, defStyleAttr, defStyleRes)

class SwitchPreferenceCompatEx @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.switchPreferenceCompatStyle,
    defStyleRes: Int = 0
) : SwitchPreferenceCompat(context, attrs, defStyleAttr, defStyleRes)
