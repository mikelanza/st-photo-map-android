package com.streetography.stphotomap.scenes.stphotomap.interactor

import com.streetography.stphotomap.extensions.geojson_object.features
import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.geojson.geometry.GeoJSONPoint
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONFeature
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapModels
import com.streetography.stphotomap.scenes.stphotomap.cache.STPhotoMapGeojsonCache

fun STPhotoMapInteractor.isLocationLevel(): Boolean {
    return this.entityLevelHandler.entityLevel == EntityLevel.location
}

fun STPhotoMapInteractor.presentLocationAnnotations(markers: ArrayList<STPhotoMapModels.Marker>) {
    if (markers.isEmpty()) { return }
    this.presenter?.presentLocationMarkers(STPhotoMapModels.LocationMarkers.Response(markers))
}

fun STPhotoMapInteractor.presentPhotoAnnotationsForCached(tiles: ArrayList<STPhotoMapGeojsonCache.Tile>) {
    val markers = arrayListOf<STPhotoMapModels.Marker>()
    tiles.forEach {
        markers.addAll(this.getMarkers(it.geojsonObject))
    }
    this.presentLocationAnnotations(markers)
}

fun STPhotoMapInteractor.getMarkers(geojsonObject: GeoJSONObject): ArrayList<STPhotoMapModels.Marker> {
    return this.getMarkers(geojsonObject.features())
}

fun STPhotoMapInteractor.getMarkers(features: ArrayList<GeoJSONFeature>): ArrayList<STPhotoMapModels.Marker> {
    val markers = arrayListOf<STPhotoMapModels.Marker>()
    features.forEach {
        this.marker(it)?.let {
            markers.add(it)
        }
    }
    return markers
}

fun STPhotoMapInteractor.marker(feature: GeoJSONFeature): STPhotoMapModels.Marker? {
    feature.idAsString?.let { id ->
        (feature.geometry as? GeoJSONPoint)?.let { point ->
            return STPhotoMapModels.Marker(id, feature.photoProperties.image250Url, point.latitude, point.longitude)
        }
    }
    return null
}



