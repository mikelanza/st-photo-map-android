package com.streetography.stphotomap.scenes.stphotomap

import java.lang.ref.WeakReference

interface STPhotoMapPresentationLogic {
}

class STPhotoMapPresenter: STPhotoMapPresentationLogic {
    var displayer: WeakReference<STPhotoMapDisplayLogic>? = null
}