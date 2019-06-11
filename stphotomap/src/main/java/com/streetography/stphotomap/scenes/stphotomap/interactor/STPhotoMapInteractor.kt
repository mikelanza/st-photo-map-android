package com.streetography.stphotomap.scenes.stphotomap.interactor

import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapPresentationLogic
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorker
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorkerDelegate

interface STPhotoMapBusinessLogic {
}

class STPhotoMapInteractor: STPhotoMapBusinessLogic,
    STPhotoMapWorkerDelegate {
    var worker: STPhotoMapWorker?
    var presenter: STPhotoMapPresentationLogic? = null

    constructor() {
        this.worker = STPhotoMapWorker(this)
    }
}