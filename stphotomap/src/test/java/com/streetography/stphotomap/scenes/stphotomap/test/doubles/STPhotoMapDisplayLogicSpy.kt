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
    var displayNavigateToPhotoCollectionCalled: Boolean = false

    var displayLocationOverlayCalled: Boolean = false
    var displayRemoveLocationOverlayCalled: Boolean = false

    var displayZoomToCoordinateCalled: Boolean = false

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

    override fun displayNavigateToPhotoCollection(viewModel: STPhotoMapModels.PhotoCollectionNavigation.ViewModel) {
        this.displayNavigateToPhotoCollectionCalled = true;
    }

    override fun displayLocationOverlay(viewModel: STPhotoMapModels.LocationOverlay.ViewModel) {
        this.displayLocationOverlayCalled = true
    }

    override fun displayRemoveLocationOverlay() {
        this.displayRemoveLocationOverlayCalled = true
    }

    override fun displayZoomToCoordinate(viewModel: STPhotoMapModels.CoordinateZoom.ViewModel) {
        this.displayZoomToCoordinateCalled = true
    }
}