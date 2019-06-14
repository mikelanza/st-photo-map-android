package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import android.os.Handler
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.operations.base.errors.OperationError
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorker
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorkerDelegate

class STPhotoMapWorkerSpy(delegate: STPhotoMapWorkerDelegate?): STPhotoMapWorker(delegate) {
    var delay: Long = 0
    lateinit var geojsonObject: GeoJSONObject

    var shouldFailGetGeojsonTileForCaching: Boolean = false
    var getGeojsonTileForCachingCalled: Boolean = false

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
}