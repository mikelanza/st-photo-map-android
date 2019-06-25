package com.streetography.stphotomap.scenes.stphotomap

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.gms.maps.model.TileProvider
import com.google.android.gms.maps.model.UrlTileProvider
import com.streetography.stphotomap.R
import com.streetography.stphotomap.extensions.google_map.visibleTiles
import com.streetography.stphotomap.extensions.visible_region.boundingBox
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.scenes.stphotomap.builders.STPhotoMapUriBuilder
import com.streetography.stphotomap.scenes.stphotomap.interactor.STPhotoMapBusinessLogic
import com.streetography.stphotomap.scenes.stphotomap.interactor.STPhotoMapInteractor
import com.streetography.stphotomap.scenes.stphotomap.views.STEntityLevelView
import java.lang.ref.WeakReference
import java.net.MalformedURLException
import java.net.URL

interface STPhotoMapDisplayLogic {
    fun displayLoadingState()
    fun displayNotLoadingState()

    fun displayEntityLevel(viewModel: STPhotoMapModels.EntityZoomLevel.ViewModel)
}

public open class STPhotoMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr),
    STPhotoMapDisplayLogic, OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener {
    var interactor: STPhotoMapBusinessLogic? = null

    var entityLevelView: STEntityLevelView? = null
    var mapView: GoogleMap? = null
    var progressBar: ProgressBar? = null

    init {
        this.setup()
        this.setupSubviews()
    }

    private fun setup() {
        val interactor = STPhotoMapInteractor()
        val presenter = STPhotoMapPresenter()

        presenter.displayer = WeakReference(this)
        interactor.presenter = presenter

        this.interactor = interactor
    }

    //region Subviews configuration
    private fun setupSubviews() {
        this.setupMapView()
        this.setupProgressBar()
        this.setupEntityView()
    }

    private fun setupMapView() {
        val options = GoogleMapOptions()
        options.mapType(GoogleMap.MAP_TYPE_NONE)
            .compassEnabled(false)
            .rotateGesturesEnabled(false)
            .tiltGesturesEnabled(false)

        val mapView = MapView(this.context, options)
        mapView.onCreate(Bundle())
        mapView.onResume()
        mapView.getMapAsync(this)
        mapView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.addView(mapView)
    }

    private fun setupMapViewListeners() {
        this.mapView?.setOnCameraIdleListener(this)
        this.mapView?.setOnCameraMoveListener(this)
    }

    private fun setupProgressBar() {
        this.progressBar = ProgressBar(this.context, null, android.R.attr.progressBarStyleHorizontal)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(ALIGN_PARENT_TOP)
        this.progressBar?.layoutParams = layoutParams
        this.progressBar?.progress = 0
        this.progressBar?.max = 100
        this.progressBar?.visibility = View.GONE
        this.progressBar?.isIndeterminate = false
        val drawable = this.progressBar?.progressDrawable?.mutate()
        drawable?.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
        this.progressBar?.progressDrawable = drawable
        this.addView(this.progressBar)
    }

    private fun setupEntityView() {
        this.entityLevelView = STEntityLevelView(this.context, null)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParams.addRule(CENTER_IN_PARENT)
        this.entityLevelView?.layoutParams = layoutParams
        this.entityLevelView?.visibility = View.GONE
        this.entityLevelView?.setBackgroundColor(Color.TRANSPARENT)
        this.addView(this.entityLevelView)
    }
    //endregion

    //region Google map style
    private fun setupGoogleMapStyle() {
        this.mapView?.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.google_map_style))
    }
    //endregion

    //region Google map interfaces
    override fun onMapReady(googleMap: GoogleMap) {
        this.mapView = googleMap
        this.mapView?.addTileOverlay(getTileOverlayOptions())

        this.setupGoogleMapStyle()
        this.setupMapViewListeners()
    }

    override fun onCameraIdle() {
        this.shouldUpdateVisibleTiles()
        this.shouldCacheGeojsonObjects()
        this.shouldDetermineEntityLevel()
    }

    override fun onCameraMove() {
        this.interactor?.shouldUpdateBoundingBox(STPhotoMapModels.UpdateBoundingBox.Request(this.mapView?.projection?.visibleRegion?.boundingBox()))
    }
    //endregion

    //region Tile overlay & provider
    private fun getTileOverlayOptions(): TileOverlayOptions {
        return TileOverlayOptions().tileProvider(getTileProvider()).transparency(0.0f)
    }

    private fun getTileProvider(): TileProvider {
        return object : UrlTileProvider(512, 512) {
            override fun getTileUrl(x: Int, y: Int, zoom: Int): URL {
                val uri = STPhotoMapUriBuilder().jpegTileUri(TileCoordinate(zoom, x, y)).second
                Log.i("STPhotoMapView", String.format("x=%d, y=%d z=%d", x, y, zoom));
                try {
                    return URL(uri)
                } catch (e: MalformedURLException) {
                    throw AssertionError(e)
                }
            }
        }
    }
    //endregion

    //region Business logic
    private fun shouldUpdateVisibleTiles() {
        val visibleTiles: ArrayList<TileCoordinate> = this.mapView?.visibleTiles() ?: ArrayList()
        this.interactor?.shouldUpdateVisibleTiles(STPhotoMapModels.VisibleTiles.Request(visibleTiles))
    }

    private fun shouldDetermineEntityLevel() {
        this.interactor?.shouldDetermineEntityLevel()
    }

    private fun shouldCacheGeojsonObjects() {
        this.interactor?.shouldCacheGeojsonObjects()
    }
    //endregion

    //region Display logic
    override fun displayLoadingState() {
        this.post {
            this.progressBar?.visibility = View.VISIBLE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.progressBar?.setProgress(100, true)
            } else {
                this.progressBar?.progress = 100
            }
        }
    }

    override fun displayNotLoadingState() {
        this.post {
            this.progressBar?.visibility = View.GONE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.progressBar?.setProgress(0, true)
            } else {
                this.progressBar?.progress = 0
            }
        }
    }

    override fun displayEntityLevel(viewModel: STPhotoMapModels.EntityZoomLevel.ViewModel) {
        this.post {
            this.entityLevelView?.setTitle(viewModel.titleId)
            this.entityLevelView?.setImage(viewModel.imageResourceId)
            this.entityLevelView?.show()
        }
    }
    //endregion
}