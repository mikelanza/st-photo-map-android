package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapModels
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapPresentationLogic

class STPhotoMapPresentationLogicSpy: STPhotoMapPresentationLogic {
    var presentLoadingStateCalled: Boolean = false
    var presentNotLoadingStateCalled: Boolean = false

    var presentEntityLevelCalled: Boolean = false
    var presentLocationMarkersCalled: Boolean = false
    var presentRemoveLocationMarkers: Boolean = false

    var presentNavigateToPhotoDetailsCalled: Boolean = false

    override fun presentLoadingState() {
        this.presentLoadingStateCalled = true
    }

    override fun presentNotLoadingState() {
        this.presentNotLoadingStateCalled = true
    }

    override fun presentEntityLevel(response: STPhotoMapModels.EntityZoomLevel.Response) {
        this.presentEntityLevelCalled = true
    }

    override fun presentLocationMarkers(response: STPhotoMapModels.LocationMarkers.Response) {
        this.presentLocationMarkersCalled = true
    }

    override fun presentRemoveLocationMarkers() {
        this.presentRemoveLocationMarkers = true
    }

    override fun presentNavigateToPhotoDetails(response: STPhotoMapModels.PhotoDetailsNavigation.Response) {
        this.presentNavigateToPhotoDetailsCalled = true
    }
}