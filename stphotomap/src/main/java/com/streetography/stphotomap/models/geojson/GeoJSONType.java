package com.streetography.stphotomap.models.geojson;

public enum GeoJSONType {
    point ("Point"),
    lineString ("LineString"),
    polygon ("Polygon"),
    multiPoint ("MultiPoint"),
    multiLineString ("MultiLineString"),
    multiPolygon ("MultiPolygon"),
    geometryCollection ("GeometryCollection"),
    feature ("Feature"),
    featureCollection ("FeatureCollection");

    private final String key;

    GeoJSONType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean equals(String otherValue) {
        return key.equals(otherValue);
    }

    public static GeoJSONType getType(String key) {
        if (key != null && key.isEmpty() == false) {
            for (GeoJSONType type : values()) {
                if (type.getKey().equalsIgnoreCase(key)) {
                    return type;
                }
            }
        }
        return null;
    }
}
