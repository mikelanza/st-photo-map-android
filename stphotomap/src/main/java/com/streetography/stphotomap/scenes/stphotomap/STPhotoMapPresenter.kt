package com.streetography.stphotomap.scenes.stphotomap

import com.streetography.stphotomap.R
import com.streetography.stphotomap.models.entity_level.EntityLevel
import java.lang.ref.WeakReference

interface STPhotoMapPresentationLogic {
    fun presentLoadingState()
    fun presentNotLoadingState()

    fun presentEntityLevel(response: STPhotoMapModels.EntityZoomLevel.Response)
    fun presentLocationMarkers(response: STPhotoMapModels.LocationMarkers.Response)
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
}