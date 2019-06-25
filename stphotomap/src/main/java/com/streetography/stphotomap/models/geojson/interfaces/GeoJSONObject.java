package com.streetography.stphotomap.models.geojson.interfaces;

import com.streetography.stphotomap.models.geojson.GeoJSONType;
import org.json.JSONObject;

import java.util.ArrayList;

public interface GeoJSONObject {
    GeoJSONType getGeoJSONType();
    ArrayList<GeoJSONGeometry> getGeoJSONGeometry();
    GeoJSONBoundingBox getGeoJSONBoundingBox();
    JSONObject getGeoJSONDictionary();
}
