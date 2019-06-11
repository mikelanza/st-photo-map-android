package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorker
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorkerDelegate

class STPhotoMapWorkerSpy(delegate: STPhotoMapWorkerDelegate?): STPhotoMapWorker(delegate) {
    var delay: Long = 0
}