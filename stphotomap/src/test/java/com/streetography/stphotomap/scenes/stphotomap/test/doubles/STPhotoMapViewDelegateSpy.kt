package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import com.google.android.gms.maps.GoogleMap
import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.location.STLocation
import com.streetography.stphotomap.scenes.stphotomap.photo_map_view.STPhotoMapViewDelegate

class STPhotoMapViewDelegateSpy:
    STPhotoMapViewDelegate {
    var photoMapViewOnMapReadyCalled: Boolean = false
    var photoMapViewNavigateToPhotoDetailsForPhotoIdCalled: Boolean = false
    var photoMapViewNavigateToPhotoCollectionCalled: Boolean = false

    override fun photoMapViewOnMapReady(googleMap: GoogleMap) {
        this.photoMapViewOnMapReadyCalled = true
    }

    override fun photoMapViewNavigateToPhotoDetails(photoId: String) {
        this.photoMapViewNavigateToPhotoDetailsForPhotoIdCalled = true
    }

    override fun photoMapViewNavigateToPhotoCollection(
        location: STLocation,
        entityLevel: EntityLevel,
        userId: String?,
        collectionId: String?
    ) {
        this.photoMapViewNavigateToPhotoCollectionCalled = true
    }
}