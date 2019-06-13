package com.streetography.stphotomap.scenes.stphotomap.interactor

import com.streetography.stphotomap.extensions.geojson_object.entityLevel
import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.scenes.stphotomap.cache.STPhotoMapGeojsonCache

fun STPhotoMapInteractor.calculateEntityLevelFor(cachedTiles: ArrayList<STPhotoMapGeojsonCache.Tile>) {
    val entityLevel = cachedTiles.first().geojsonObject.entityLevel
    this.entityLevelHandler.change(entityLevel)
}

fun STPhotoMapInteractor.prepareTilesForEntityLevel(): ArrayList<TileCoordinate> {
    val filteredList = this.visibleTiles.filter { tile ->
        // TODO: Build url for tile
        val keyUrl = ""
        !this.entityLevelHandler.hasActiveDownload(keyUrl)
    }
    return ArrayList(filteredList)
}

fun STPhotoMapInteractor.entityLevelGeojsonObjectsFor(tiles: ArrayList<TileCoordinate>) {
    tiles.forEach({ this.entityLevelGeojsonObjectsFor(it) })
}

fun STPhotoMapInteractor.entityLevelGeojsonObjectsFor(tile: TileCoordinate) {
    // TODO: Build url for tile
    val keyUrl = ""
    val downloadUrl = ""
    this.entityLevelHandler.addActiveDownload(keyUrl)
    this.worker?.getGeojsonEntityLevel(tile, keyUrl, downloadUrl)
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