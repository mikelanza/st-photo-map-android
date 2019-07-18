package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import com.google.android.gms.maps.GoogleMap
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapViewDelegate

class STPhotoMapViewDelegateSpy: STPhotoMapViewDelegate {
    var photoMapViewOnMapReadyCalled: Boolean = false
    var photoMapViewNavigateToPhotoDetailsForPhotoIdCalled: Boolean = false

    override fun photoMapViewOnMapReady(googleMap: GoogleMap) {
        this.photoMapViewOnMapReadyCalled = true
    }

    override fun photoMapViewNavigateToPhotoDetails(photoId: String) {
        this.photoMapViewNavigateToPhotoDetailsForPhotoIdCalled = true
    }
}