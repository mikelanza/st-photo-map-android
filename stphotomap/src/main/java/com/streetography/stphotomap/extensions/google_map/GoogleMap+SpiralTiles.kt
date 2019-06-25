package com.streetography.stphotomap.extensions.google_map

import com.google.android.gms.maps.GoogleMap
import com.streetography.stphotomap.extensions.visible_region.spiralTile
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate

fun GoogleMap.spiralTiles(): ArrayList<TileCoordinate> {
    val zoom = Math.round(this.getCameraPosition().zoom)
    return this.getProjection().getVisibleRegion().spiralTile(zoom)
}