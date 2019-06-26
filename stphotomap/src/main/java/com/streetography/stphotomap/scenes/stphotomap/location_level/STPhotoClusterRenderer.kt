package com.streetography.stphotomap.scenes.stphotomap.location_level

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
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
import com.streetography.stphotomap.scenes.stphotomap.markers.PhotoMapClusterMarkerView
import com.streetography.stphotomap.scenes.stphotomap.markers.PhotoMarker
import com.streetography.stphotomap.scenes.stphotomap.markers.PhotoMarkerView

class STPhotoClusterRenderer(context: Context, map: GoogleMap?, clusterManager: ClusterManager<PhotoMarker>?) :
    DefaultClusterRenderer<PhotoMarker>(context, map, clusterManager) {

    private val context: Context
    private val markerView: PhotoMarkerView
    private val clusterView: PhotoMapClusterMarkerView

    init {
        this.context = context
        this.markerView = PhotoMarkerView(context, null)
        this.clusterView = PhotoMapClusterMarkerView(context, null)
    }

    override fun onBeforeClusterItemRendered(item: PhotoMarker?, markerOptions: MarkerOptions?) {
        markerView.setImageBackgroundColor(Color.GRAY)
        markerView.setImageResource(null)
        markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(convertLayout(markerView)))?.anchor(0.5f, 0.8f)
    }

    override fun onBeforeClusterRendered(cluster: Cluster<PhotoMarker>?, markerOptions: MarkerOptions?) {
        cluster?.let {
            clusterView.setupBadgeNumber(it.size)
            markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(this.convertLayout(clusterView)))?.anchor(0.5f, 0.8f)
        }
    }

    override fun onClusterItemRendered(clusterItem: PhotoMarker?, marker: Marker?) {
        markerView.setImageBackgroundColor(Color.GRAY)
        markerView.setImageResource(null)

        Glide.with(context).load(clusterItem?.model?.imageUrl).into(object: SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                markerView.setImageResource(resource)
                marker?.setIcon(BitmapDescriptorFactory.fromBitmap(convertLayout(markerView)))
            }
        })
    }

    override fun onClusterRendered(cluster: Cluster<PhotoMarker>?, marker: Marker?) {
        cluster?.let {
            clusterView.setupBadgeNumber(it.size)
            marker?.setIcon(BitmapDescriptorFactory.fromBitmap(this.convertLayout(clusterView)))
        }
    }

    private fun convertLayout(view: View): Bitmap {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.TRANSPARENT)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}
