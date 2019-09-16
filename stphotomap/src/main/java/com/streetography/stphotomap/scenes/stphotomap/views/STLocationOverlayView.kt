package com.streetography.stphotomap.scenes.stphotomap.views

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.streetography.stphotomap.R
import com.streetography.stphotomap.extensions.view.transformDimension

interface STLocationOverlayViewDelegate {
    fun locationOverlayViewDidSelectPhoto(photoId: String?)
}

class STLocationOverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr) {
    class Model(
        var photoId: String,
        var title: String?,
        var time: String?,
        var description: String?
    )

    var model: Model? = null
        set(value) {
            field = value
            this.updateSubviews()
        }

    var delegate: STLocationOverlayViewDelegate? = null

    private lateinit var containerLayout: RelativeLayout
    private lateinit var titleTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var accessoryImageView: ImageView

    //region Init
    init {
        this.setupSubviews()
        this.addSubviews()
    }
    //endregion

    //region Subviews configuration
    private fun setupSubviews() {
        this.setupContainerLayout()
        this.setupTitleTextView()
        this.setupTimeTextView()
        this.setupDescriptionTextView()
        this.setupAccessoryImageView()
    }

    private fun setupContainerLayout() {
        this.containerLayout = RelativeLayout(this.context)
        val padding = this.transformDimension(15F).toInt()
        this.containerLayout.setPadding(padding, padding, padding, padding)
        this.containerLayout.setOnClickListener {
            this.delegate?.locationOverlayViewDidSelectPhoto(this.model?.photoId)
        }

        val drawable = GradientDrawable()
        drawable.cornerRadius = this.transformDimension(10F)
        drawable.setColor(Color.WHITE)
        drawable.setStroke(this.transformDimension(2F).toInt(), Color.rgb(73, 175, 253))
        this.containerLayout.background = drawable
    }

    private fun setupTitleTextView() {
        this.titleTextView = this.generateTextView()
        this.titleTextView.textSize = 16F
        this.titleTextView.setTypeface(this.titleTextView.typeface, Typeface.BOLD)
        this.titleTextView.setTextColor(Color.rgb(53, 61, 75))
    }

    private fun setupTimeTextView() {
        this.timeTextView = this.generateTextView()
        this.timeTextView.textSize = 13F
        this.timeTextView.setTypeface(this.timeTextView.typeface, Typeface.NORMAL)
        this.timeTextView.setTextColor(Color.DKGRAY)
    }

    private fun setupDescriptionTextView() {
        this.descriptionTextView = this.generateTextView()
        this.descriptionTextView.textSize = 13F
        this.descriptionTextView.setTypeface(this.descriptionTextView.typeface, Typeface.NORMAL)
        this.descriptionTextView.setTextColor(Color.DKGRAY)
    }

    private fun generateTextView(): TextView {
        val textView = TextView(this.context)
        textView.id = View.generateViewId()
        textView.maxLines = 1
        textView.ellipsize = TextUtils.TruncateAt.END
        return textView
    }

    private fun setupAccessoryImageView() {
        this.accessoryImageView = ImageView(this.context)
        this.accessoryImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        this.accessoryImageView.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.st_entity_level_block))
    }
    //endregion

    //region Subviews addition
    private fun addSubviews() {
        this.addContainerLayout()
        this.addTitleTextView()
        this.addTimeTextView()
        this.addDescriptionTextView()
        this.addAccessoryImageView()
    }

    private fun addContainerLayout() {
        val layoutParameters = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val margin = this.transformDimension(15F).toInt()
        layoutParameters.setMargins(margin, margin, margin, margin)
        this.addView(this.containerLayout, layoutParameters)
    }

    private fun addTitleTextView() {
        val layoutParameters = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParameters.addRule(START_OF, this.accessoryImageView.id)
        layoutParameters.addRule(ALIGN_PARENT_START)
        layoutParameters.marginEnd = this.transformDimension(15F).toInt()
        this.containerLayout.addView(this.titleTextView, layoutParameters)
    }

    private fun addTimeTextView() {
        val layoutParameters = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParameters.addRule(BELOW, this.titleTextView.id)
        layoutParameters.addRule(START_OF, this.accessoryImageView.id)
        layoutParameters.addRule(ALIGN_PARENT_START)
        layoutParameters.marginEnd = this.transformDimension(15F).toInt()
        this.containerLayout.addView(this.timeTextView, layoutParameters)
    }

    private fun addDescriptionTextView() {
        val layoutParameters = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParameters.addRule(BELOW, this.timeTextView.id)
        this.containerLayout.addView(this.descriptionTextView, layoutParameters)
    }

    private fun addAccessoryImageView() {
        val size = this.transformDimension(30F).toInt()
        val layoutParameters = LayoutParams(size, size)
        layoutParameters.addRule(ALIGN_PARENT_END)
        layoutParameters.addRule(CENTER_VERTICAL)
        this.containerLayout.addView(this.accessoryImageView, layoutParameters)
    }
    //endregion

    //region Subviews update
    private fun updateSubviews() {
        this.updateTitleTextView()
        this.updateTimeTextView()
        this.updateDescriptionTextView()
    }

    private fun updateTitleTextView() {
        this.titleTextView.text = this.model?.title
    }

    private fun updateTimeTextView() {
        this.timeTextView.text = this.model?.time
    }

    private fun updateDescriptionTextView() {
        this.descriptionTextView.text = this.model?.description
        this.descriptionTextView.visibility = if (this.model?.description.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
    //endregion
}