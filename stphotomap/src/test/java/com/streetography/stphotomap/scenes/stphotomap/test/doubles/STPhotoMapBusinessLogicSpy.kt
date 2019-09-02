package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapModels
import com.streetography.stphotomap.scenes.stphotomap.interactor.STPhotoMapBusinessLogic

class STPhotoMapBusinessLogicSpy: STPhotoMapBusinessLogic {
    var shouldUpdateVisibleTilesCalled: Boolean = false
    var shouldDetermineEntityLevelCalled: Boolean = false
    var shouldCacheGeojsonObjectsCalled: Boolean = false
    var shouldUpdateBoundingBoxCalled: Boolean = false
    var shouldDetermineLocationLevelCalled: Boolean = false
    var shouldNavigateToPhotoDetailsCalled: Boolean = false
    var shouldNavigateToPhotoCollectionCalled: Boolean = false
    var shouldGetPhotoDetailsForPhotoMarkerCalled: Boolean = false
    var shouldZoomToCoordinateCalled: Boolean = false

    override fun shouldUpdateVisibleTiles(request: STPhotoMapModels.VisibleTiles.Request) {
        this.shouldUpdateVisibleTilesCalled = true
    }

    override fun shouldDetermineEntityLevel() {
        this.shouldDetermineEntityLevelCalled = true
    }

    override fun shouldCacheGeojsonObjects() {
        this.shouldCacheGeojsonObjectsCalled = true
    }

    override fun shouldUpdateBoundingBox(request: STPhotoMapModels.UpdateBoundingBox.Request) {
        this.shouldUpdateBoundingBoxCalled = true
    }

    override fun shouldDetermineLocationLevel() {
        this.shouldDetermineLocationLevelCalled = true
    }

    override fun shouldNavigateToPhotoDetails(request: STPhotoMapModels.PhotoDetailsNavigation.Request) {
        this.shouldNavigateToPhotoDetailsCalled = true
    }

    override fun shouldNavigateToPhotoCollection(request: STPhotoMapModels.PhotoCollectionNavigation.Request) {
        this.shouldNavigateToPhotoCollectionCalled = true;
    }

    override fun shouldGetPhotoDetailsForPhotoMarker(request: STPhotoMapModels.PhotoDetails.Request) {
        this.shouldGetPhotoDetailsForPhotoMarkerCalled = true
    }

    override fun shouldZoomToCoordinate(request: STPhotoMapModels.CoordinateZoom.Request) {
        this.shouldZoomToCoordinateCalled = true
    }
}