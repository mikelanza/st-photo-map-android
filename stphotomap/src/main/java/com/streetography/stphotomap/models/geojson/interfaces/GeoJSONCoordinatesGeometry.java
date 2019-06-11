package com.streetography.stphotomap.models.geojson.interfaces;

import java.util.ArrayList;

public interface GeoJSONCoordinatesGeometry extends GeoJSONGeometry {
    ArrayList<Object> getGeoJSONCoordinates();
    ArrayList<GeoJSONGeometry> getGeoJSONGeometry();
    GeoJSONBoundingBox getGeoJSONBoundingBox();
}
