package com.streetography.stphotomap.extensions.visible_region

import com.google.android.gms.maps.model.VisibleRegion
import com.streetography.stphotomap.models.geojson.BoundingBox
import com.streetography.stphotomap.models.geojson.BoundingCoordinates


fun VisibleRegion.boundingBox(): BoundingBox {
    val minLongitude: Double = this.latLngBounds.southwest.longitude
    val minLatitude: Double = this.latLngBounds.southwest.latitude
    val maxLongitude: Double = this.latLngBounds.northeast.longitude
    val maxLatitude: Double = this.latLngBounds.northeast.latitude

    val boundingCoordinates = BoundingCoordinates(minLongitude, minLatitude, maxLongitude, maxLatitude)
    return BoundingBox(boundingCoordinates)
}