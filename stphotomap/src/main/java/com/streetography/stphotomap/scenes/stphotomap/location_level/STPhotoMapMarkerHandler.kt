package com.streetography.stphotomap.scenes.stphotomap.location_level

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.streetography.stphotomap.scenes.stphotomap.markers.PhotoMarker

class STPhotoMapMarkerHandler(context: Context, map: GoogleMap?): ClusterManager.OnClusterClickListener<PhotoMarker>, ClusterManager.OnClusterItemClickListener<PhotoMarker> {
    private val context: Context
    private val map: GoogleMap?
    var clusterManager: ClusterManager<PhotoMarker>
    var markers: ArrayList<PhotoMarker>

    init {
        this.context = context
        this.map = map
        this.markers = arrayListOf()
        this.clusterManager = ClusterManager(context, map)

        this.setupClusterManager()
        this.setupGoogleMap()
    }

    private fun setupClusterManager() {
        clusterManager.setRenderer(STPhotoClusterRenderer(context, map, clusterManager))
        clusterManager.setOnClusterClickListener(this)
        clusterManager.setOnClusterItemClickListener(this)
    }

    private fun setupGoogleMap() {
        map?.setOnMarkerClickListener(clusterManager)
        map?.setOnInfoWindowClickListener(clusterManager)
    }

    fun addMarkers(markers: ArrayList<PhotoMarker>) {
        markers.forEach {
            this.addMarker(it)
        }
        clusterManager.cluster()
    }

    fun removeAllMarkers() {
        this.markers.clear()
        this.clusterManager.clearItems()
    }

    private fun addMarker(marker: PhotoMarker) {
        if (this.alreadyExists(marker) == false) {
            markers.add(marker)
            clusterManager.addItem(marker)
        }
    }

    private fun alreadyExists(marker: PhotoMarker): Boolean {
        val first = this.markers.firstOrNull {
            it.model.photoId == marker.model.photoId
        }
        first?.let {
            return true
        }
        return false
    }

    override fun onClusterClick(p0: Cluster<PhotoMarker>?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClusterItemClick(p0: PhotoMarker?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}