package com.streetography.stphotomap.scenes.stphotomap.markers.photo

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class PhotoMarker(id: String, coordinate: LatLng, imageUrl: String?, bitmap: Bitmap?): ClusterItem {
    class Model(val photoId: String, var coordinate: LatLng, var imageUrl: String?, var bitmapImage: Bitmap?)

    var model: Model
    var isSelected: Boolean = false

    init {
        this.model = Model(
            id,
            coordinate,
            imageUrl,
            bitmap
        )
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

    fun update(marker: PhotoMarker?) {
        marker?.let {
            this.isSelected = it.isSelected
        }
    }
}