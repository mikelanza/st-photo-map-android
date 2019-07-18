package com.streetography.stphotomap.scenes.stphotomap.location_level

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.streetography.stphotomap.scenes.stphotomap.markers.photo.PhotoMarker

class STPhotoMapMarkerHandler(private val context: Context, private val map: GoogleMap?): ClusterManager.OnClusterClickListener<PhotoMarker>, ClusterManager.OnClusterItemClickListener<PhotoMarker> {
    var clusterManager: ClusterManager<PhotoMarker> = ClusterManager(context, map)
    var clusterRenderer: STPhotoClusterRenderer = STPhotoClusterRenderer(this.context, this.map, this.clusterManager)
    var markers: ArrayList<PhotoMarker> = arrayListOf()

    var selectedPhotoMarker: PhotoMarker? = null
    var selectedCluster: Cluster<PhotoMarker>? = null

    init {
        this.setupClusterManager()
        this.setupGoogleMap()
    }

    private fun setupClusterManager() {
        this.clusterManager.setRenderer(this.clusterRenderer)
        this.clusterManager.setOnClusterClickListener(this)
        this.clusterManager.setOnClusterItemClickListener(this)
    }

    private fun setupGoogleMap() {
        this.map?.setOnMarkerClickListener(this.clusterManager)
        this.map?.setOnInfoWindowClickListener(this.clusterManager)
    }

    fun addMarkers(markers: ArrayList<PhotoMarker>) {
        markers.forEach {
            this.addMarker(it)
        }
        this.clusterManager.cluster()
    }

    fun removeAllMarkers() {
        this.markers.clear()
        this.clusterManager.clearItems()
    }

    private fun addMarker(marker: PhotoMarker) {
        if (!this.alreadyExists(marker)) {
            this.markers.add(marker)
            this.clusterManager.addItem(marker)
        }
    }

    private fun alreadyExists(marker: PhotoMarker): Boolean {
        return this.markers.firstOrNull { it.model.photoId == marker.model.photoId } != null
    }

    private fun removeMarker(marker: PhotoMarker) {
        if (this.alreadyExists(marker)) {
            this.markers.remove(marker)
            this.clusterManager.removeItem(marker)
        }
    }

    private fun updateMarker(marker: PhotoMarker?) {
        this.markers.forEach {
            if (it.model.photoId == marker?.model?.photoId) {
                it.update(marker)
            }
        }

        this.clusterManager.algorithm.items.forEach {
            if (it.model.photoId == marker?.model?.photoId) {
                it.update(marker)
            }
        }
    }

    override fun onClusterClick(p0: Cluster<PhotoMarker>?): Boolean {
        Log.i("Marker handler", "onClusterClick size = " + p0?.size)
        return true
    }

    override fun onClusterItemClick(p0: PhotoMarker?): Boolean {
        this.deselectPhotoMarker()
        this.selectPhotoMarker(p0)
        return true
    }

    private fun deselectPhotoMarker() {
        this.selectedPhotoMarker?.isSelected = false
        this.updateMarker(this.selectedPhotoMarker)
        this.clusterRenderer.updatePhotoMarker(this.selectedPhotoMarker)
    }

    private fun selectPhotoMarker(marker: PhotoMarker?) {
        this.selectedPhotoMarker = marker
        this.selectedPhotoMarker?.isSelected = true
        this.updateMarker(this.selectedPhotoMarker)
        this.clusterRenderer.updatePhotoMarker(this.selectedPhotoMarker)
    }
}