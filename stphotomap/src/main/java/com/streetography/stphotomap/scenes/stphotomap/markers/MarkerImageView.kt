package com.streetography.stphotomap.scenes.stphotomap.markers

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout

class MarkerImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr) {
    private val cornerRadius = 12F
    private val margins = 7F

    private var imageView: ImageView = ImageView(this.context)

    init {
        this.setup()
        this.setupImageView()
    }

    private fun setup() {
        this.background = this.roundedDrawable()
    }

    private fun setupImageView() {
        this.imageView.setBackgroundColor(Color.LTGRAY)
        this.imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        val margin = this.transformDimension(this.margins).toInt()
        val layoutParameters = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParameters.setMargins(margin, margin, margin, margin)
        this.addView(this.imageView, layoutParameters)
    }

    private fun roundedDrawable(): PaintDrawable {
        val drawable = PaintDrawable()
        drawable.setCornerRadius(this.transformDimension(this.cornerRadius))
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        return drawable
    }

    fun setImageDrawable(image: Drawable?) {
        this.imageView.setImageDrawable(image)
    }
}