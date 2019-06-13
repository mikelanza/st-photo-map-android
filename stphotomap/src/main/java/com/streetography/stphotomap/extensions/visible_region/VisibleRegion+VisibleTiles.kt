package com.streetography.stphotomap.extensions.visible_region

import com.google.android.gms.maps.model.VisibleRegion
import com.streetography.stphotomap.extensions.lat_lng.transformCoordinate
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate

fun VisibleRegion.visibleTiles(zoom: Int): ArrayList<TileCoordinate> {

    val northWestTileCoordinate = this.farLeft.transformCoordinate(zoom)
    val southEastTileCoordinate = this.nearRight.transformCoordinate(zoom)

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