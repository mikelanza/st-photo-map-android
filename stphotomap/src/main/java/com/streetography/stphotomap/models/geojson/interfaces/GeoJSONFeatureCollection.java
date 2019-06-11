package com.streetography.stphotomap.models.geojson.interfaces;

import java.util.ArrayList;

public interface GeoJSONFeatureCollection extends GeoJSONObject {
    ArrayList<GeoJSONFeature> getFeatures();
}
