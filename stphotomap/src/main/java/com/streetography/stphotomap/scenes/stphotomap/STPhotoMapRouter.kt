package com.streetography.stphotomap.scenes.stphotomap

import java.lang.ref.WeakReference

interface STPhotoMapRoutingLogic {
}

class STPhotoMapRouter: STPhotoMapRoutingLogic {
    var displayer: WeakReference<STPhotoMapDisplayLogic>? = null

}