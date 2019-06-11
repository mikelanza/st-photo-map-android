package com.streetography.stphotomap.models.geojson.interfaces;

import com.streetography.stphotomap.models.geojson.geometry.*;
import org.json.JSONObject;

import java.util.ArrayList;

public interface GeoJSONProtocol {
    GeoJSONObject parse(JSONObject geoJSON);
    GeoJSONFeatureCollection getFeatureCollection(ArrayList<GeoJSONFeature> features);
    GeoJSONFeature getFeature(GeoJSONGeometry geometry, Object id, JSONObject properties);
    GeoJSONGeometryCollection getGeometryCollection(ArrayList<GeoJSONGeometry> geometries);
    GeoJSONMultiPolygon getMultiPolygon(ArrayList<GeoJSONPolygon> polygons);
    GeoJSONPolygon getPolygon(ArrayList<GeoJSONLineString> linearRings);
    GeoJSONMultiLineString getMultiLineString(ArrayList<GeoJSONLineString> lineStrings);
    GeoJSONLineString getLineString(ArrayList<GeoJSONPoint> points);
    GeoJSONMultiPoint getMultiPoint(ArrayList<GeoJSONPoint> points);
    GeoJSONPoint getPoint(Double longitude, Double latitude);
}
