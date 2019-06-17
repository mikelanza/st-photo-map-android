package com.streetography.stphotomap.scenes.stphotomap

import com.streetography.stphotomap.R
import com.streetography.stphotomap.models.entity_level.EntityLevel
import java.lang.ref.WeakReference

interface STPhotoMapPresentationLogic {
    fun presentLoadingState()
    fun presentNotLoadingState()

    fun presentEntityLevel(response: STPhotoMapModels.EntityZoomLevel.Response)
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
        when (entityLevel) {
            EntityLevel.location -> return R.string.st_photo_map_location_level_title
            EntityLevel.block -> return R.string.st_photo_map_block_level_title
            EntityLevel.neighborhood -> return R.string.st_photo_map_neighborhood_level_title
            EntityLevel.city -> return R.string.st_photo_map_city_level_title
            EntityLevel.county -> return R.string.st_photo_map_county_level_title
            EntityLevel.state -> return R.string.st_photo_map_state_level_title
            EntityLevel.country -> return R.string.st_photo_map_country_level_title
            else -> return 0
        }
    }

    private fun imageResourceIdForEntityLevel(entityLevel: EntityLevel): Int {
        when (entityLevel) {
            EntityLevel.location -> return R.drawable.st_entity_level_location
            EntityLevel.block -> return R.drawable.st_entity_level_block
            EntityLevel.neighborhood -> return R.drawable.st_entity_level_neighborhood
            EntityLevel.city -> return R.drawable.st_entity_level_city
            EntityLevel.county -> return R.drawable.st_entity_level_county
            EntityLevel.state -> return R.drawable.st_entity_level_state
            EntityLevel.country -> return R.drawable.st_entity_level_country
            else -> return 0
        }
    }
}