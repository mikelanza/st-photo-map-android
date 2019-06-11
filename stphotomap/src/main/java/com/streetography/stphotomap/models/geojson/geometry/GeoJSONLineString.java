package com.streetography.stphotomap.models.geojson.geometry;

import com.streetography.stphotomap.models.geojson.GeoJSONLineSegment;
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONMultiCoordinatesGeometry;

import java.util.ArrayList;

public interface GeoJSONLineString extends GeoJSONMultiCoordinatesGeometry {
    ArrayList<GeoJSONLineSegment> getSegments();
}
