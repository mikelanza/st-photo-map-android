package com.streetography.stphotomap.scenes.stphotomap.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.streetography.stphotomap.R

class STEntityLevelView : RelativeLayout {
    private var containerLayout: LinearLayout? = null
    private var titleTextView: TextView? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        inflateSubviews()
        findSubviews()
        setupSubviews()
    }

    private fun inflateSubviews() {
        View.inflate(this.context, R.layout.st_entity_level_view, this)
    }

    private fun findSubviews() {
        this.containerLayout = findViewById(R.id.containerLayout)
        this.titleTextView = findViewById(R.id.titleTextView)
    }

    private fun setupSubviews() {
        setupContainerLayout()
        setupTitleTextView()
    }

    private fun setupContainerLayout() {
        this.containerLayout?.setBackgroundColor(Color.WHITE) // TODO - Add color from styles!
    }

    private fun setupTitleTextView() {
        this.titleTextView?.setTextColor(Color.WHITE) // TODO - Add color and text size from styles!
    }

    fun setTitle(title: String) {
        this.titleTextView?.text = title
    }

    fun show() {
        fadeIn()
        postDelayed({ fadeOut() }, SHOW_DURATION)
    }

    private fun fadeIn() {
        this.alpha = 0f
        this.visibility = View.VISIBLE
        animate().setDuration(FADE_DURATION).alpha(1f)
    }

    private fun fadeOut() {
        animate().setDuration(FADE_DURATION).alpha(0f)
        postDelayed({ this.visibility = View.GONE }, FADE_DURATION)
    }

    companion object {
        private val FADE_DURATION: Long = 300
        private val SHOW_DURATION: Long = 2000 // TODO - Add duration from styles!
    }
}