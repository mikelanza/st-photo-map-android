package com.streetography.stphotomap.scenes.stphotomap
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.photo.STPhoto
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.operations.base.errors.OperationError
import com.streetography.stphotomap.operations.base.operations.OperationQueue
import com.streetography.stphotomap.operations.base.results.OperationResult
import com.streetography.stphotomap.operations.network.geojson_tile.GetGeojsonTileOperation
import com.streetography.stphotomap.operations.network.geojson_tile.GetGeojsonTileOperationModel
import com.streetography.stphotomap.operations.network.photo.get.GetPhotoOperation
import com.streetography.stphotomap.operations.network.photo.get.GetPhotoOperationModel


interface STPhotoMapWorkerDelegate {
    fun successDidGetGeojsonTileForEntityLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, geojsonObject: GeoJSONObject)
    fun failureDidGetGeojsonTileForEntityLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, error: OperationError)

    fun successDidGetGeojsonTileForCaching(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, geojsonObject: GeoJSONObject)
    fun failureDidGetGeojsonTileForCaching(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, error: OperationError)

    fun successDidGetGeojsonTileForLocationLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, geojsonObject: GeoJSONObject)
    fun failureDidGetGeojsonTileForLocationLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String, error: OperationError)

    fun successDidGetPhotoDetailsForPhotoMarker(photoId: String, photo: STPhoto)
    fun failureDidGetPhotoDetailsForPhotoMarker(photoId: String, error: OperationError)
}

open class STPhotoMapWorker(val delegate: STPhotoMapWorkerDelegate?) {
    var geojsonTileCachingQueue: OperationQueue = OperationQueue()
    var geojsonEntityLevelQueue: OperationQueue = OperationQueue()
    var geojsonLocationLevelQueue: OperationQueue = OperationQueue()
    var photoDetailsQueue: OperationQueue = OperationQueue()

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

    //region Get geojson for location level
    open fun getGeojsonLocationLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String) {
        val model = GetGeojsonTileOperationModel.Request(tileCoordinate, downloadUrl)
        val operation = GetGeojsonTileOperation(model, object: OperationResult<GetGeojsonTileOperationModel.Response> {
            override fun onSuccess(value: GetGeojsonTileOperationModel.Response) {
                delegate?.successDidGetGeojsonTileForLocationLevel(tileCoordinate, keyUrl, downloadUrl, value.geoJSONObject)
            }

            override fun onFailure(error: OperationError) {
                delegate?.failureDidGetGeojsonTileForLocationLevel(tileCoordinate, keyUrl, downloadUrl, error)
            }
        })
        this.geojsonLocationLevelQueue.addOperation(operation)
    }

    open fun cancelAllGeojsonLocationLevelOperations() {
        this.geojsonLocationLevelQueue.cancelAllOperations()
    }
    //endregion

    open fun getPhotoDetailsForPhotoMarker(photoId: String) {
        val model = GetPhotoOperationModel.Request(photoId)
        val operation = GetPhotoOperation(model, object: OperationResult<GetPhotoOperationModel.Response> {
            override fun onSuccess(value: GetPhotoOperationModel.Response) {
                delegate?.successDidGetPhotoDetailsForPhotoMarker(photoId, value.photo)
            }

            override fun onFailure(error: OperationError) {
                delegate?.failureDidGetPhotoDetailsForPhotoMarker(photoId, error)
            }
        })
        this.photoDetailsQueue.addOperation(operation)
    }
}