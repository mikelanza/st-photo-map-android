package com.streetography.stphotomap.models.geo_entity

import com.streetography.stphotomap.models.coordinate.Coordinate
import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.geojson.BoundingBox
import com.streetography.stphotomap.models.geojson.geometry.GeoJSONPolygon
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.photo.STPhoto

class GeoEntity(var id: Int, var boundingBox: BoundingBox) {
    var name: String? = null
    var entityLevel: EntityLevel = EntityLevel.unknown
    var center: Coordinate? = null
    var geoJSONPolygons: ArrayList<GeoJSONPolygon> = ArrayList()
    var area: Double = 0.0

    var geoJSONObject: GeoJSONObject? = null
    var photoCount: Int = 0
    var photos: ArrayList<STPhoto> = ArrayList()
    var label: GeoLabel? = null
}