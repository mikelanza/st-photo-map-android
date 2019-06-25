package com.streetography.stphotomap.models.geojson.interfaces;

import com.streetography.stphotomap.models.coordinate.GeodesicPoint;
import com.streetography.stphotomap.models.geojson.BoundingCoordinates;

import java.util.ArrayList;

public interface GeoJSONBoundingBox {
    Double getMinLongitude();
    Double getMinLatitude();
    Double getMaxLongitude();
    Double getMaxLatitude();

    Double getLongitudeDelta();
    Double getLatitudeDelta();

    ArrayList<GeodesicPoint> getPoints();
    GeodesicPoint getCentroid();
    BoundingCoordinates getBoundingCoordinates();

    GeoJSONBoundingBox best(ArrayList<GeoJSONBoundingBox> boundingBoxes);
}
