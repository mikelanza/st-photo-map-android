package com.streetography.stphotomap.extensions.geojson_feature

import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONFeature

val GeoJSONFeature.entityLevel: EntityLevel
get() {
    this.photoProperties?.type?.let {
        return EntityLevel.fromString(it)
    }?: return EntityLevel.unknown
}