package com.google.android.ads.nativetemplates

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.clone.android.dual.space.R
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class TemplateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var templateType: Int = R.layout.gnt_medium_template_view
    private var nativeAdView: NativeAdView? = null
    private var primaryView: TextView? = null
    private var secondaryView: TextView? = null
    private var ratingBar: RatingBar? = null
    private var callToActionView: Button? = null
    private var iconView: ImageView? = null
    private var mediaView: MediaView? = null
    private var bodyView: TextView? = null
    private var styles: NativeTemplateStyle? = null

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.TemplateView, 0, 0)
        try {
            templateType = a.getResourceId(R.styleable.TemplateView_gnt_template_type, R.layout.gnt_medium_template_view)
        } finally {
            a.recycle()
        }
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(templateType, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        nativeAdView = findViewById(R.id.native_ad_view)
        primaryView = findViewById(R.id.primary)
        secondaryView = findViewById(R.id.secondary)
        bodyView = findViewById(R.id.body)
        ratingBar = findViewById(R.id.rating_bar)
        ratingBar?.isEnabled = false
        callToActionView = findViewById(R.id.cta)
        iconView = findViewById(R.id.icon)
        mediaView = findViewById(R.id.media_view)
    }

    val templateTypeName: String
        get() = when (templateType) {
            R.layout.gnt_medium_template_view -> "medium_template"
            R.layout.gnt_small_template_view -> "small_template"
            else -> ""
        }

    fun getNativeAdView(): NativeAdView? = nativeAdView

    fun setStyles(styles: NativeTemplateStyle?) {
        this.styles = styles
        applyStyles()
    }

    private fun applyStyles() {
        val s = styles ?: return
        s.mainBackgroundColor?.let { background = it }
        primaryView?.let { tv ->
            s.primaryTextTypefaceColor.takeIf { it != 0 }?.let { tv.setTextColor(it) }
            s.primaryTextSize.takeIf { it > 0 }?.let { tv.textSize = it }
            s.primaryTextTypeface?.let { tv.typeface = it }
            s.primaryTextBackgroundColor?.let { tv.background = it }
        }
        secondaryView?.let { tv ->
            s.secondaryTextTypefaceColor.takeIf { it != 0 }?.let { tv.setTextColor(it) }
            s.secondaryTextSize.takeIf { it > 0 }?.let { tv.textSize = it }
            s.secondaryTextTypeface?.let { tv.typeface = it }
            s.secondaryTextBackgroundColor?.let { tv.background = it }
        }
        callToActionView?.let { btn ->
            s.callToActionTypefaceColor.takeIf { it != 0 }?.let { btn.setTextColor(it) }
            s.callToActionTextSize.takeIf { it > 0 }?.let { btn.textSize = it }
            s.callToActionTypeface?.let { btn.typeface = it }
            s.callToActionBackgroundColor?.let { btn.background = it }
        }
        invalidate()
        requestLayout()
    }

    fun setNativeAd(nativeAd: NativeAd) {
        val adView = nativeAdView ?: return

        adView.callToActionView = callToActionView
        adView.headlineView = primaryView
        adView.mediaView = mediaView

        val store = nativeAd.store
        val advertiser = nativeAd.advertiser

        secondaryView?.visibility = VISIBLE
        val secondaryText: String
        if (!TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser)) {
            adView.storeView = secondaryView
            secondaryText = store ?: ""
        } else if (!TextUtils.isEmpty(advertiser)) {
            adView.advertiserView = secondaryView
            secondaryText = advertiser ?: ""
        } else {
            secondaryText = ""
        }

        primaryView?.text = nativeAd.headline
        callToActionView?.text = nativeAd.callToAction

        val starRating = nativeAd.starRating
        if (starRating == null || starRating <= 0.0) {
            secondaryView?.text = secondaryText
            secondaryView?.visibility = VISIBLE
            ratingBar?.visibility = GONE
        } else {
            secondaryView?.visibility = GONE
            ratingBar?.visibility = VISIBLE
            ratingBar?.rating = starRating.toFloat()
            adView.starRatingView = ratingBar
        }

        val icon = nativeAd.icon
        if (icon != null) {
            iconView?.visibility = VISIBLE
            iconView?.setImageDrawable(icon.drawable)
            adView.iconView = iconView
        } else {
            iconView?.visibility = GONE
        }

        bodyView?.let { bv ->
            bv.text = nativeAd.body
            adView.bodyView = bv
        }

        adView.setNativeAd(nativeAd)
    }
}
