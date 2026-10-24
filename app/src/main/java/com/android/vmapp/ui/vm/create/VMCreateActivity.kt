package com.android.vmapp.ui.vm.create

import android.os.Bundle
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import com.android.vmapp.ui.base.BaseActivity
import com.android.vmapp.ui.vm.resolution.ResolutionFragment
import com.android.vmapp.ui.vm.rom.RomFragment
import com.android.vmcore.DisplayConfig
import com.android.vmcore.RomConfig
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
            "galaxy_s6_5_1"
        } else {
            "pixel_xl_7_1"
        }
        val romConfig = RomConfig().apply {
            id = if (RomFragment.selectedRomSdk == 22) "rom_5_1_1" else "rom_7_1_2"
            displayName = RomFragment.selectedRomName
            osVersion = RomFragment.selectedRomSdk
            romVersion = 1
            romUri = arrayOf("asset:///rom71_64.7z")
            magiskUri = "asset:///plugins/magisk.zip"
            suUri = "asset:///plugins/superuser.zip"
            xposedUri = "asset:///plugins/xposed.zip"
            playUri = "asset:///plugins/play.zip"
        }
        val displayConfig = DisplayConfig(
            name = "${ResolutionFragment.selectedWidth}x${ResolutionFragment.selectedHeight}",
            width = ResolutionFragment.selectedWidth,
            height = ResolutionFragment.selectedHeight,
            dpi = ResolutionFragment.selectedDpi
        )
        VMManager.getInstance(this).createVM(
            name = VMNamingFragment.enteredName,
            romConfig = romConfig,
            displayConfig = displayConfig,
            presetId = presetId
        )
        finish()
    }
}
