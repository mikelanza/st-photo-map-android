package com.streetography.stphotomap.models.coordinate

import com.google.android.gms.maps.model.LatLng

import java.io.Serializable

interface GeodesicPoint {
    val longitude: Double
    val latitude: Double

    val locationCoordinate: LatLng
}

class Coordinate(override var longitude: Double, override var latitude: Double) : GeodesicPoint, Serializable {
    val description: String
        get() = "Coordinate: (longitude: " + longitude.toString() + ", latitude: " + latitude.toString()

    override val locationCoordinate: LatLng
        get() = LatLng(latitude, longitude)

    companion object {
        fun fromLatLng(latLng: LatLng): Coordinate {
            return Coordinate(latLng.longitude, latLng.latitude)
        }
    }
}
