package com.streetography.stphotomap.scenes.stphotomap

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.streetography.stphotomap.R
import com.streetography.stphotomap.models.entity_level.EntityLevel
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat

interface STPhotoMapPresentationLogic {
    fun presentLoadingState()
    fun presentNotLoadingState()

    fun presentEntityLevel(response: STPhotoMapModels.EntityZoomLevel.Response)
    fun presentLocationMarkers(response: STPhotoMapModels.LocationMarkers.Response)
    fun presentRemoveLocationMarkers()

    fun presentNavigateToPhotoDetails(response: STPhotoMapModels.PhotoDetailsNavigation.Response)
    fun presentNavigateToPhotoCollection(response: STPhotoMapModels.PhotoCollectionNavigation.Response)

    fun presentLocationOverlay(response: STPhotoMapModels.LocationOverlay.Response)
    fun presentRemoveLocationOverlay()

    fun presentZoomToCoordinate(response: STPhotoMapModels.CoordinateZoom.Response)
    fun presentCenterToCoordinate(response: STPhotoMapModels.CoordinateCenter.Response)

    fun presentRequestLocationPermissions()
    fun presentLocationAccessDeniedAlert()

    fun presentOpenDataSourcesLink()
    fun presentOpenSettingsApplication()
}

class STPhotoMapPresenter: STPhotoMapPresentationLogic {
    var displayer: WeakReference<STPhotoMapDisplayLogic>? = null

    override fun presentLoadingState() {
        this.displayer?.get()?.displayLoadingState()
    }

    override fun presentNotLoadingState() {
        this.displayer?.get()?.displayNotLoadingState()
    }

    override fun presentEntityLevel(response: STPhotoMapModels.EntityZoomLevel.Response) {
        val titleId = titleStringIdForEntityLevel(response.entityLevel)
        val imageResourceId = imageResourceIdForEntityLevel(response.entityLevel)
        this.displayer?.get()?.displayEntityLevel(STPhotoMapModels.EntityZoomLevel.ViewModel(titleId, imageResourceId))
    }

    private fun titleStringIdForEntityLevel(entityLevel: EntityLevel): Int {
        return when (entityLevel) {
            EntityLevel.location -> R.string.st_photo_map_location_level_title
            EntityLevel.block -> R.string.st_photo_map_block_level_title
            EntityLevel.neighborhood -> R.string.st_photo_map_neighborhood_level_title
            EntityLevel.city -> R.string.st_photo_map_city_level_title
            EntityLevel.county -> R.string.st_photo_map_county_level_title
            EntityLevel.state -> R.string.st_photo_map_state_level_title
            EntityLevel.country -> R.string.st_photo_map_country_level_title
            else -> 0
        }
    }

    private fun imageResourceIdForEntityLevel(entityLevel: EntityLevel): Int {
        return when (entityLevel) {
            EntityLevel.location -> R.drawable.st_entity_level_location
            EntityLevel.block -> R.drawable.st_entity_level_block
            EntityLevel.neighborhood -> R.drawable.st_entity_level_neighborhood
            EntityLevel.city -> R.drawable.st_entity_level_city
            EntityLevel.county -> R.drawable.st_entity_level_county
            EntityLevel.state -> R.drawable.st_entity_level_state
            EntityLevel.country -> R.drawable.st_entity_level_country
            else -> 0
        }
    }

    override fun presentLocationMarkers(response: STPhotoMapModels.LocationMarkers.Response) {
        val markers = response.markers.map {
            it.toPhotoMarker()
        }
        this.displayer?.get()?.displayLocationMarkers(STPhotoMapModels.LocationMarkers.ViewModel(ArrayList(markers)))
    }

    override fun presentRemoveLocationMarkers() {
        this.displayer?.get()?.displayRemoveLocationMarkers()
    }

    override fun presentNavigateToPhotoDetails(response: STPhotoMapModels.PhotoDetailsNavigation.Response) {
        this.displayer?.get()?.displayNavigateToPhotoDetails(STPhotoMapModels.PhotoDetailsNavigation.ViewModel(response.photoId))
    }

    override fun presentNavigateToPhotoCollection(response: STPhotoMapModels.PhotoCollectionNavigation.Response) {
        this.displayer?.get()?.displayNavigateToPhotoCollection(STPhotoMapModels.PhotoCollectionNavigation.ViewModel(response.location, response.entityLevel, response.userId, response.collectionId))
    }

    override fun presentLocationOverlay(response: STPhotoMapModels.LocationOverlay.Response) {
        val photoId = response.photo.id
        val title = response.photo.user?.name ?: response.photo.fhUsername
        val time = SimpleDateFormat("MMMM dd, yyyy").format(response.photo.createdAt)
        val description = response.photo.text
        this.displayer?.get()?.displayLocationOverlay(STPhotoMapModels.LocationOverlay.ViewModel(photoId, title, time, description))
    }

    override fun presentRemoveLocationOverlay() {
        this.displayer?.get()?.displayRemoveLocationOverlay()
    }

    override fun presentZoomToCoordinate(response: STPhotoMapModels.CoordinateZoom.Response) {
        this.displayer?.get()?.displayZoomToCoordinate(STPhotoMapModels.CoordinateZoom.ViewModel(response.coordinate))
    }

    override fun presentCenterToCoordinate(response: STPhotoMapModels.CoordinateCenter.Response) {
        val zoom: Float = this.zoomForEntityLevel(response.entityLevel)
        this.displayer?.get()?.displayCenterToCoordinate(STPhotoMapModels.CoordinateCenter.ViewModel(response.coordinate, zoom))
    }

    private fun zoomForEntityLevel(level: EntityLevel): Float {
        when (level) {
            EntityLevel.block -> return 14F
            EntityLevel.neighborhood -> return 12F
            EntityLevel.city -> return 10F
            EntityLevel.county -> return 8F
            EntityLevel.state -> return 6F
            EntityLevel.country -> return 4F
            else -> return 2F
        }
    }

    override fun presentRequestLocationPermissions() {
        this.displayer?.get()?.displayRequestLocationPermissions()
    }

    override fun presentLocationAccessDeniedAlert() {
        val messageId = R.string.st_photo_map_location_access_denied_message
        val settingsTitleId = R.string.st_photo_map_location_access_denied_settings
        val cancelTitleId = R.string.st_photo_map_location_access_denied_cancel
        this.displayer?.get()?.displayLocationAccessDeniedAlert(STPhotoMapModels.LocationAccessDeniedAlert.ViewModel(messageId, cancelTitleId, settingsTitleId))
    }

    override fun presentOpenDataSourcesLink() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.streetography.com/data-licensing"))
        this.displayer?.get()?.displayStartIntent(STPhotoMapModels.IntentStart.ViewModel(intent))
    }

    override fun presentOpenSettingsApplication() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        this.displayer?.get()?.displayStartIntent(STPhotoMapModels.IntentStart.ViewModel(intent))
    }
}