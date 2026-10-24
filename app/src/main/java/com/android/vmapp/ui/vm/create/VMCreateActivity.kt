package com.android.vmapp.ui.vm.create

import android.os.Bundle
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import com.android.vmapp.ui.base.BaseActivity
import com.android.vmapp.ui.vm.resolution.ResolutionFragment
import com.android.vmapp.ui.vm.rom.RomFragment
import com.android.vmcore.DevicePreset
import com.android.vmcore.VMManager
import com.clone.android.dual.space.R
import com.google.android.material.appbar.MaterialToolbar

class VMCreateActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_create)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val nextBtn = findViewById<Button>(R.id.button)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as? NavHostFragment
        val navController = navHostFragment?.navController

        toolbar?.setNavigationOnClickListener {
            if (navController == null || !navController.popBackStack()) {
                finish()
            }
        }

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            toolbar?.title = destination.label
            when (destination.id) {
                R.id.action_naming -> nextBtn?.setText(R.string.done)
                else -> nextBtn?.setText(R.string.next)
            }
        }

        nextBtn?.setOnClickListener {
            val currentId = navController?.currentDestination?.id
            when (currentId) {
                R.id.action_rom_selection -> {
                    navController.navigate(R.id.action_resolution_selection)
                }
                R.id.action_resolution_selection -> {
                    navController.navigate(R.id.action_naming)
                }
                R.id.action_naming -> {
                    createVMAndFinish()
                }
                else -> {
                    createVMAndFinish()
                }
            }
        }
    }

    private fun createVMAndFinish() {
        val presetId = if (RomFragment.selectedRomSdk == 22) {
            "samsung_s6_51"
        } else {
            DevicePreset.DEFAULT_71_PRESET_ID
        }
        val vm = VMManager.getInstance(this).createVM(
            name = VMNamingFragment.enteredName,
            presetId = presetId
        )
        vm.config.width = ResolutionFragment.selectedWidth
        vm.config.height = ResolutionFragment.selectedHeight
        vm.config.dpi = ResolutionFragment.selectedDpi
        vm.config.save(this)
        finish()
    }
}
