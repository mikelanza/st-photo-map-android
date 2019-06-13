package com.streetography.stphotomap.scenes.stphotomap
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.operations.base.errors.OperationError


interface STPhotoMapWorkerDelegate {
    fun successDidGetGeojsonTileForEntityLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, geojsonObject: GeoJSONObject)
    fun failureDidGetGeojsonTileForEntityLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, error: OperationError)
}

open class STPhotoMapWorker {
    val delegate: STPhotoMapWorkerDelegate?

    constructor(delegate: STPhotoMapWorkerDelegate?) {
        this.delegate = delegate
    }

    //region Get geojson for entity level
    fun getGeojsonEntityLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String) {
        // TODO: Implement
    }

    fun cancelAllGeojsonEntityLevelOperations() {
        // TODO: Implement
    }
    //endregion
}