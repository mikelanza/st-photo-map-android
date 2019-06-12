package com.streetography.stphotomap.extensions.visible_region

import com.google.android.gms.maps.model.VisibleRegion
import com.streetography.stphotomap.extensions.lat_lng.transformCoordinate
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate

fun VisibleRegion.spiralTile(zoom: Int): ArrayList<TileCoordinate> {
    val northWestTileCoordinate = this.farLeft.transformCoordinate(zoom)
    val southEastTileCoordinate = this.nearRight.transformCoordinate(zoom)

    var maxX = northWestTileCoordinate.maxX(southEastTileCoordinate)
    var minX = northWestTileCoordinate.minX(southEastTileCoordinate)

    var maxY = northWestTileCoordinate.maxY(southEastTileCoordinate)
    var minY = northWestTileCoordinate.minY(southEastTileCoordinate)

    val spiralVisibleTiles: ArrayList<TileCoordinate> = ArrayList()

    while (minX <= maxX && minY <= maxY) {
        for (i in minY .. maxY) { spiralVisibleTiles.add(TileCoordinate(zoom, minX, i)) }
        minX += 1
        if (validate(minX, maxX, minY, maxY) == true) {
            for (i in minX .. maxX) { spiralVisibleTiles.add(TileCoordinate(zoom, i, maxY)) }
        }
        maxY -= 1
        if (validate(minX, maxX, minY, maxY) == true) {
            for (i in (minY .. maxY).reversed()) { spiralVisibleTiles.add(TileCoordinate(zoom, maxX, i)) }
        }
        maxX -= 1
        if (validate(minX, maxX, minY, maxY) == true) {
            for (i in (minX .. maxX).reversed()) { spiralVisibleTiles.add(TileCoordinate(zoom, i, minY)) }
        }
        minY += 1
    }

    return ArrayList(spiralVisibleTiles.reversed())
}

private fun validate(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean {
    if (minX > maxX) { return false }
    if (minY > maxY) { return false }
    return true
}
