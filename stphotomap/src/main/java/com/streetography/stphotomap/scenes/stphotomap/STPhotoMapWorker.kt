package com.streetography.stphotomap.scenes.stphotomap
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.operations.base.errors.OperationError
import com.streetography.stphotomap.operations.base.operations.OperationQueue
import com.streetography.stphotomap.operations.base.results.OperationResult
import com.streetography.stphotomap.operations.network.geojson_tile.GetGeojsonTileOperation
import com.streetography.stphotomap.operations.network.geojson_tile.GetGeojsonTileOperationModel


interface STPhotoMapWorkerDelegate {
    fun successDidGetGeojsonTileForEntityLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, geojsonObject: GeoJSONObject)
    fun failureDidGetGeojsonTileForEntityLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, error: OperationError)

    fun successDidGetGeojsonTileForCaching(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, geojsonObject: GeoJSONObject)
    fun failureDidGetGeojsonTileForCaching(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, error: OperationError)
}

open class STPhotoMapWorker(val delegate: STPhotoMapWorkerDelegate?) {
    var geojsonTileCachingQueue: OperationQueue = OperationQueue()
    var geojsonEntityLevelQueue: OperationQueue = OperationQueue()

    //region Get geojson for entity level
    open fun getGeojsonEntityLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String) {
        val model = GetGeojsonTileOperationModel.Request(tileCoordinate, downloadUrl)
        val operation = GetGeojsonTileOperation(model, object: OperationResult<GetGeojsonTileOperationModel.Response> {
            override fun onSuccess(value: GetGeojsonTileOperationModel.Response) {
                delegate?.successDidGetGeojsonTileForEntityLevel(tileCoordinate, keyUrl, downloadUrl, value.geoJSONObject)
            }

            override fun onFailure(error: OperationError) {
                delegate?.failureDidGetGeojsonTileForEntityLevel(tileCoordinate, keyUrl, downloadUrl, error)
            }
        })
        this.geojsonEntityLevelQueue.addOperation(operation)
    }

    open fun cancelAllGeojsonEntityLevelOperations() {
        this.geojsonEntityLevelQueue.cancelAllOperations()
    }
    //endregion

    //region Get geojson for caching
    open fun getGeojsonTileForCaching(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String) {
        val model = GetGeojsonTileOperationModel.Request(tileCoordinate, downloadUrl)
        val operation = GetGeojsonTileOperation(model, object: OperationResult<GetGeojsonTileOperationModel.Response> {
            override fun onSuccess(value: GetGeojsonTileOperationModel.Response) {
                delegate?.successDidGetGeojsonTileForCaching(tileCoordinate, keyUrl, downloadUrl, value.geoJSONObject)
            }

            override fun onFailure(error: OperationError) {
                delegate?.failureDidGetGeojsonTileForCaching(tileCoordinate, keyUrl, downloadUrl, error)
            }
        })
        this.geojsonTileCachingQueue.addOperation(operation)
    }
    //endregion
}