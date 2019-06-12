package com.streetography.stphotomap.extensions.LatLng

import com.google.android.gms.maps.model.LatLng
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate


fun LatLng.transformCoordinate(zoom: Int): TileCoordinate {
    val tileX = Math.floor((longitude + 180) / 360.0 * Math.pow(2.0, zoom.toDouble())).toInt()
    val tileY = Math.floor(
        (1 - Math.log(Math.tan(latitude * Math.PI / 180.0) + 1 / Math.cos(latitude * Math.PI / 180.0)) / Math.PI) / 2 * Math.pow(
            2.0,
            zoom.toDouble()
        )
    ).toInt()
    return TileCoordinate(zoom, tileX, tileY)
}