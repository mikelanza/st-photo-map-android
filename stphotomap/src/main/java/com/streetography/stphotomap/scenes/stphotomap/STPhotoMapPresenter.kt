package com.streetography.stphotomap.scenes.stphotomap

import java.lang.ref.WeakReference

interface STPhotoMapPresentationLogic {
    fun presentLoadingState()
    fun presentNotLoadingState()
}

class STPhotoMapPresenter: STPhotoMapPresentationLogic {
    var displayer: WeakReference<STPhotoMapDisplayLogic>? = null

    override fun presentLoadingState() {
        this.displayer?.get()?.displayLoadingState()
    }

    override fun presentNotLoadingState() {
        this.displayer?.get()?.displayNotLoadingState()
    }
}