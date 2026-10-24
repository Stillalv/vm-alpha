package com.android.vmapp.ui.vm.main

import android.content.Context
import android.content.Intent
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.vmapp.ui.vm.create.VMCreateActivity
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
        findViewById<TextView>(R.id.slogan)?.movementMethod = ScrollingMovementMethod()
        findViewById<View>(R.id.start)?.setOnClickListener {
            context.startActivity(Intent(context, VMCreateActivity::class.java))
        }
    }
}

class VMBigCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    var vm: VMInstance? = null
}

class VMBigPreviewCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    var vm: VMInstance? = null
}

class VMSmallCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    var vm: VMInstance? = null
}

class VMSmallPreviewCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    var vm: VMInstance? = null
}

class VMListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    var vm: VMInstance? = null
}
