package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import android.os.Handler
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.photo.STPhoto
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.operations.base.errors.OperationError
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorker
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorkerDelegate
import java.util.*

class STPhotoMapWorkerSpy(delegate: STPhotoMapWorkerDelegate?): STPhotoMapWorker(delegate) {
    var delay: Long = 0
    lateinit var geojsonObject: GeoJSONObject

    var shouldFailGetGeojsonTileForCaching: Boolean = false
    var getGeojsonTileForCachingCalled: Boolean = false

    var shouldFailGetGeojsonTileForEntityLevel: Boolean = false
    var getGeojsonTileForEntityLevelCalled: Boolean = false
    var cancelAllGeojsonEntityLevelOperationsCalled: Boolean = false

    var shouldFailGetGeojsonLocationLevel: Boolean = false
    var getGeojsonLocationLevelCalled: Boolean = false
    var cancelAllGeojsonLocationLevelOperationsCalled: Boolean = false

    var shouldFailGetPhotoDetailsForPhotoMarker: Boolean = false
    var getPhotoDetailsForPhotoMarkerCalled: Boolean = false

    override fun getGeojsonTileForCaching(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String) {
        this.getGeojsonTileForCachingCalled = true

        if (this.delay == 0L) {
            this.didGetGeojsonTileForCaching(tileCoordinate, keyUrl, downloadUrl)
        } else {
            Handler().postDelayed({
                this.didGetGeojsonTileForCaching(tileCoordinate, keyUrl, downloadUrl)
            }, this.delay)
        }
    }

    private fun didGetGeojsonTileForCaching(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String) {
        if (this.shouldFailGetGeojsonTileForCaching) {
            this.delegate?.failureDidGetGeojsonTileForCaching(tileCoordinate, keyUrl, downloadUrl, OperationError.NO_DATA_AVAILABLE)
        } else {
            this.delegate?.successDidGetGeojsonTileForCaching(tileCoordinate, keyUrl, downloadUrl, this.geojsonObject)
        }
    }

    //region Geojson for entity level
    override fun getGeojsonEntityLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String) {
        this.getGeojsonTileForEntityLevelCalled = true

        if (this.delay == 0L)  {
            this.didGetGeojsonTileForEntityLevel(tileCoordinate, keyUrl, downloadUrl)
        } else {
            Handler().postDelayed({
                this.didGetGeojsonTileForEntityLevel(tileCoordinate, keyUrl, downloadUrl)
            }, this.delay)
        }
    }

    private fun didGetGeojsonTileForEntityLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String) {
        if (this.shouldFailGetGeojsonTileForEntityLevel) {
            this.delegate?.failureDidGetGeojsonTileForEntityLevel(tileCoordinate, keyUrl, downloadUrl, OperationError.NO_DATA_AVAILABLE)
        } else {
            this.delegate?.successDidGetGeojsonTileForEntityLevel(tileCoordinate, keyUrl, downloadUrl, this.geojsonObject)
        }
    }

    override fun cancelAllGeojsonEntityLevelOperations() {
        this.cancelAllGeojsonEntityLevelOperationsCalled = true
    }
    //endregion

    //region Geojson for location level
    override fun getGeojsonLocationLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String) {
        this.getGeojsonLocationLevelCalled = true

        if (this.delay == 0L)  {
            this.didGetGeojsonLocationLevel(tileCoordinate, keyUrl, downloadUrl)
        } else {
            Handler().postDelayed({
                this.didGetGeojsonLocationLevel(tileCoordinate, keyUrl, downloadUrl)
            }, this.delay)
        }
    }

    private fun didGetGeojsonLocationLevel(tileCoordinate: TileCoordinate, keyUrl: String, downloadUrl: String) {
        if (this.shouldFailGetGeojsonLocationLevel) {
            this.delegate?.failureDidGetGeojsonTileForLocationLevel(tileCoordinate, keyUrl, downloadUrl, OperationError.NO_DATA_AVAILABLE)
        } else {
            this.delegate?.successDidGetGeojsonTileForLocationLevel(tileCoordinate, keyUrl, downloadUrl, this.geojsonObject)
        }
    }

    override fun cancelAllGeojsonLocationLevelOperations() {
        this.cancelAllGeojsonLocationLevelOperationsCalled = true
    }
    //end

    //region Photo details
    override fun getPhotoDetailsForPhotoMarker(photoId: String) {
        this.getPhotoDetailsForPhotoMarkerCalled = true

        if (this.delay == 0L) {
            this.didGetPhotoDetailsForPhotoMarker(photoId)
        } else {
            Handler().postDelayed({
                this.didGetPhotoDetailsForPhotoMarker(photoId)
            }, this.delay)
        }
    }

    private fun didGetPhotoDetailsForPhotoMarker(photoId: String) {
        if (this.shouldFailGetPhotoDetailsForPhotoMarker) {
            this.delegate?.failureDidGetPhotoDetailsForPhotoMarker(photoId, OperationError.NO_DATA_AVAILABLE)
        } else {
            this.delegate?.successDidGetPhotoDetailsForPhotoMarker(photoId, STPhoto(photoId, Date()))
        }
    }
    //endregion
}