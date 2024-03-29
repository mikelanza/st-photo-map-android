package com.streetography.stphotomap.scenes.stphotomap

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.TileOverlay
import com.google.android.gms.maps.model.TileOverlayOptions
import com.streetography.stphotomap.R
import com.streetography.stphotomap.extensions.google_map.visibleTiles
import com.streetography.stphotomap.extensions.view.transformDimension
import com.streetography.stphotomap.extensions.visible_region.boundingBox
import com.streetography.stphotomap.models.coordinate.Coordinate
import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.location.STLocation
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.scenes.stphotomap.interactor.STPhotoMapBusinessLogic
import com.streetography.stphotomap.scenes.stphotomap.interactor.STPhotoMapInteractor
import com.streetography.stphotomap.scenes.stphotomap.location_level.STPhotoMapMarkerHandler
import com.streetography.stphotomap.scenes.stphotomap.location_level.STPhotoMapMarkerHandlerDelegate
import com.streetography.stphotomap.scenes.stphotomap.tiles.STPhotoTileProvider
import com.streetography.stphotomap.scenes.stphotomap.tiles.STPhotoUrlTileProvider
import com.streetography.stphotomap.scenes.stphotomap.views.STEntityLevelView
import com.streetography.stphotomap.scenes.stphotomap.views.STLocationOverlayView
import java.lang.ref.WeakReference

interface STPhotoMapViewDelegate {
    fun photoMapViewOnMapReady(googleMap: GoogleMap)
    fun photoMapViewNavigateToPhotoDetails(photoId: String)
    fun photoMapViewNavigateToPhotoCollection(location: STLocation, entityLevel: EntityLevel, userId: String?, collectionId: String?)
}

interface STPhotoMapDisplayLogic {
    fun displayLoadingState()
    fun displayNotLoadingState()

    fun displayEntityLevel(viewModel: STPhotoMapModels.EntityZoomLevel.ViewModel)
    fun displayLocationMarkers(viewModel: STPhotoMapModels.LocationMarkers.ViewModel)
    fun displayRemoveLocationMarkers()

    fun displayNavigateToPhotoDetails(viewModel: STPhotoMapModels.PhotoDetailsNavigation.ViewModel)
    fun displayNavigateToPhotoCollection(viewModel: STPhotoMapModels.PhotoCollectionNavigation.ViewModel)

    fun displayLocationOverlay(viewModel: STPhotoMapModels.LocationOverlay.ViewModel)
    fun displayRemoveLocationOverlay()

    fun displayZoomToCoordinate(viewModel: STPhotoMapModels.CoordinateZoom.ViewModel)
    fun displayCenterToCoordinate(viewModel: STPhotoMapModels.CoordinateCenter.ViewModel)

    fun displayRequestLocationPermissions()
    fun displayLocationAccessDeniedAlert(viewModel: STPhotoMapModels.LocationAccessDeniedAlert.ViewModel)

    fun displayStartIntent(viewModel: STPhotoMapModels.IntentStart.ViewModel)
}

