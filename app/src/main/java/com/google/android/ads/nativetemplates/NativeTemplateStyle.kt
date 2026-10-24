package com.google.android.ads.nativetemplates

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable

class NativeTemplateStyle {
    var callToActionBackgroundColor: ColorDrawable? = null
    var callToActionTypeface: Typeface? = null
    var callToActionTextSize: Float = 0f
    var callToActionTypefaceColor: Int = 0
    var primaryTextTypeface: Typeface? = null
    var primaryTextSize: Float = 0f
    var primaryTextTypefaceColor: Int = 0
    var primaryTextBackgroundColor: ColorDrawable? = null
    var secondaryTextTypeface: Typeface? = null
    var secondaryTextSize: Float = 0f
    var secondaryTextTypefaceColor: Int = 0
    var secondaryTextBackgroundColor: ColorDrawable? = null
    var tertiaryTextTypeface: Typeface? = null
    var tertiaryTextSize: Float = 0f
    var tertiaryTextTypefaceColor: Int = 0
    var tertiaryTextBackgroundColor: ColorDrawable? = null
    var mainBackgroundColor: ColorDrawable? = null

    class Builder {
        val styles = NativeTemplateStyle()

        fun withCallToActionBackgroundColor(drawable: ColorDrawable) = apply { styles.callToActionBackgroundColor = drawable }
        fun withCallToActionTypeface(typeface: Typeface) = apply { styles.callToActionTypeface = typeface }
        fun withCallToActionTextSize(size: Float) = apply { styles.callToActionTextSize = size }
        fun withCallToActionTypefaceColor(color: Int) = apply { styles.callToActionTypefaceColor = color }
        fun withPrimaryTextTypeface(typeface: Typeface) = apply { styles.primaryTextTypeface = typeface }
        fun withPrimaryTextSize(size: Float) = apply { styles.primaryTextSize = size }
        fun withPrimaryTextTypefaceColor(color: Int) = apply { styles.primaryTextTypefaceColor = color }
        fun withPrimaryTextBackgroundColor(drawable: ColorDrawable) = apply { styles.primaryTextBackgroundColor = drawable }
        fun withSecondaryTextTypeface(typeface: Typeface) = apply { styles.secondaryTextTypeface = typeface }
        fun withSecondaryTextSize(size: Float) = apply { styles.secondaryTextSize = size }
        fun withSecondaryTextTypefaceColor(color: Int) = apply { styles.secondaryTextTypefaceColor = color }
        fun withSecondaryTextBackgroundColor(drawable: ColorDrawable) = apply { styles.secondaryTextBackgroundColor = drawable }
        fun withMainBackgroundColor(drawable: ColorDrawable) = apply { styles.mainBackgroundColor = drawable }
        fun build() = styles
    }
}
