package com.streetography.stphotomap.scenes.stphotomap.markers

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class PhotoMarker(id: String, coordinate: LatLng, imageUrl: String?, bitmap: Bitmap?): ClusterItem {
    class Model(photoId: String, coordinate: LatLng, imageUrl: String?, bitmapImage: Bitmap?) {
        val photoId: String
        var coordinate: LatLng
        var imageUrl: String?
        var bitmapImage: Bitmap?

        init {
            this.photoId = photoId
            this.coordinate = coordinate
            this.imageUrl = imageUrl
            this.bitmapImage = bitmapImage
        }
    }

    var model: Model

    init {
        this.model = Model(id, coordinate, imageUrl, bitmap)
    }

    override fun getSnippet(): String? {
        return null
    }

    override fun getTitle(): String? {
        return null
    }

    override fun getPosition(): LatLng {
        return model.coordinate
    }
}