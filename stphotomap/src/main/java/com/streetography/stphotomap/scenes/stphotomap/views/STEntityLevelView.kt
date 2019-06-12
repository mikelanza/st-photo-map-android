package com.streetography.stphotomap.scenes.stphotomap.views

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.streetography.stphotomap.R

public class STEntityLevelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr) {
    private var containerLayout: LinearLayout? = null
    private var titleTextView: TextView? = null

    public fun setImage(resourceId: Int) {
        val drawable = ContextCompat.getDrawable(this.context, resourceId)
        this.titleTextView?.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
    }

    public fun setTitle(title: String?) {
        this.titleTextView?.text = title
    }

    public fun show() {
        fadeIn()
        postDelayed({ fadeOut() }, showDuration)
    }

    init {
        inflateSubviews()
        findSubviews()
    }

    private fun inflateSubviews() {
        View.inflate(this.context, R.layout.st_entity_level_view, this)
    }

    private fun findSubviews() {
        this.containerLayout = findViewById(R.id.containerLayout)
        this.titleTextView = findViewById(R.id.titleTextView)
    }

    private fun fadeIn() {
        this.alpha = 0f
        this.visibility = View.VISIBLE
        animate().setDuration(fadeDuration).alpha(1f)
    }

    private fun fadeOut() {
        animate().setDuration(fadeDuration).alpha(0f)
        postDelayed({ this.visibility = View.GONE }, fadeDuration)
    }

    private val showDuration: Long
        get() {
            val attributes = this.context.obtainStyledAttributes(R.style.STEntityLevelView, R.styleable.STEntityLevelView)
            val duration = attributes.getFloat(0, 0F)
            attributes.recycle()
            return duration.toLong()
        }

    private val fadeDuration: Long
        @SuppressLint("ResourceType")
        get() {
            val attributes = this.context.obtainStyledAttributes(R.style.STEntityLevelView, R.styleable.STEntityLevelView)
            val duration = attributes.getFloat(1, 0F)
            attributes.recycle()
            return duration.toLong()
        }
}