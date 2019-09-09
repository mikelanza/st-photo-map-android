package com.streetography.stphotomap.scenes.stphotomap.user_location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.streetography.stphotomap.models.coordinate.Coordinate
import java.lang.ref.WeakReference

interface STPhotoMapUserLocationHandlerDelegate {
    fun userLocationHandler(handler: STPhotoMapUserLocationHandler?, centerToCoordinate: Coordinate)
}

open class STPhotoMapUserLocationHandler(context: Context, delegate: STPhotoMapUserLocationHandlerDelegate) {
    val context: WeakReference<Context>?
    val delegate: WeakReference<STPhotoMapUserLocationHandlerDelegate>?
    val locationClient: FusedLocationProviderClient?

    open val locationPermissionStatus: Int
        get() {
            return this.context?.get()?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } ?: PackageManager.PERMISSION_DENIED
        }

    open var location: Coordinate? = null

    init {
        this.context = WeakReference(context)
        this.delegate = WeakReference(delegate)
        this.locationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    open fun requestUserLocation() {
        this.location?.let {
            this.sendUserLocationToDelegate(it)
        } ?: kotlin.run {
            this.retrieveLastLocation()
        }
    }

    fun retrieveLastLocation() {
        this.locationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
            location?.let {
                val coordinate = Coordinate(it.longitude, it.latitude)
                this.location = coordinate
                this.sendUserLocationToDelegate(coordinate)
            }
        }
    }

    fun sendUserLocationToDelegate(location: Coordinate) {
        this.delegate?.get()?.userLocationHandler(this, centerToCoordinate = location)
    }
}