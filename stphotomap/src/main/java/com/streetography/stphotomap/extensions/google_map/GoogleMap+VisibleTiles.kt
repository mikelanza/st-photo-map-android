package com.streetography.stphotomap.extensions.google_map

import com.google.android.gms.maps.GoogleMap
import com.streetography.stphotomap.extensions.visible_region.visibleTiles
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate

fun GoogleMap.visibleTiles(): ArrayList<TileCoordinate> {
    val zoom = Math.round(this.getCameraPosition().zoom)
    return this.getProjection().getVisibleRegion().visibleTiles(zoom)
}