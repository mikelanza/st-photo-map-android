package com.streetography.stphotomap.scenes.stphotomap.location_level

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.streetography.stphotomap.models.coordinate.Coordinate
import com.streetography.stphotomap.scenes.stphotomap.markers.photo.PhotoMarker

interface STPhotoMapMarkerHandlerDelegate {
    fun photoMapMarkerHandlerDidReselectPhoto(photoId: String)
    fun photoMapMarkerHandlerDidSelectPhoto(photoId: String)

    fun photoMapMarkerHandlerNavigateToSpecificPhotos(photoIds: ArrayList<String>)
    fun photoMapMarkerHandlerZoomToCoordinate(coordinate: Coordinate)
}

class STPhotoMapMarkerHandler(private val context: Context, private val map: GoogleMap?): ClusterManager.OnClusterClickListener<PhotoMarker>, ClusterManager.OnClusterItemClickListener<PhotoMarker> {
    var clusterManager: ClusterManager<PhotoMarker> = ClusterManager(context, map)
    var clusterRenderer: STPhotoClusterRenderer = STPhotoClusterRenderer(this.context, this.map, this.clusterManager)
    var markers: ArrayList<PhotoMarker> = arrayListOf()

    var selectedPhotoMarker: PhotoMarker? = null
    var selectedCluster: Cluster<PhotoMarker>? = null

    var delegate: STPhotoMapMarkerHandlerDelegate? = null

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
        this.clusterManager.cluster()
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
        if (p0 == null) return false

        val zoom = this.map?.cameraPosition?.zoom
        val sameCoordinateForPhotoMarkers: Boolean = this.doPhotoMarkersHaveSameCoordinate(p0)

        if (zoom == 20F && p0.size > 15) {
            this.delegate?.photoMapMarkerHandlerNavigateToSpecificPhotos(this.photoIds(p0))
        } else if (zoom == 20F || sameCoordinateForPhotoMarkers) {
            if (this.selectedCluster != null) {
                this.clusterRenderer.selectedCluster = null
                this.clusterRenderer.updateCluster(this.selectedCluster)
            }

            this.selectedCluster = p0
            this.clusterRenderer.selectedCluster = p0
            this.clusterRenderer.updateCluster(p0)
        } else {
            this.delegate?.photoMapMarkerHandlerZoomToCoordinate(Coordinate.fromLatLng(p0.position))
        }

        return true
    }

    private fun doPhotoMarkersHaveSameCoordinate(cluster: Cluster<PhotoMarker>): Boolean {
        val coordinate = cluster.items?.firstOrNull()?.model?.coordinate
        cluster.items?.forEach {
            if (it?.model?.coordinate?.equals(coordinate) == false) {
                return false
            }
        }
        return true
    }

    private fun photoIds(cluster: Cluster<PhotoMarker>): ArrayList<String> {
        return ArrayList(cluster.items?.mapNotNull { it?.model?.photoId }.orEmpty())
    }

    override fun onClusterItemClick(p0: PhotoMarker?): Boolean {
        p0?.let {
            if (it.isSelected) {
                this.delegate?.photoMapMarkerHandlerDidReselectPhoto(it.model.photoId)
            } else {
                this.delegate?.photoMapMarkerHandlerDidSelectPhoto(it.model.photoId)
            }
        }

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