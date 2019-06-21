package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapModels
import com.streetography.stphotomap.scenes.stphotomap.interactor.STPhotoMapBusinessLogic

class STPhotoMapBusinessLogicSpy: STPhotoMapBusinessLogic {
    var shouldUpdateVisibleTilesCalled: Boolean = false
    var shouldDetermineEntityLevelCalled: Boolean = false
    var shouldCacheGeojsonObjectsCalled: Boolean = false
    var shouldUpdateBoundingBoxCalled: Boolean = false

    override fun shouldUpdateVisibleTiles(request: STPhotoMapModels.VisibleTiles.Request) {
        this.shouldUpdateVisibleTilesCalled = true
    }

    override fun shouldDetermineEntityLevel() {
        this.shouldDetermineEntityLevelCalled = true
    }

    override fun shouldCacheGeojsonObjects() {
        this.shouldCacheGeojsonObjectsCalled = true
    }

    override fun shouldUpdateBoundingBox(request: STPhotoMapModels.UpdateBoundingBox.Request) {
        this.shouldUpdateBoundingBoxCalled = true
    }
}