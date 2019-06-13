package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapPresentationLogic

class STPhotoMapPresentationLogicSpy: STPhotoMapPresentationLogic {
    var presentLoadingStateCalled: Boolean = false
    var presentNotLoadingStateCalled: Boolean = false

    override fun presentLoadingState() {
        this.presentLoadingStateCalled = true
    }

    override fun presentNotLoadingState() {
        this.presentNotLoadingStateCalled = true
    }
}