package com.streetography.stphotomap.models.geojson.interfaces;

import com.streetography.stphotomap.models.geojson.properties.PhotoProperties;
import org.json.JSONObject;

public interface GeoJSONFeature extends GeoJSONObject {
    GeoJSONGeometry getGeometry();
    Object getId();
    String getIdAsString();
    JSONObject getProperties();
    PhotoProperties getPhotoProperties();
}
