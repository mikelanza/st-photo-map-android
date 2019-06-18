package com.streetography.stphotomap.scenes.stphotomap.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.streetography.stphotomap.R

open public class STEntityLevelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr) {
    private var containerLayout: LinearLayout? = null
    private var titleTextView: TextView? = null

    open fun setImage(resourceId: Int) {
//        val drawable = ContextCompat.getDrawable(this.context, resourceId)
//        this.titleTextView?.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
    }

    open fun setTitle(titleId: Int) {
//        this.titleTextView?.text = this.resources.getString(titleId)
        this.titleTextView?.text = "Entity Level"
    }

    open fun show() {
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