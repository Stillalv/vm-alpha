package com.android.vmapp.ui.vm.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.vmapp.ui.vm.advanced.SettingsActivity
import com.android.vmapp.ui.vm.backup.VMBackupRestoreActivity
import com.android.vmapp.ui.vm.create.VMCreateActivity
import com.android.vmapp.ui.vm.settings.VMSettingsActivity
import com.android.vmapp.vm.VMDisplayActivity
import com.android.vmcore.VMInstance
import com.android.vmcore.VMManager
import com.clone.android.dual.space.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class VMFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var emptyView: VMEmptyCardView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vm, container, false)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar?.inflateMenu(R.menu.vm_tab_menu)
        toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.create_vm -> {
                    startActivity(Intent(requireContext(), VMCreateActivity::class.java))
                    true
                }
                R.id.backup_restore -> {
                    startActivity(Intent(requireContext(), VMBackupRestoreActivity::class.java))
                    true
                }
                R.id.floating_window, R.id.advanced_options -> {
                    startActivity(Intent(requireContext(), SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        recyclerView = view.findViewById(R.id.recyclerview)
        emptyView = view.findViewById(R.id.emptyView)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        refreshVMList()
        return view
    }

    override fun onResume() {
        super.onResume()
        refreshVMList()
    }

    private fun refreshVMList() {
        val ctx = context ?: return
        val vms = VMManager.getInstance(ctx).getAllVMs()
        if (vms.isEmpty()) {
            emptyView?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            emptyView?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
            recyclerView?.adapter = VMListAdapter(vms)
        }
    }

    private inner class VMListAdapter(
        private val items: List<VMInstance>
    ) : RecyclerView.Adapter<VMListAdapter.VH>() {

        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val nameText: TextView? = view.findViewById(R.id.name)
            val startBtn: MaterialButton? = view.findViewById(R.id.start)
            val shutdownBtn: MaterialButton? = view.findViewById(R.id.shutdown)
            val volumeBtn: MaterialButton? = view.findViewById(R.id.volume)
            val settingsBtn: MaterialButton? = view.findViewById(R.id.settings)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_vm_list_item, parent, false)
            return VH(v)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val vm = items[position]
            holder.nameText?.text = vm.config.name.ifEmpty { "VM #${vm.vmId}" }
            holder.startBtn?.setOnClickListener {
                vm.start()
                val intent = Intent(requireContext(), VMDisplayActivity::class.java)
                intent.putExtra("vm_id", vm.vmId)
                startActivity(intent)
            }
            holder.shutdownBtn?.setOnClickListener {
                vm.stop()
                notifyItemChanged(position)
            }
            holder.volumeBtn?.setOnClickListener {
                vm.halManager.audioService.toggleMute()
            }
            holder.settingsBtn?.setOnClickListener {
                val intent = Intent(requireContext(), VMSettingsActivity::class.java)
                intent.putExtra("vm_id", vm.vmId)
                startActivity(intent)
            }
        }
    }
}
