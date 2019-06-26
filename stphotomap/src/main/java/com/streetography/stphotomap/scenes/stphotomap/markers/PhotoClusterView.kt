package com.streetography.stphotomap.scenes.stphotomap.markers

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.streetography.stphotomap.R

class PhotoMapClusterMarkerView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private var mBadgeNumber: TextView? = null

    init {
        this.findViews(context)
    }

    private fun findViews(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.st_photo_cluster_view, this, true)

        mBadgeNumber = view.findViewById(R.id.badgeTextView)
    }

    fun setupBadgeNumber(badgeNumber: Int) {
        mBadgeNumber?.text = badgeNumber.toString()
    }
}