package com.streetography.stphotomap.scenes.stphotomap

import com.streetography.stphotomap.scenes.stphotomap.photo_map_view.STPhotoMapDisplayLogic
import java.lang.ref.WeakReference

interface STPhotoMapRoutingLogic {
}

class STPhotoMapRouter: STPhotoMapRoutingLogic {
    var displayer: WeakReference<STPhotoMapDisplayLogic>? = null

}