package com.streetography.stphotomap.scenes.stphotomap.markers.cluster

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.streetography.stphotomap.extensions.view.transformDimension
import com.streetography.stphotomap.scenes.stphotomap.markers.photo.MarkerImageView
import com.streetography.stphotomap.scenes.stphotomap.markers.photo.PhotoMarker
import kotlin.math.cos
import kotlin.math.sin

class PhotoClusterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr) {
    private val itemWidth = 80F
    private val itemHeight = 80F
    private val itemOffset = 12F

    private var titleTextView: ClusterTextView =
        ClusterTextView(this.context)
    private var imageViews: ArrayList<MarkerImageView> = ArrayList()
    private var lines: ArrayList<Line> = ArrayList()

    private val paint: Paint = Paint()

    private var photoMarkers: ArrayList<PhotoMarker> = ArrayList()
    private var count: Int = 0

    init {
        this.setupPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val elements = this.setupDrawingElements()
        val radius = this.transformDimension(elements.radius).toInt()

        val width = resolveSizeAndState((radius * 4), widthMeasureSpec, 1)
        val height = resolveSizeAndState((radius * 4), heightMeasureSpec, 0)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            for (line in lines) {
                drawLine(line.startX, line.startY, line.stopX, line.stopY, paint)
            }
        }
    }

    fun setPhotoMarkers(markers: ArrayList<PhotoMarker>) {
        this.photoMarkers = markers
        this.count = markers.size
    }

    private fun setupPaint() {
        this.paint.color = Color.BLACK
        this.paint.strokeWidth = 2f
        this.paint.strokeJoin = Paint.Join.BEVEL
        this.paint.strokeCap = Paint.Cap.SQUARE
        this.paint.isAntiAlias = true
        this.paint.isDither = true
        this.paint.style = Paint.Style.STROKE
    }

    //region Lines configuration
    fun addLines() {
        this.lines.clear()
        this.setupLines()
        this.invalidate()
    }

    private fun setupLines() {
        val elements = this.setupDrawingElements()
        val startingRadians = elements.startingRadians
        val deltaRadians = elements.deltaRadians
        val radius = this.transformDimension(elements.radius)
        val offset: Float = this.transformDimension(this.itemOffset)
        val centerPoint = this.centerPoint()

        for (i in 0 until this.count) {
            val radians = (i.toDouble() * deltaRadians) + startingRadians
            val startX = centerPoint.x + (offset * cos(radians))
            val startY = centerPoint.y + (offset * sin(radians))
            val stopX = startX + (radius * cos(radians))
            val stopY = startY + (radius * sin(radians))
            this.lines.add(
                Line(
                    startX.toFloat(),
                    startY.toFloat(),
                    stopX.toFloat(),
                    stopY.toFloat()
                )
            )
        }
    }
    //endregion

    //region Title configuration
    fun addTitle() {
        this.removeView(this.titleTextView)
        this.setupTitleTextView()
    }

    private fun setupTitleTextView() {
        this.titleTextView =
            ClusterTextView(this.context)
        this.titleTextView.text = this.count.toString()

        val width = this.transformDimension(this.titleTextView.titleWidth).toInt()
        val height = this.transformDimension(this.titleTextView.titleHeight).toInt()
        val layoutParameters = LayoutParams(width, height)
        this.addView(this.titleTextView, layoutParameters)

        val centerPoint = this.centerPoint()
        this.titleTextView.x = (centerPoint.x - (width / 2)).toFloat()
        this.titleTextView.y = (centerPoint.y - (height / 2)).toFloat()
    }
    //endregion

    //region Images configuration
    fun addImages() {
        this.imageViews.clear()
        this.setupImageViews()
    }

    fun clearImages() {
        for (imageView in this.imageViews) {
            imageView.setImageDrawable(null)
        }
        this.imageViews.clear()
    }

    fun setImageResource(image: Drawable?, index: Int) {
        if (this.imageViews.indices.contains(index)) {
            this.imageViews[index].setImageDrawable(image)
        }
    }

    private fun setupImageViews() {
        val elements = this.setupDrawingElements()
        val startingRadians = elements.startingRadians
        val deltaRadians = elements.deltaRadians
        val radius = this.transformDimension(elements.radius)
        val offset: Float = this.transformDimension(this.itemOffset)

        for (i in 0 until this.count) {
            val imageView = MarkerImageView(this.context)

            val width = this.transformDimension(this.itemWidth).toInt()
            val height = this.transformDimension(this.itemHeight).toInt()
            val layoutParameters = LayoutParams(width, height)
            this.addView(imageView, layoutParameters)
            this.imageViews.add(imageView)

            val centerPoint = this.centerPoint()
            val radians = (i.toDouble() * deltaRadians) + startingRadians
            val startX = centerPoint.x + (offset * cos(radians))
            val startY = centerPoint.y + (offset * sin(radians))
            val stopX = startX + (radius * cos(radians))
            val stopY = startY + (radius * sin(radians))
            imageView.x = (stopX - (width / 2)).toFloat()
            imageView.y = (stopY - (height / 2)).toFloat()
        }
    }
    //endregion

    private fun setupDrawingElements(): Elements {
        val totalRadians = Math.toRadians(360.0)
        val deltaRadians: Double = totalRadians / this.count.toDouble()
        val startingRadians: Double = Math.toRadians(90.0)
        val radius: Float = if (this.count <=7) 80F else 100F
        return Elements(
            startingRadians,
            deltaRadians,
            radius
        )
    }

    private fun centerPoint(): Point {
        val x = (this.x + this.width / 2).toInt()
        val y = (this.y + this.height / 2).toInt()
        return Point(x, y)
    }

    private class Elements(val startingRadians: Double, val deltaRadians: Double, val radius: Float)

    private class Line(val startX: Float, val startY: Float, val stopX: Float, val stopY: Float)
}