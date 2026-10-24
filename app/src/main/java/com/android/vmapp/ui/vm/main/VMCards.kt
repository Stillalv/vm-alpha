package com.android.vmapp.ui.vm.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.vmcore.VMInstance
import com.clone.android.dual.space.R
import com.google.android.material.card.MaterialCardView

class VMBigCardLayoutManager(context: Context) : LinearLayoutManager(context, HORIZONTAL, false)
class VMSmallCardLayoutManager(context: Context) : LinearLayoutManager(context, HORIZONTAL, false)
class VMListItemLayoutManager(context: Context) : LinearLayoutManager(context, VERTICAL, false)

class VMEmptyCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_vm_empty_card, this, true)
    }
}

class VMBigCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    var vm: VMInstance? = null
    init {
        LayoutInflater.from(context).inflate(R.layout.view_vm_big_card, this, true)
    }
}

class VMBigPreviewCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    var vm: VMInstance? = null
    init {
        LayoutInflater.from(context).inflate(R.layout.view_vm_big_preview_card, this, true)
    }
}

class VMSmallCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    var vm: VMInstance? = null
    init {
        LayoutInflater.from(context).inflate(R.layout.view_vm_small_card, this, true)
    }
}

class VMSmallPreviewCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    var vm: VMInstance? = null
    init {
        LayoutInflater.from(context).inflate(R.layout.view_vm_small_preview_card, this, true)
    }
}

class VMListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    var vm: VMInstance? = null
    init {
        LayoutInflater.from(context).inflate(R.layout.view_vm_list_item, this, true)
    }
}
