package com.android.ui.auth.ui.phone

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.style.ScaleXSpan
import android.util.AttributeSet
import android.widget.TextView
import com.clone.android.dual.space.R
import com.google.android.material.textfield.TextInputEditText

class SpacedEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    private var spacingProportion: Float = 1.0f
    private var unspacedText: SpannableStringBuilder = SpannableStringBuilder("")

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.SpacedEditText)
            spacingProportion = a.getFloat(R.styleable.SpacedEditText_spacingProportion, 1.0f)
            a.recycle()
        }
    }

    fun getUnspacedText(): Editable = unspacedText

    override fun setText(text: CharSequence?, type: TextView.BufferType?) {
        val raw = text ?: ""
        unspacedText = SpannableStringBuilder(raw)
        val sb = SpannableStringBuilder()
        val len = raw.length
        for (i in 0 until len) {
            sb.append(raw[i])
            if (i < len - 1) {
                sb.append(" ")
                val spanStart = sb.length - 1
                sb.setSpan(ScaleXSpan(spacingProportion), spanStart, spanStart + 1, 33)
            }
        }
        super.setText(sb, TextView.BufferType.SPANNABLE)
    }
}
