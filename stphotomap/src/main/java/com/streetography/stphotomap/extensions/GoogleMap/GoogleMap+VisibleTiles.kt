package com.streetography.stphotomap.extensions.GoogleMap

import com.google.android.gms.maps.GoogleMap
import com.streetography.stphotomap.extensions.LatLng.transformCoordinate
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate

fun GoogleMap.visibleTiles(): ArrayList<TileCoordinate> {
    val zoom = Math.round(this.getCameraPosition().zoom)

    val northWestTileCoordinate = this.getProjection().getVisibleRegion().farLeft.transformCoordinate(zoom)
    val southEastTileCoordinate = this.getProjection().getVisibleRegion().nearRight.transformCoordinate(zoom)

    val xMax = northWestTileCoordinate.maxX(southEastTileCoordinate)
    val xMin = northWestTileCoordinate.minX(southEastTileCoordinate)

    val yMax = northWestTileCoordinate.maxY(southEastTileCoordinate)
    val yMin = northWestTileCoordinate.minY(southEastTileCoordinate)

    val visibleTiles: ArrayList<TileCoordinate> = ArrayList()

    for (y in yMin..yMax) {
        for (x in xMin..xMax) {
            visibleTiles.add(TileCoordinate(zoom, x, y))
        }
    }

    return visibleTiles
}