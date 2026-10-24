package com.android.vmapp.ui.vm.create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.android.vmapp.ui.vm.rom.RomFragment
import com.clone.android.dual.space.R

class VMNamingFragment : Fragment() {
    companion object {
        var enteredName: String = "Android 7.1.2 VM"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vm_naming, container, false)
        val inputBox = view.findViewById<AppCompatEditText>(R.id.input_box)
        val defaultName = if (RomFragment.selectedRomSdk == 22) "Android 5.1.1 VM" else "Android 7.1.2 VM"
        enteredName = defaultName
        inputBox?.setText(defaultName)
        inputBox?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                enteredName = s?.toString()?.trim()?.ifEmpty { defaultName } ?: defaultName
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        return view
    }
}
