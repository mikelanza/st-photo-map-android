package com.streetography.stphotomap.models.geojson;

import com.streetography.stphotomap.models.coordinate.GeodesicPoint;

public class GeoJSONLineSegment {
    public GeodesicPoint point1;
    public GeodesicPoint point2;

    public GeoJSONLineSegment(GeodesicPoint point1, GeodesicPoint point2) {
        this.point1 = point1;
        this.point2 = point2;
    }
}
