package com.streetography.stphotomap.extensions.geojson_object

import com.streetography.stphotomap.models.geojson.GeoJSONType
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONFeature
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONFeatureCollection
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject

fun GeoJSONObject.features(): ArrayList<GeoJSONFeature> {
    when (getGeoJSONType()) {
        GeoJSONType.featureCollection -> {
            (this as? GeoJSONFeatureCollection)?.features?.let {
                return it
            } ?: return arrayListOf()
        }
        GeoJSONType.feature -> (this as? GeoJSONFeature)?.let {
            return arrayListOf(it)
        } ?: return arrayListOf()

        else -> return arrayListOf()
    }
}