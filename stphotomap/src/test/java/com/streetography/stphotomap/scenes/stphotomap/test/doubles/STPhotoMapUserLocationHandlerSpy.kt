package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import android.content.Context
import android.content.pm.PackageManager
import com.streetography.stphotomap.models.coordinate.Coordinate
import com.streetography.stphotomap.scenes.stphotomap.user_location.STPhotoMapUserLocationHandler
import com.streetography.stphotomap.scenes.stphotomap.user_location.STPhotoMapUserLocationHandlerDelegate

class STPhotoMapUserLocationHandlerSpy(
    context: Context,
    delegate: STPhotoMapUserLocationHandlerDelegate
) : STPhotoMapUserLocationHandler(context, delegate) {
    var permissionStatus: Int = PackageManager.PERMISSION_DENIED
    var requestUserLocationCalled: Boolean = false

    override val locationPermissionStatus: Int
        get() = this.permissionStatus

    override var location: Coordinate? = Coordinate(40.0, 40.0)

    override fun requestUserLocation() {
        this.requestUserLocationCalled = true
    }
}