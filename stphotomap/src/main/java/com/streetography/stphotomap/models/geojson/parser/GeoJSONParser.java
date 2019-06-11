package com.streetography.stphotomap.models.geojson.parser;

import android.util.Log;
import com.streetography.stphotomap.models.geojson.GeoJSON;
import com.streetography.stphotomap.models.geojson.GeoJSONType;
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONGeometry;
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoJSONParser implements  GeoJSONParserProtocol {

    @Override
    public GeoJSONObject geoJSONObject(JSONObject geoJSON) {
        GeoJSONType type = getJSONType(geoJSON);
        if (type == null) return null;

        try {
            switch (type) {
                case feature: return new GeoJSON.Feature(geoJSON);
                case featureCollection: return new GeoJSON.FeatureCollection(geoJSON);
                default: return geometry(geoJSON, type);
            }
        } catch (IllegalArgumentException e) {
          return null;
        }
    }

    private GeoJSONType getJSONType(JSONObject geoJSONDictionary) {
        String typeString = null;
        try {
            typeString = (String) geoJSONDictionary.get("type");

            GeoJSONType type = GeoJSONType.getType(typeString);
            if (type == null) {
                Log.i("type", "Invalid GeoJSON Geometry type: " + typeString);
                return null;
            }

            return type;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("getJSONType", "A valid geoJSON must have a type key: String : " + geoJSONDictionary);
            return null;
        }
    }

    private GeoJSONGeometry geometry(JSONObject geoJSONDictionary, GeoJSONType geoJSONType) {
        if (geoJSONType.equals(GeoJSONType.geometryCollection)) {
            return new GeoJSON.GeometryCollection(geoJSONDictionary);
        }

        try {
            JSONArray coordinates = (JSONArray) geoJSONDictionary.get("coordinates");

            if (coordinates == null) {
                Log.i("geometry", "A valid GeoJSON Coordinates Geometry must have a valid coordinates array: String:" + geoJSONDictionary);
                return null;
            }

            switch (geoJSONType) {
                case point: return new GeoJSON.Point(coordinates);
                case multiPoint: return new GeoJSON.MultiPoint(coordinates);
                case lineString: return new GeoJSON.LineString(coordinates);
                case multiLineString: return new GeoJSON.MultiLineString(coordinates);
                case polygon: return new GeoJSON.Polygon(coordinates);
                case multiPolygon: return new GeoJSON.MultiPolygon(coordinates);
                default:
                    Log.i("geometry", geoJSONType.getKey() + " is not a valid Coordinates Geometry.");
                    return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
