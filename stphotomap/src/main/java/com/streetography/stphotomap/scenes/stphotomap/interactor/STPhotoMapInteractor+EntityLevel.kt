package com.streetography.stphotomap.scenes.stphotomap.interactor

import com.streetography.stphotomap.extensions.geojson_object.entityLevel
import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.scenes.stphotomap.builders.STPhotoMapUriBuilder
import com.streetography.stphotomap.scenes.stphotomap.cache.STPhotoMapGeojsonCache

fun STPhotoMapInteractor.calculateEntityLevelFor(cachedTiles: ArrayList<STPhotoMapGeojsonCache.Tile>) {
    val entityLevel = cachedTiles.firstOrNull()?.geojsonObject?.entityLevel
    entityLevel?.let {
        this.entityLevelHandler.change(entityLevel)
    }
}

fun STPhotoMapInteractor.prepareTilesForEntityLevel(): ArrayList<TileCoordinate> {
    val filteredList = this.visibleTiles.filter { tile ->
        val uri = STPhotoMapUriBuilder().geojsonTileUri(tile)
        !this.entityLevelHandler.hasActiveDownload(uri.first)
    }
    return ArrayList(filteredList)
}

fun STPhotoMapInteractor.entityLevelGeojsonObjectsFor(tiles: ArrayList<TileCoordinate>) {
    tiles.forEach({ this.entityLevelGeojsonObjectsFor(it) })
}

fun STPhotoMapInteractor.entityLevelGeojsonObjectsFor(tile: TileCoordinate) {
    val uri = STPhotoMapUriBuilder().geojsonTileUri(tile)
    this.entityLevelHandler.addActiveDownload(uri.first)
    this.worker?.getGeojsonEntityLevel(tile, uri.first, uri.second)
}

fun STPhotoMapInteractor.isStillTileVisible(tileCoordinate: TileCoordinate): Boolean {
    return  this.visibleTiles.contains(tileCoordinate)
}

fun STPhotoMapInteractor.didGetGeojsonTileForEntityLevel(tileCoordinate: TileCoordinate, geojsonObject: GeoJSONObject) {
    if (this.isStillTileVisible(tileCoordinate) && geojsonObject.entityLevel != EntityLevel.unknown ) {
        this.entityLevelHandler.change(geojsonObject.entityLevel)
        this.worker?.cancelAllGeojsonEntityLevelOperations()
    }
    this.handleLoadingStateForEntityLevel()
}

fun STPhotoMapInteractor.handleLoadingStateForEntityLevel() {
    if (this.entityLevelHandler.activeDownloads.size > 0) {
        this.presenter?.presentLoadingState()
    } else {
        this.presenter?.presentNotLoadingState()
    }
}

fun STPhotoMapInteractor.cancelAllGeojsonEntityLevelOperations() {
    this.worker?.cancelAllGeojsonEntityLevelOperations()
}