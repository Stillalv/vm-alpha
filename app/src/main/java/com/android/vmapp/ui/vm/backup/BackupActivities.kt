package com.android.vmapp.ui.vm.backup

import android.os.Bundle
import com.android.vmapp.ui.base.BaseActivity
import com.clone.android.dual.space.R

class VMBackupActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_backup)
    }
}

class VMBackupRestoreActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vm_backup_restore)
    }
}