public open class STPhotoMapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): RelativeLayout(context, attrs, defStyleAttr),
    STPhotoMapDisplayLogic, OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnMapClickListener,
    STPhotoMapMarkerHandlerDelegate {
    public val FINE_LOCATION_REQUEST_CODE: Int = 2019

    var interactor: STPhotoMapBusinessLogic? = null

    var entityLevelView: STEntityLevelView? = null
    var mapView: GoogleMap? = null
    var progressBar: ProgressBar? = null
    var locationOverlayView: STLocationOverlayView? = null
    var userLocationButton: ImageButton? = null
    var dataSourcesButton: Button? = null

    var markerHandler: STPhotoMapMarkerHandler? = null

    public var delegate: STPhotoMapViewDelegate? = null
    public var activity: WeakReference<Activity>? = null

    private var tileOverlay: TileOverlay? = null

    init {
        this.setup()
        this.setupSubviews()
        this.addSubviews()
    }

    public fun requestUserLocation() {
        this.interactor?.shouldRequestUserLocation()
    }

    public fun clearTileCache() {
        this.tileOverlay?.clearTileCache()
    }

    private fun setup() {
        val interactor = STPhotoMapInteractor(this.context)
        val presenter = STPhotoMapPresenter()

        presenter.displayer = WeakReference(this)
        interactor.presenter = presenter

        this.interactor = interactor
    }

    private fun setupMarkerHandler() {
        this.markerHandler = STPhotoMapMarkerHandler(this.context, this.mapView)
        this.markerHandler?.delegate = this
    }

    //region Subviews configuration
    private fun setupSubviews() {
        this.setupMapView()
        this.setupProgressBar()
        this.setupEntityView()
        this.setupLocationOverlayView()
        this.setupUserLocationButton()
        this.setupDataSourcesButton()
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
        this.mapView?.setOnMapClickListener(this)
    }

    private fun setupProgressBar() {
        this.progressBar = ProgressBar(this.context, null, android.R.attr.progressBarStyleHorizontal)
        this.progressBar?.id = View.generateViewId()
        this.progressBar?.progress = 0
        this.progressBar?.max = 100
        this.progressBar?.visibility = View.GONE
        this.progressBar?.isIndeterminate = false
        val drawable = this.progressBar?.progressDrawable?.mutate()
        drawable?.setColorFilter(Color.argb(255, 65, 171, 255), PorterDuff.Mode.SRC_IN)
        this.progressBar?.progressDrawable = drawable
    }

    private fun setupEntityView() {
        this.entityLevelView = STEntityLevelView(this.context, null)
        this.entityLevelView?.id = View.generateViewId()
        this.entityLevelView?.visibility = View.GONE
        this.entityLevelView?.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun setupLocationOverlayView() {
        this.locationOverlayView = STLocationOverlayView(this.context)
        this.locationOverlayView?.id = View.generateViewId()
        this.locationOverlayView?.visibility = View.GONE
    }

    private fun setupUserLocationButton() {
        this.userLocationButton = ImageButton(this.context)
        this.userLocationButton?.id = View.generateViewId()
        this.userLocationButton?.background = null
        this.userLocationButton?.setImageResource(R.drawable.st_current_user_location)
        this.userLocationButton?.setOnClickListener { this.didClickUserLocationButton() }
    }

    fun didClickUserLocationButton() {
        this.interactor?.shouldAskForLocationPermissions()
    }

    private fun setupDataSourcesButton() {
        this.dataSourcesButton = Button(this.context)
        this.dataSourcesButton?.id = View.generateViewId()
        this.dataSourcesButton?.background = null
        this.dataSourcesButton?.isAllCaps = false
        this.dataSourcesButton?.setText(R.string.st_photo_map_data_sources_title)
        this.dataSourcesButton?.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        this.dataSourcesButton?.setTextColor(Color.DKGRAY)
        this.dataSourcesButton?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9F)
        this.dataSourcesButton?.setOnClickListener { this.didClickDataSourcesButton() }
    }

    fun didClickDataSourcesButton() {
        this.interactor?.shouldOpenDataSourcesLink()
    }
    //endregion

    //region Subviews addition
    private fun addSubviews() {
        this.addProgressBar()
        this.addEntityView()
        this.addLocationOverlayView()
        this.addUserLocationButton()
        this.addDataSourcesButton()
    }

    private fun addProgressBar() {
        val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics)
        val layoutParameters = LayoutParams(LayoutParams.MATCH_PARENT, height.toInt())

        layoutParameters.addRule(ALIGN_PARENT_TOP)
        this.addView(this.progressBar, layoutParameters)
    }

    private fun addEntityView() {
        val layoutParameters = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParameters.addRule(CENTER_IN_PARENT)
        this.addView(this.entityLevelView, layoutParameters)
    }

    private fun addLocationOverlayView() {
        val layoutParameters = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val marginLeft = this.transformDimension(15F).toInt()
        val marginBottom = this.transformDimension(30F).toInt()
        val marginRight = this.transformDimension(75F).toInt()
        layoutParameters.setMargins(marginLeft, 0, marginRight, marginBottom)
        layoutParameters.addRule(ALIGN_PARENT_BOTTOM)
        this.addView(this.locationOverlayView, layoutParameters)
    }

    private fun addUserLocationButton() {
        val layoutParameters = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val marginBottom = this.transformDimension(5F).toInt()
        val marginRight = this.transformDimension(5F).toInt()
        layoutParameters.setMargins(0, 0, 0, 0)
        layoutParameters.addRule(ALIGN_PARENT_BOTTOM)
        layoutParameters.addRule(ALIGN_PARENT_END)
        this.addView(this.userLocationButton, layoutParameters)
    }

    private fun addDataSourcesButton() {
        val layoutParameters = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val marginRight = this.transformDimension(5F).toInt()
        layoutParameters.setMargins(0, 0, 0, 0)
        layoutParameters.addRule(ALIGN_PARENT_BOTTOM)
        this.userLocationButton?.id?.let { layoutParameters.addRule(START_OF, it) }
        this.addView(this.dataSourcesButton, layoutParameters)
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
        tileOverlay = this.mapView?.addTileOverlay(getTileOverlayOptions())

        this.setupGoogleMapStyle()
        this.setupMapViewListeners()
        this.setupMarkerHandler()

        this.delegate?.photoMapViewOnMapReady(googleMap)
    }

    override fun onCameraIdle() {
        this.shouldUpdateVisibleTiles()
        this.shouldCacheGeojsonObjects()
        this.shouldDetermineEntityLevel()
        this.shouldDetermineLocationLevel()
    }

    override fun onCameraMove() {
        this.interactor?.shouldUpdateBoundingBox(STPhotoMapModels.UpdateBoundingBox.Request(this.mapView?.projection?.visibleRegion?.boundingBox()))
    }

    override fun onMapClick(latLng: LatLng?) {
        this.interactor?.shouldNavigateToPhotoCollection(STPhotoMapModels.PhotoCollectionNavigation.Request(latLng));
    }
    //endregion

    //region Tile overlay & provider
    private fun getTileOverlayOptions(): TileOverlayOptions {
        val tileProvider = STPhotoTileProvider(STPhotoUrlTileProvider(256, 256))
        val options = TileOverlayOptions()
        options.tileProvider(tileProvider)
        options.transparency(0.0.toFloat())
        return options
    }
    //endregion

    //region Map logic
    fun moveMapCameraTo(latLng: LatLng) {
       this.post {
           mapView?.moveCamera(
               CameraUpdateFactory.newLatLng(latLng)
           )
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

    private fun shouldDetermineLocationLevel() {
        this.interactor?.shouldDetermineLocationLevel()
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

    override fun displayLocationMarkers(viewModel: STPhotoMapModels.LocationMarkers.ViewModel) {
        this.post {
            this.markerHandler?.addMarkers(viewModel.markers)
        }
    }

    override fun displayRemoveLocationMarkers() {
        this.post {
            this.markerHandler?.removeAllMarkers()
        }
    }

    override fun displayNavigateToPhotoDetails(viewModel: STPhotoMapModels.PhotoDetailsNavigation.ViewModel) {
        this.delegate?.photoMapViewNavigateToPhotoDetails(viewModel.photoId)
    }

    override fun displayNavigateToPhotoCollection(viewModel: STPhotoMapModels.PhotoCollectionNavigation.ViewModel) {
        this.delegate?.photoMapViewNavigateToPhotoCollection(viewModel.location, viewModel.entityLevel, viewModel.userId, viewModel.collectionId);
    }

    override fun displayLocationOverlay(viewModel: STPhotoMapModels.LocationOverlay.ViewModel) {
        this.post {
            this.locationOverlayView?.model = STLocationOverlayView.Model(viewModel.photoId, viewModel.title, viewModel.time, viewModel.description)
            this.locationOverlayView?.visibility = View.VISIBLE
        }
    }

    override fun displayRemoveLocationOverlay() {
        this.post {
            this.locationOverlayView?.visibility = View.GONE
            this.locationOverlayView?.model = null
        }
    }

    override fun displayZoomToCoordinate(viewModel: STPhotoMapModels.CoordinateZoom.ViewModel) {
        this.post {
            this.mapView?.let {
                val zoom = it.cameraPosition.zoom + 1F
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(viewModel.coordinate.latitude, viewModel.coordinate.longitude), zoom))
            }
        }
    }

    override fun displayCenterToCoordinate(viewModel: STPhotoMapModels.CoordinateCenter.ViewModel) {
        this.post {
            this.mapView?.animateCamera(CameraUpdateFactory.newLatLngZoom(viewModel.coordinate.locationCoordinate, viewModel.zoom))
        }
    }

    override fun displayRequestLocationPermissions() {
        this.post {
            this.activity?.get()?.let {
                ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION_REQUEST_CODE)
            }
        }
    }

    override fun displayLocationAccessDeniedAlert(viewModel: STPhotoMapModels.LocationAccessDeniedAlert.ViewModel) {
        this.post {
            val dialogBuilder = AlertDialog.Builder(this.context)
            dialogBuilder.setMessage(viewModel.messageId)
            dialogBuilder.setPositiveButton(viewModel.settingsTitleId) { dialog, _ -> this.interactor?.shouldOpenSettingsApplication() }
            dialogBuilder.setNegativeButton(viewModel.cancelTitleId) { dialog, _ -> dialog.cancel() }
            dialogBuilder.create().show()
        }
    }

    override fun displayStartIntent(viewModel: STPhotoMapModels.IntentStart.ViewModel) {
        this.post {
            this.activity?.get()?.let {
                if (viewModel.intent.resolveActivity(it.packageManager) != null) {
                    it.startActivity(viewModel.intent)
                }
            }
        }
    }
    //endregion

    //region Marker handler delegate
    override fun photoMapMarkerHandlerDidReselectPhoto(photoId: String) {
        this.interactor?.shouldNavigateToPhotoDetails(STPhotoMapModels.PhotoDetailsNavigation.Request(photoId))
    }

    override fun photoMapMarkerHandlerDidSelectPhoto(photoId: String) {
//        this.interactor?.shouldGetPhotoDetailsForPhotoMarker(STPhotoMapModels.PhotoDetails.Request(photoId))
        this.interactor?.shouldNavigateToPhotoDetails(STPhotoMapModels.PhotoDetailsNavigation.Request(photoId))
    }

    override fun photoMapMarkerHandlerZoomToCoordinate(coordinate: Coordinate) {
        this.interactor?.shouldZoomToCoordinate(STPhotoMapModels.CoordinateZoom.Request(coordinate))
    }

    override fun photoMapMarkerHandlerNavigateToSpecificPhotos(photoIds: ArrayList<String>) {

    }
    //endregion
}