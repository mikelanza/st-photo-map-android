package com.streetography.stphotomap.scenes.stphotomap.markers.cluster

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.PaintDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import com.streetography.stphotomap.extensions.view.transformDimension

class ClusterTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): TextView(context, attrs, defStyleAttr) {
    val titleWidth = 60F
    val titleHeight = 60F
    private val cornerRadius = 12F

    init {
        this.setup()
    }

    private fun setup() {
        this.textSize = 18F
        this.setTypeface(this.typeface, Typeface.BOLD)
        this.gravity = Gravity.CENTER
        this.setTextColor(Color.WHITE)
    }

    fun setIsSelected(isSelected: Boolean) {
        val drawable = PaintDrawable()
        drawable.setCornerRadius(this.transformDimension(this.cornerRadius))
        val color = if (isSelected) Color.argb(100, 53, 61, 75) else Color.rgb(53, 61, 75)
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        this.background = drawable
    }
}