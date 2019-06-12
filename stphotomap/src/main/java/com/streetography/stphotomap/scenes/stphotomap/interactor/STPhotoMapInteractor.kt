package com.streetography.stphotomap.scenes.stphotomap.interactor

import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapModels
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapPresentationLogic
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorker
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorkerDelegate
import java.util.*

interface STPhotoMapBusinessLogic {
    fun shouldUpdateVisibleTiles(request: STPhotoMapModels.VisibleTiles.Request)
}

class STPhotoMapInteractor: STPhotoMapBusinessLogic,
    STPhotoMapWorkerDelegate {
    var worker: STPhotoMapWorker?
    var presenter: STPhotoMapPresentationLogic? = null

    var visibleTiles: ArrayList<TileCoordinate>

    constructor() {
        this.worker = STPhotoMapWorker(this)

        this.visibleTiles = ArrayList<TileCoordinate>()
    }

    override fun shouldUpdateVisibleTiles(request: STPhotoMapModels.VisibleTiles.Request) {
        this.visibleTiles = request.tiles
    }
}