package com.streetography.stphotomap.scenes.stphotomap.interactor

import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.parameters.KeyValue
import com.streetography.stphotomap.models.parameters.Parameters
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.operations.base.errors.OperationError
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapModels
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapPresentationLogic
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorker
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorkerDelegate
import com.streetography.stphotomap.scenes.stphotomap.builders.STPhotoMapUriBuilder
import com.streetography.stphotomap.scenes.stphotomap.cache.STPhotoMapGeojsonCache
import com.streetography.stphotomap.scenes.stphotomap.cache.STPhotoMapGeojsonCacheHandler
import com.streetography.stphotomap.scenes.stphotomap.entity_level.STPhotoMapEntityLevelHandler
import com.streetography.stphotomap.scenes.stphotomap.entity_level.STPhotoMapEntityLevelHandlerDelegate
import com.streetography.stphotomap.scenes.stphotomap.parameters.STPhotoMapParametersHandler

interface STPhotoMapBusinessLogic {
    fun shouldUpdateVisibleTiles(request: STPhotoMapModels.VisibleTiles.Request)
    fun shouldUpdateBoundingBox(request: STPhotoMapModels.UpdateBoundingBox.Request)
    fun shouldDetermineEntityLevel()
    fun shouldCacheGeojsonObjects()
}

class STPhotoMapInteractor : STPhotoMapBusinessLogic,
    STPhotoMapWorkerDelegate, STPhotoMapEntityLevelHandlerDelegate {
    var worker: STPhotoMapWorker?
    var presenter: STPhotoMapPresentationLogic? = null

    var visibleTiles: ArrayList<TileCoordinate>
    var cacheHandler: STPhotoMapGeojsonCacheHandler
    var entityLevelHandler: STPhotoMapEntityLevelHandler

    init {
        this.worker = STPhotoMapWorker(this)

        this.visibleTiles = ArrayList()
        this.cacheHandler = STPhotoMapGeojsonCacheHandler()
        this.entityLevelHandler = STPhotoMapEntityLevelHandler(this)
    }

    override fun shouldUpdateVisibleTiles(request: STPhotoMapModels.VisibleTiles.Request) {
        this.visibleTiles = request.tiles
    }

    override fun shouldUpdateBoundingBox(request: STPhotoMapModels.UpdateBoundingBox.Request) {
        request.boundingBox?.description?.let {
            STPhotoMapParametersHandler.instance.update(KeyValue(Parameters.Keys.bbox, it))
        }
    }

    override fun shouldDetermineEntityLevel() {
        this.cancelAllGeojsonEntityLevelOperations()

        val cachedTiles = this.getVisibleCachedTiles()
        if (cachedTiles.size > 0) {
            this.calculateEntityLevelFor(cachedTiles)
        } else {
            this.entityLevelGeojsonObjectsFor(this.prepareTilesForEntityLevel())
        }

        this.handleLoadingStateForEntityLevel()
    }

    internal fun getVisibleCachedTiles(): ArrayList<STPhotoMapGeojsonCache.Tile> {
        return ArrayList(this.visibleTiles.mapNotNull { tile ->
            val uri = STPhotoMapUriBuilder().geojsonTileUri(tile)
            this.cacheHandler.cache.getTile(uri.first)
        })
    }

    //region Entity level
    override fun photoMapEntityLevelHandler(newEntityLevel: EntityLevel) {
        this.worker?.cancelAllGeojsonEntityLevelOperations()
        this.presenter?.presentEntityLevel(STPhotoMapModels.EntityZoomLevel.Response(newEntityLevel))
    }

    override fun photoMapEntityLevelHandlerNewLocationLevel(level: EntityLevel) {
        this.worker?.cancelAllGeojsonEntityLevelOperations()

        this.presenter?.presentEntityLevel(STPhotoMapModels.EntityZoomLevel.Response(level))
    }

    override fun successDidGetGeojsonTileForEntityLevel(
        tileCoordinate: TileCoordinate,
        keyUrl: String,
        downloadUrl: String,
        geojsonObject: GeoJSONObject
    ) {
        this.entityLevelHandler.removeActiveDownload(keyUrl)
        this.didGetGeojsonTileForEntityLevel(tileCoordinate, geojsonObject)
    }

    override fun failureDidGetGeojsonTileForEntityLevel(
        tileCoordinate: TileCoordinate,
        keyUrl: String,
        downloadUrl: String,
        error: OperationError
    ) {
        this.entityLevelHandler.removeActiveDownload(keyUrl)
        this.handleLoadingStateForEntityLevel()
    }
    //endregion

    //region Geojson caching
    override fun shouldCacheGeojsonObjects() {
        val tiles = this.prepareTilesForCaching()
        this.cacheGeojsonObjects(tiles)
    }

    override fun successDidGetGeojsonTileForCaching(
        tileCoordinate: TileCoordinate,
        keyUrl: String,
        downloadUrl: String,
        geojsonObject: GeoJSONObject
    ) {
        this.cacheHandler.cache.addTile(STPhotoMapGeojsonCache.Tile(keyUrl, geojsonObject))
        this.cacheHandler.removeActiveDownload(keyUrl)
    }

    override fun failureDidGetGeojsonTileForCaching(
        tileCoordinate: TileCoordinate,
        keyUrl: String,
        downloadUrl: String,
        error: OperationError
    ) {
        this.cacheHandler.removeActiveDownload(keyUrl)
    }
    //endregion
}