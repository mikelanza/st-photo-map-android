package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapDisplayLogic
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapModels

class STPhotoMapDisplayLogicSpy: STPhotoMapDisplayLogic {
    var displayLoadingStateCalled: Boolean = false
    var displayNotLoadingStateCalled: Boolean = false
    var displayEntityLevelCalled: Boolean = false
    var displayLocationMarkersCalled: Boolean = false
    var displayRemoveLocationMarkersCalled: Boolean = false
    var displayNavigateToPhotoDetailsCalled: Boolean = false

    override fun displayLoadingState() {
        this.displayLoadingStateCalled = true
    }

    override fun displayNotLoadingState() {
        this.displayNotLoadingStateCalled = true
    }

    override fun displayEntityLevel(viewModel: STPhotoMapModels.EntityZoomLevel.ViewModel) {
        this.displayEntityLevelCalled = true
    }

    override fun displayLocationMarkers(viewModel: STPhotoMapModels.LocationMarkers.ViewModel) {
        this.displayLocationMarkersCalled = true
    }

    override fun displayRemoveLocationMarkers() {
        this.displayRemoveLocationMarkersCalled = true
    }

    override fun displayNavigateToPhotoDetails(viewModel: STPhotoMapModels.PhotoDetailsNavigation.ViewModel) {
        this.displayNavigateToPhotoDetailsCalled = true
    }
}