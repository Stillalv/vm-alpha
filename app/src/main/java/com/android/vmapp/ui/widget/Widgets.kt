package com.android.vmapp.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.clone.android.dual.space.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator

class BottomWindowInsetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var insetHeight = 0

    init {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            insetHeight = navBars.bottom
            visibility = if (insetHeight == 0) GONE else VISIBLE
            requestLayout()
            insets
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), insetHeight)
    }
}

class StatusBarInsetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var insetHeight = 0

    init {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            insetHeight = statusBars.top
            visibility = if (insetHeight == 0) GONE else VISIBLE
            requestLayout()
            insets
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), insetHeight)
    }
}

class NavBarInsetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var insetHeight = 0

    init {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            insetHeight = navBars.bottom
            visibility = if (insetHeight == 0) GONE else VISIBLE
            requestLayout()
            insets
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), insetHeight)
    }
}

class VerticalTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private val textBounds = Rect()

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(0f, height.toFloat())
        canvas.rotate(-90f)
        val paint = paint
        paint.color = currentTextColor
        val str = text.toString()
        paint.getTextBounds(str, 0, str.length, textBounds)
        canvas.drawText(str, compoundPaddingLeft.toFloat(), (textBounds.height()).toFloat(), paint)
        canvas.restore()
    }
}

class CommonEmptyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var loadingView: CircularProgressIndicator? = null
    private var messageView: TextView? = null
    private var actionButton: Button? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_common_empty, this, true)
        loadingView = findViewById(R.id.indicator)
        messageView = findViewById(R.id.message)
        actionButton = findViewById(R.id.button)
    }

    fun showLoading() {
        loadingView?.visibility = VISIBLE
        messageView?.visibility = GONE
        actionButton?.visibility = GONE
    }

    fun showEmpty(message: String, actionText: String? = null, action: OnClickListener? = null) {
        loadingView?.visibility = GONE
        messageView?.visibility = VISIBLE
        messageView?.text = message
        if (actionText != null && action != null) {
            actionButton?.visibility = VISIBLE
            actionButton?.text = actionText
            actionButton?.setOnClickListener(action)
        } else {
            actionButton?.visibility = GONE
        }
    }
}

class FabScrollBehavior<V : View>(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<V>(context, attrs) {
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int, consumed: IntArray) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            child.visibility = View.INVISIBLE
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.visibility = View.VISIBLE
        }
    }
}
