package com.streetography.stphotomap.scenes.stphotomap.interactor

import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.scenes.stphotomap.builders.STPhotoMapUriBuilder

fun STPhotoMapInteractor.prepareTilesForCaching(): ArrayList<TileCoordinate> {
    return ArrayList(this.visibleTiles.filter {
        val uri = STPhotoMapUriBuilder().geojsonTileUri(it)
        this.cacheHandler.shouldPrepareTileForCaching(uri.first)
    })
}

fun STPhotoMapInteractor.cacheGeojsonObjects(tiles: ArrayList<TileCoordinate>) {
    tiles.forEach { this.cacheGeojsonObjectsFor(it) }
}

fun STPhotoMapInteractor.cacheGeojsonObjectsFor(tile: TileCoordinate) {
    val uri = STPhotoMapUriBuilder().geojsonTileUri(tile)
    this.cacheHandler.addActiveDownload(uri.first)
    this.worker?.getGeojsonTileForCaching(tile, uri.first, uri.second)
}