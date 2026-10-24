package com.android.vmapp.ui.vm.rom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.clone.android.dual.space.R
import com.google.android.material.button.MaterialButton

class RomFragment : Fragment() {
    data class RomItem(
        val id: String,
        val name: String,
        val arch: String,
        val desc: String,
        val sdk: Int,
        val isBuiltIn: Boolean
    )

    companion object {
        var selectedRomSdk: Int = 25
        var selectedRomName: String = "Android 7.1.2 (Built-in Default)"
    }

    private val romList = listOf(
        RomItem(
            id = "rom_712_arm64",
            name = "Android 7.1.2 Nougat (Built-in)",
            arch = "64-bit (arm64-v8a / armeabi-v7a)",
            desc = "Included in APK assets (rom71_64.7z) • Ready to use",
            sdk = 25,
            isBuiltIn = true
        ),
        RomItem(
            id = "rom_511_arm64",
            name = "Android 5.1.1 Lollipop",
            arch = "64-bit / 32-bit Compatibility",
            desc = "Lightweight legacy ROM preset",
            sdk = 22,
            isBuiltIn = false
        )
    )

    private var selectedIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rom, container, false)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        swipeRefresh?.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
        }

        val listView = view.findViewById<RecyclerView>(R.id.listview)
        listView?.layoutManager = LinearLayoutManager(requireContext())
        listView?.adapter = RomAdapter()
        return view
    }

    private inner class RomAdapter : RecyclerView.Adapter<RomAdapter.VH>() {
        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val icon: ImageView? = view.findViewById(R.id.rom_icon)
            val name: TextView? = view.findViewById(R.id.rom_name)
            val arch: TextView? = view.findViewById(R.id.rom_arch)
            val desc: TextView? = view.findViewById(R.id.rom_desc)
            val action: MaterialButton? = view.findViewById(R.id.action)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_rom_list_item, parent, false)
            return VH(v)
        }

        override fun getItemCount(): Int = romList.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = romList[position]
            holder.icon?.setImageResource(R.mipmap.ic_launcher)
            holder.name?.text = item.name
            holder.arch?.text = item.arch
            holder.desc?.text = item.desc
            val isSelected = position == selectedIndex
            holder.action?.text = if (isSelected) "Selected" else "Select"
            val clickListener = View.OnClickListener {
                val old = selectedIndex
                selectedIndex = holder.bindingAdapterPosition.coerceAtLeast(0)
                selectedRomSdk = romList[selectedIndex].sdk
                selectedRomName = romList[selectedIndex].name
                notifyItemChanged(old)
                notifyItemChanged(selectedIndex)
            }
            holder.itemView.setOnClickListener(clickListener)
            holder.action?.setOnClickListener(clickListener)
        }
    }
}
