package com.streetography.stphotomap.models.geojson.geometry;

import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONMultiCoordinatesGeometry;

import java.util.ArrayList;

public interface GeoJSONMultiLineString extends GeoJSONMultiCoordinatesGeometry {
    ArrayList<GeoJSONLineString> getLineStrings();
}
