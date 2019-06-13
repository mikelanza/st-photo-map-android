package com.streetography.stphotomap.scenes.stphotomap.interactor

import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapModels
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapPresentationLogic
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorker
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorkerDelegate
import com.streetography.stphotomap.scenes.stphotomap.entity_level.STPhotoMapEntityLevelHandler
import java.util.*

interface STPhotoMapBusinessLogic {
    fun shouldUpdateVisibleTiles(request: STPhotoMapModels.VisibleTiles.Request)
    fun shouldDetermineEntityLevel()
}

class STPhotoMapInteractor: STPhotoMapBusinessLogic,
    STPhotoMapWorkerDelegate {
    var worker: STPhotoMapWorker?
    var presenter: STPhotoMapPresentationLogic? = null

    var visibleTiles: ArrayList<TileCoordinate>
    var entityLevelHandler: STPhotoMapEntityLevelHandler

    constructor() {
        this.worker = STPhotoMapWorker(this)

        this.visibleTiles = ArrayList<TileCoordinate>()
        this.entityLevelHandler = STPhotoMapEntityLevelHandler()
    }

    override fun shouldUpdateVisibleTiles(request: STPhotoMapModels.VisibleTiles.Request) {
        this.visibleTiles = request.tiles
    }

    override fun shouldDetermineEntityLevel() {
        TODO("not implemented")
    }
}