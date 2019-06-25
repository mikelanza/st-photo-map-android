package com.streetography.stphotomap.extensions.geojson_object

import com.streetography.stphotomap.extensions.geojson_feature.entityLevel
import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.geojson.GeoJSONType
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONFeature
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONFeatureCollection
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject

val GeoJSONObject.entityLevel: EntityLevel
    get() {
        when (getGeoJSONType()) {
            GeoJSONType.featureCollection -> {
                val feature = (this as? GeoJSONFeatureCollection)?.features?.firstOrNull()
                feature?.entityLevel?.let {
                    return it
                } ?: return EntityLevel.unknown
            }
            GeoJSONType.feature -> (this as? GeoJSONFeature)?.entityLevel?.let {
                return it
            } ?: return EntityLevel.unknown

            else -> return EntityLevel.unknown
        }
    }