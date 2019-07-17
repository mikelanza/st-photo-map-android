package com.streetography.stphotomap.scenes.stphotomap.location_level

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.streetography.stphotomap.scenes.stphotomap.markers.PhotoClusterView
import com.streetography.stphotomap.scenes.stphotomap.markers.PhotoMarker
import com.streetography.stphotomap.scenes.stphotomap.markers.PhotoMarkerView

class STPhotoClusterRenderer(private val context: Context, map: GoogleMap?, clusterManager: ClusterManager<PhotoMarker>?) :
    DefaultClusterRenderer<PhotoMarker>(context, map, clusterManager) {

    private val markerView: PhotoMarkerView = PhotoMarkerView(context, null)
    private val clusterView: PhotoClusterView = PhotoClusterView(context, null)

    //region Cluster item rendition
    override fun onBeforeClusterItemRendered(item: PhotoMarker?, markerOptions: MarkerOptions?) {
        markerView.setImageResource(null)
        markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(makeMarkerIcon()))?.anchor(0.5f, 1.0f)
    }

    override fun onClusterItemRendered(clusterItem: PhotoMarker?, marker: Marker?) {
        markerView.setImageResource(null)

        this.loadImage(clusterItem?.model?.imageUrl, completion = { drawable ->
            markerView.setImageResource(drawable)
            marker?.setIcon(BitmapDescriptorFactory.fromBitmap(makeMarkerIcon()))
        })
    }

    private fun makeMarkerIcon(): Bitmap {
        return makeIcon(markerView)
    }
    //endregion

    //region Cluster rendition
    override fun onBeforeClusterRendered(cluster: Cluster<PhotoMarker>?, markerOptions: MarkerOptions?) {
        cluster?.let {
            clusterView.setPhotoMarkers(ArrayList(it.items))
            clusterView.clearImages()
            markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(makeClusterIcon()))?.anchor(0.5f, 1.0f)
        }
    }

    override fun onClusterRendered(cluster: Cluster<PhotoMarker>?, marker: Marker?) {
        cluster?.let {
            clusterView.addLines()
            clusterView.addTitle()
            clusterView.addImages()

            val markers = ArrayList(it.items)
            for (i in 0 until markers.size) {
                this.loadImage(markers[i].model.imageUrl, completion = { drawable ->
                    clusterView.setImageResource(drawable, i)
                    marker?.setIcon(BitmapDescriptorFactory.fromBitmap(makeClusterIcon()))
                })
            }
        }
    }

    private fun makeClusterIcon(): Bitmap {
        return makeIcon(clusterView)
    }
    //endregion

    private fun loadImage(url: String?, completion: (image: Drawable) -> Unit) {
        Glide.with(this.context)
            .load(url)
            .into(object: SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                completion(resource)
            }
        })
    }

    private fun makeIcon(view: View): Bitmap {
        val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(measureSpec, measureSpec)
        val measuredWidth = view.measuredWidth
        val measuredHeight = view.measuredHeight
        view.layout(0, 0, measuredWidth, measuredHeight)

        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(0)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}
