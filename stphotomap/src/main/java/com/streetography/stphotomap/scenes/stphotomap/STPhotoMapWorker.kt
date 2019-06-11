package com.streetography.stphotomap.scenes.stphotomap

interface STPhotoMapWorkerDelegate {
}

open class STPhotoMapWorker {
    val delegate: STPhotoMapWorkerDelegate?

    constructor(delegate: STPhotoMapWorkerDelegate?) {
        this.delegate = delegate
    }
}