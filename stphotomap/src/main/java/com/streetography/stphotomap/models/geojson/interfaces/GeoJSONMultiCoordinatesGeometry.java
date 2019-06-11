package com.streetography.stphotomap.models.geojson.interfaces;

import com.streetography.stphotomap.models.geojson.geometry.GeoJSONPoint;

import java.util.ArrayList;

public interface GeoJSONMultiCoordinatesGeometry extends GeoJSONCoordinatesGeometry {
        ArrayList<GeoJSONPoint> getPoints();
}
