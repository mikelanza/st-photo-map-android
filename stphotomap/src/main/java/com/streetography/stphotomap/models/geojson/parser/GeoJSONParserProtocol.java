package com.streetography.stphotomap.models.geojson.parser;

import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject;
import org.json.JSONObject;

public interface GeoJSONParserProtocol {
    GeoJSONObject geoJSONObject(JSONObject geoJSON);
}
