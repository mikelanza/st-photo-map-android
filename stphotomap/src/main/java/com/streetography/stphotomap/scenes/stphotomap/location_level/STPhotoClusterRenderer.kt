package com.streetography.stphotomap.scenes.stphotomap.location_level

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.streetography.stphotomap.scenes.stphotomap.markers.cluster.PhotoClusterView
import com.streetography.stphotomap.scenes.stphotomap.markers.photo.PhotoMarker
import com.streetography.stphotomap.scenes.stphotomap.markers.photo.PhotoMarkerView

class STPhotoClusterRenderer(private val context: Context, map: GoogleMap?, clusterManager: ClusterManager<PhotoMarker>?) :
    DefaultClusterRenderer<PhotoMarker>(context, map, clusterManager) {

    private val markerView: PhotoMarkerView = PhotoMarkerView(context, null)
    private val clusterView: PhotoClusterView = PhotoClusterView(context, null)

    var selectedCluster: Cluster<PhotoMarker>? = null

    fun updatePhotoMarker(photoMarker: PhotoMarker?) {
        val marker: Marker? = this.getMarker(photoMarker)
        this.onClusterItemRendered(photoMarker, marker)
    }

    fun updateCluster(cluster: Cluster<PhotoMarker>?) {
        val marker = this.getMarker(cluster)
        this.onClusterRendered(cluster, marker)
    }

    //region Cluster item logic
    override fun onBeforeClusterItemRendered(item: PhotoMarker?, markerOptions: MarkerOptions?) {
        markerView.setImageResource(null)
        markerView.setIsSelected(false)
        markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(makeMarkerIcon()))?.anchor(0.5f, 1.0f)
    }

    override fun onClusterItemRendered(clusterItem: PhotoMarker?, marker: Marker?) {
        clusterItem?.let {
            markerView.setImageResource(null)
            markerView.setIsSelected(it.isSelected)

            this.loadImage(it.model.imageUrl, completion = { drawable ->
                markerView.setImageResource(drawable)
                markerView.setIsSelected(it.isSelected)
                marker?.setIcon(BitmapDescriptorFactory.fromBitmap(makeMarkerIcon()))
            })
        }
    }

    private fun makeMarkerIcon(): Bitmap {
        return makeIcon(markerView)
    }
    //endregion

    //region Cluster logic
    override fun onBeforeClusterRendered(cluster: Cluster<PhotoMarker>?, markerOptions: MarkerOptions?) {
        clusterView.setPhotoMarkers(this.photoMarkersFromCluster(cluster))
        clusterView.setIsClusterSelected(this.selectedCluster?.equals(cluster) ?: false)
        clusterView.clearLines()
        clusterView.clearTitle()
        clusterView.clearImages()
        markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(makeClusterIcon()))?.anchor(0.5f, 1.0f)
    }

    override fun onClusterRendered(cluster: Cluster<PhotoMarker>?, marker: Marker?) {
        if (this.selectedCluster?.equals(cluster) == true) {
            this.onSelectedClusterRenderer(cluster, marker)
        } else {
            this.onDeselectedClusterRenderer(cluster, marker)
        }
    }

    private fun onSelectedClusterRenderer(cluster: Cluster<PhotoMarker>?, marker: Marker?) {
        clusterView.setIsClusterSelected(true)
        clusterView.addLines()
        clusterView.addTitle()
        clusterView.addImages()

        val markers = this.photoMarkersFromCluster(cluster)
        for (i in 0 until markers.size) {
            this.loadImage(markers[i].model.imageUrl, completion = { drawable ->
                clusterView.setImageResource(drawable, i)
                marker?.setIcon(BitmapDescriptorFactory.fromBitmap(makeClusterIcon()))
            })
        }
    }

    private fun onDeselectedClusterRenderer(cluster: Cluster<PhotoMarker>?, marker: Marker?) {
        clusterView.setIsClusterSelected(false)
        clusterView.clearLines()
        clusterView.addTitle()
        clusterView.clearImages()

        marker?.setIcon(BitmapDescriptorFactory.fromBitmap(makeClusterIcon()))
    }

    private fun photoMarkersFromCluster(cluster: Cluster<PhotoMarker>?): ArrayList<PhotoMarker> {
        cluster?.let { return ArrayList(it.items) }
        return ArrayList()
    }

    private fun makeClusterIcon(): Bitmap {
        return makeIcon(clusterView)
    }
    //endregion

    private fun loadImage(url: String?, completion: (image: Drawable) -> Unit) {
        Glide.with(this.context)
            .load(url)
            .into(object: CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

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
