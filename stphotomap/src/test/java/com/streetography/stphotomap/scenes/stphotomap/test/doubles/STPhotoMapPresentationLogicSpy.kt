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
    var presentNavigateToPhotoCollectionCalled: Boolean = false

    var presentLocationOverlayCalled: Boolean = false
    var presentRemoveLocationOverlayCalled: Boolean = false

    var presentZoomToCoordinateCalled: Boolean = false
    var presentCenterToCoordinateCalled: Boolean = false

    var presentRequestLocationPermissionsCalled: Boolean = false
    var presentLocationAccessDeniedAlertCalled: Boolean = false

    var presentOpenDataSourcesLinkCalled: Boolean = false
    var presentOpenSettingsApplicationCalled: Boolean = false

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

    override fun presentNavigateToPhotoCollection(response: STPhotoMapModels.PhotoCollectionNavigation.Response) {
        this.presentNavigateToPhotoCollectionCalled = true
    }

    override fun presentLocationOverlay(response: STPhotoMapModels.LocationOverlay.Response) {
        this.presentLocationOverlayCalled = true
    }

    override fun presentRemoveLocationOverlay() {
        this.presentRemoveLocationOverlayCalled = true
    }

    override fun presentZoomToCoordinate(response: STPhotoMapModels.CoordinateZoom.Response) {
        this.presentZoomToCoordinateCalled = true
    }

    override fun presentCenterToCoordinate(response: STPhotoMapModels.CoordinateCenter.Response) {
        this.presentCenterToCoordinateCalled = true
    }

    override fun presentRequestLocationPermissions() {
        this.presentRequestLocationPermissionsCalled = true
    }

    override fun presentLocationAccessDeniedAlert() {
        this.presentLocationAccessDeniedAlertCalled = true
    }

    override fun presentOpenDataSourcesLink() {
        this.presentOpenDataSourcesLinkCalled = true
    }

    override fun presentOpenSettingsApplication() {
        this.presentOpenSettingsApplicationCalled = true
    }
}