package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapDisplayLogic

class STPhotoMapDisplayLogicSpy: STPhotoMapDisplayLogic {
    var displayLoadingStateCalled: Boolean = false
    var displayNotLoadingStateCalled: Boolean = false

    override fun displayLoadingState() {
        this.displayLoadingStateCalled = true
    }

    override fun displayNotLoadingState() {
        this.displayNotLoadingStateCalled = true
    }
}