package com.android.vmapp.ui.vm.resolution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clone.android.dual.space.R

class ResolutionFragment : Fragment() {
    data class ResOption(val title: String, val width: Int, val height: Int, val dpi: Int)

    companion object {
        var selectedWidth: Int = 1080
        var selectedHeight: Int = 1920
        var selectedDpi: Int = 420
    }

    private val options = listOf(
        ResOption("1080 × 1920 (FHD 16:9)", 1080, 1920, 420),
        ResOption("1080 × 2400 (FHD+ 20:9)", 1080, 2400, 420),
        ResOption("720 × 1280 (HD 16:9)", 720, 1280, 320),
        ResOption("720 × 1600 (HD+ 20:9)", 720, 1600, 320),
        ResOption("1440 × 2560 (QHD 16:9)", 1440, 2560, 560)
    )

    private var selectedIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resolution, container, false)
        val listView = view.findViewById<RecyclerView>(R.id.list)
        listView?.layoutManager = LinearLayoutManager(requireContext())
        listView?.adapter = ResAdapter()
        return view
    }

    private inner class ResAdapter : RecyclerView.Adapter<ResAdapter.VH>() {
        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val radio: RadioButton? = view.findViewById(R.id.select)
            val title: TextView? = view.findViewById(R.id.title)
            val desc: TextView? = view.findViewById(R.id.desc)
            val delBtn: View? = view.findViewById(R.id.del)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_resolution_item, parent, false)
            return VH(v)
        }

        override fun getItemCount(): Int = options.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = options[position]
            holder.radio?.isChecked = position == selectedIndex
            holder.title?.text = item.title
            holder.desc?.text = "${item.dpi} DPI"
            holder.delBtn?.visibility = View.GONE
            holder.itemView.setOnClickListener {
                val old = selectedIndex
                selectedIndex = holder.bindingAdapterPosition.coerceAtLeast(0)
                val chosen = options[selectedIndex]
                selectedWidth = chosen.width
                selectedHeight = chosen.height
                selectedDpi = chosen.dpi
                notifyItemChanged(old)
                notifyItemChanged(selectedIndex)
            }
        }
    }
}
