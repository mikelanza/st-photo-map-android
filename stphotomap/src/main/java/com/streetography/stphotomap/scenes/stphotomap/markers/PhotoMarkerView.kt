package com.streetography.stphotomap.scenes.stphotomap.markers

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.streetography.stphotomap.R

class PhotoMarkerView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private var imageView: ImageView? = null

    init {
        this.findViews(context)
    }

    private fun findViews(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.st_photo_marker_view, this, true)

        imageView = view.findViewById(R.id.imageView)
    }

    fun setImageBackgroundColor(color: Int) {
        imageView?.setBackgroundColor(color)
    }

    fun setImageResource(drawable: Drawable?) {
        imageView?.setImageDrawable(drawable)
    }
}