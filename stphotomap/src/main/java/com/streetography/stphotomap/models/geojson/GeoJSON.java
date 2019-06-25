package com.streetography.stphotomap.models.geojson;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.streetography.stphotomap.models.coordinate.Coordinate;
import com.streetography.stphotomap.models.geojson.geometry.*;
import com.streetography.stphotomap.models.geojson.interfaces.*;
import com.streetography.stphotomap.models.geojson.parser.GeoJSONParser;
import com.streetography.stphotomap.models.geojson.properties.PhotoProperties;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class GeoJSON implements GeoJSONProtocol {

    private static GeoJSONParser parser = new GeoJSONParser();

    @Override
    public GeoJSONObject parse(JSONObject geoJSON) {
        return GeoJSON.parser.geoJSONObject(geoJSON);
    }

    //region Point

    /**
     * Creates a new GeoJSONPoint object
     *
     * @param longitude point longitude
     * @param latitude point latitude
     * @return GeoJSONPoint object
     * @throws IllegalArgumentException if coordinates cannot be parsed
     */

    @Override
    public GeoJSONPoint getPoint(Double longitude, Double latitude) throws IllegalArgumentException {
        return new Point(longitude, latitude);
    }

    public static class Point implements GeoJSONPoint {
        Double longitude;
        Double latitude;

        public Point(JSONArray coordinatesJSON) {
            try {
                this.init(coordinatesJSON.getDouble(0), coordinatesJSON.getDouble(1));
            } catch (JSONException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("A valid Point must have at least 2 coordinates");
            }
        }

        public Point(Double longitude, Double latitude) {
            this.init(longitude, latitude);
        }

        private void init(Double longitude, Double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        @Override
        public String toString() {
            return "Point {" + "\n longitude: " + longitude +
                    "\n latitude: " + latitude +
                    "\n}\n";
        }

        @Override
        public ArrayList<Object> getGeoJSONCoordinates() {
            return new ArrayList<Object>(Arrays.asList(longitude, latitude));
        }

        @Override
        public double getLongitude() {
            return longitude;
        }

        @Override
        public double getLatitude() {
            return latitude;
        }

        @Override
        public LatLng getLocationCoordinate() {
            return new LatLng(latitude, longitude);
        }

        @Override
        public ArrayList<GeoJSONGeometry> getGeoJSONGeometry() {
            return null;
        }

        @Override
        public GeoJSONType getGeoJSONType() {
            return GeoJSONType.point;
        }

        @Override
        public GeoJSONBoundingBox getGeoJSONBoundingBox() {
            return new BoundingBox(new BoundingCoordinates(this.longitude, this.latitude, this.longitude, this.latitude));
        }

        @Override
        public JSONObject getGeoJSONDictionary() {
            return null;
        }
    }


    //endregion

    //region Multi Point

    /**
     * Creates a new GeoJSONMultiPoint object
     *
     * @param points array of points
     * @return GeoJSONMultiPoint object
     * @throws IllegalArgumentException if points cannot be parsed
     */

    @Override
    public GeoJSONMultiPoint getMultiPoint(ArrayList<GeoJSONPoint> points) throws IllegalArgumentException {
        return new MultiPoint(points);
    }

    public static class MultiPoint implements GeoJSONMultiPoint {

        ArrayList<GeoJSONPoint> points = new ArrayList<>();

        MultiPoint(ArrayList<GeoJSONPoint> points) {
            this.init(points);
        }

        public MultiPoint(JSONArray coordinatesJSONArray) {
            ArrayList<GeoJSONPoint> points = new ArrayList<>();
            int length = coordinatesJSONArray.length();
            for (int i = 0; i<length; i++) {
                try {
                    Point point =  new Point(coordinatesJSONArray.getJSONArray(i));
                    points.add(point);
                } catch (Exception e){
                    throw new IllegalArgumentException("Invalid Point in MultiPoint");
                }
            }

            this.init(points);
        }

        private void init(ArrayList<GeoJSONPoint> points) {
            if (points.size() >= 1) {
                this.points = points;
            } else {
                throw new IllegalArgumentException("A valid MultiPoint must have at least one Point");
            }
        }

        @Override
        public GeoJSONBoundingBox getGeoJSONBoundingBox() {
            ArrayList<GeoJSONBoundingBox> compactBoundingBox = new ArrayList<>();
            for (GeoJSONPoint point : points) {
                GeoJSONBoundingBox boundingBox = point.getGeoJSONBoundingBox();
                if (boundingBox != null) {
                    compactBoundingBox.add(boundingBox);
                }
            }
            return BoundingBox.getBest(compactBoundingBox);
        }

        @Override
        public ArrayList<Object> getGeoJSONCoordinates() {
            ArrayList<Object> geoJSONCoordinates = new ArrayList<>();
            for (GeoJSONPoint point : points) {
                ArrayList<Object> coordinates = point.getGeoJSONCoordinates();
                if (coordinates != null) {
                    geoJSONCoordinates.add(coordinates);
                }
            }
            return geoJSONCoordinates;
        }

        @Override
        public ArrayList<GeoJSONGeometry> getGeoJSONGeometry() {
            return null;
        }

        @Override
        public ArrayList<GeoJSONPoint> getPoints() {
            return points;
        }

        @Override
        public GeoJSONType getGeoJSONType() {
            return GeoJSONType.multiPoint;
        }

        @Override
        public JSONObject getGeoJSONDictionary() {
            return null;
        }

        @Override
        public String toString() {
            return "MultiPoint {" + "\n points: " + points +
                    "\n}\n";
        }
    }

    //endregion

    //region Polygon

    /**
     * Creates a new GeoJSONPolygon object
     *
     * @param linearRings array of linear rings
     * @return GeoJSONPolygon object
     * @throws IllegalArgumentException if linear rings cannot be parsed
     */

    @Override
    public GeoJSONPolygon getPolygon(ArrayList<GeoJSONLineString> linearRings) throws IllegalArgumentException {
        return new Polygon(linearRings);
    }

    public static class Polygon implements GeoJSONPolygon {

        ArrayList<GeoJSONLineString> linearRings = new ArrayList<>();

        Polygon(ArrayList<GeoJSONLineString> linearRings) {
            this.init(linearRings);
        }

        public Polygon(JSONArray coordinatesJSONArray) {
            ArrayList<GeoJSONLineString> linearRings = new ArrayList<>();
            int length = coordinatesJSONArray.length();
            for (int i = 0; i<length; i++) {
                try {
                    LineString lineString =  new LineString(coordinatesJSONArray.getJSONArray(i));
                    linearRings.add(lineString);
                } catch (Exception e){
                    throw new IllegalArgumentException("Invalid linear ring (LineString) in Polygon");
                }
            }

            this.init(linearRings);
        }

        private void init(ArrayList<GeoJSONLineString> linearRings) {
            if (!(linearRings.size() >= 1)) {
                throw new IllegalArgumentException("A valid Polygon must have at least one LinearRing");
            }

            for (GeoJSONLineString linearRing : linearRings) {
                if (linearRing.getPoints().get(0).equals(linearRing.getPoints().get(linearRing.getPoints().size() - 1))) {
                    throw new IllegalArgumentException("A valid Polygon LinearRing must have the first and last points equal");
                }

                if (!(linearRing.getPoints().size() >= 4)) {
                    throw new IllegalArgumentException("A valid Polygon LinearRing must have at least 4 points");
                }
            }
            this.linearRings = linearRings;
        }

        @Override
        public ArrayList<GeoJSONLineString> getLinearRings() {
            return linearRings;
        }

        @Override
        public GeoJSONType getGeoJSONType() {
            return GeoJSONType.polygon;
        }

        @Override
        public GeoJSONBoundingBox getGeoJSONBoundingBox() {
            ArrayList<GeoJSONBoundingBox> compactBoundingBox = new ArrayList<>();
            for (GeoJSONLineString linearRing : linearRings) {
                GeoJSONBoundingBox boundingBox = linearRing.getGeoJSONBoundingBox();
                if ( boundingBox != null) {
                    compactBoundingBox.add(boundingBox);
                }
            }
            return BoundingBox.getBest(compactBoundingBox);
        }

        @Override
        public ArrayList<Object> getGeoJSONCoordinates() {
            ArrayList<Object> geoJSONCoordinates = new ArrayList<>();
            for (GeoJSONLineString linearRing : linearRings) {
                ArrayList<Object> coordinates = linearRing.getGeoJSONCoordinates();
                if (coordinates != null) {
                    geoJSONCoordinates.add(coordinates);
                }
            }
            return geoJSONCoordinates;
        }

        @Override
        public ArrayList<GeoJSONPoint> getPoints() {
            ArrayList<GeoJSONPoint> points = new ArrayList<>();
            for (GeoJSONLineString linearRing : linearRings) {
                if (linearRing.getPoints() != null) {
                    points.addAll(linearRing.getPoints());
                }
            }
            return points;
        }

        @Override
        public ArrayList<GeoJSONGeometry> getGeoJSONGeometry() {
            return null;
        }

        @Override
        public JSONObject getGeoJSONDictionary() {
            return null;
        }

        @Override
        public String toString() {
            return  "Polygon {" + "\n linear rings: " + linearRings +
                    "\n}\n";
        }
    }


    //endregion

    //region LineString

    /**
     * Creates a new GeoJSONLineString object
     *
     * @param points array of points
     * @return GeoJSONLineString object
     * @throws IllegalArgumentException if points cannot be parsed
     */

    @Override
    public GeoJSONLineString getLineString(ArrayList<GeoJSONPoint> points) throws IllegalArgumentException {
        return new LineString(points);
    }

    public static class LineString implements GeoJSONLineString {

        ArrayList<GeoJSONPoint> points = new ArrayList<>();
        ArrayList<GeoJSONLineSegment> segments = new ArrayList<>();


        public LineString(JSONArray coordinatesJSONArray) {
            ArrayList<GeoJSONPoint> points = new ArrayList<>();
            int length = coordinatesJSONArray.length();
            for (int i = 0; i<length; i++) {
                try {
                    Point point =  new Point(coordinatesJSONArray.getJSONArray(i));
                    points.add(point);
                } catch (Exception e){
                    throw new IllegalArgumentException("Invalid Point in LineString");
                }
            }

            this.init(points);
        }

        LineString(ArrayList<GeoJSONPoint> points) {
            this.init(points);
        }

        private void init(ArrayList<GeoJSONPoint> points) {
            if (points.size() >= 2) {
                this.points = points;
            } else {
                throw new IllegalArgumentException("A valid MultiPoint must have at least two Points");
            }

            int size = points.size();
            for (int offset = 0; offset<size; offset++) {
                if (points.size() == offset + 1) return;

                GeoJSONPoint point = points.get(offset);
                if (point == null) return;

                GeoJSONLineSegment segment = new GeoJSONLineSegment(points.get(offset), points.get(offset+1));
                segments.add(segment);
            }
        }

        @Override
        public ArrayList<Object> getGeoJSONCoordinates() {
            ArrayList<Object> geoJSONCoordinates = new ArrayList<>();
            for (GeoJSONPoint point : points) {
                ArrayList<Object> coordinates = point.getGeoJSONCoordinates();
                if (coordinates != null) {
                    geoJSONCoordinates.add(coordinates);
                }
            }
            return geoJSONCoordinates;
        }

        @Override
        public ArrayList<GeoJSONGeometry> getGeoJSONGeometry() {
            return null;
        }

        @Override
        public GeoJSONBoundingBox getGeoJSONBoundingBox() {
            ArrayList<GeoJSONBoundingBox> compactBoundingBox = new ArrayList<>();
            for (GeoJSONPoint point : points) {
                GeoJSONBoundingBox boundingBox = point.getGeoJSONBoundingBox();
                if (boundingBox != null) {
                    compactBoundingBox.add(boundingBox);
                }
            }
            return BoundingBox.getBest(compactBoundingBox);
        }

        @Override
        public ArrayList<GeoJSONPoint> getPoints() {
            return points;
        }

        @Override
        public GeoJSONType getGeoJSONType() {
            return GeoJSONType.lineString;
        }

        @Override
        public ArrayList<GeoJSONLineSegment> getSegments() {
            return segments;
        }

        @Override
        public JSONObject getGeoJSONDictionary() {
            return null;
        }

        @Override
        public String toString() {
            return "LineString {" + "\n points: " + points +
                    "\n}\n";
        }
    }

    //endregion

    //region MultiLineString

    /**
     * Creates a new GeoJSONMultiLineString object
     *
     * @param lineStrings array of line strings
     * @return GeoJSONMultiLineString object
     * @throws IllegalArgumentException if line strings cannot be parsed
     */

    @Override
    public GeoJSONMultiLineString getMultiLineString(ArrayList<GeoJSONLineString> lineStrings) throws IllegalArgumentException {
        return new MultiLineString(lineStrings);
    }

    public static class MultiLineString implements GeoJSONMultiLineString {

        private ArrayList<GeoJSONLineString> lineStrings = new ArrayList<>();

        public MultiLineString(JSONArray coordinatesJSONArray) {
            ArrayList<GeoJSONLineString> lineStrings = new ArrayList<>();
            int length = coordinatesJSONArray.length();
            for (int i = 0; i<length; i++) {
                try {
                    LineString lineString =  new LineString(coordinatesJSONArray.getJSONArray(i));
                    lineStrings.add(lineString);
                } catch (Exception e){
                    throw new IllegalArgumentException("Invalid LineString in MultiLineString");
                }
            }

            this.init(lineStrings);
        }

        MultiLineString(ArrayList<GeoJSONLineString> lineStrings) {
            this.init(lineStrings);
        }

        private void init(ArrayList<GeoJSONLineString> lineStrings) {
            if (lineStrings.size() >= 1) {
                this.lineStrings = lineStrings;
            } else {
                throw new IllegalArgumentException("A valid MultiLineString must have at least one LineString");
            }
        }

        @Override
        public ArrayList<Object> getGeoJSONCoordinates() {
            ArrayList<Object> geoJSONCoordinates = new ArrayList<>();
            for (GeoJSONLineString lineString : lineStrings) {
                ArrayList<Object> coordinates = lineString.getGeoJSONCoordinates();
                if (coordinates != null) {
                    geoJSONCoordinates.add(coordinates);
                }
            }
            return geoJSONCoordinates;
        }

        @Override
        public ArrayList<GeoJSONGeometry> getGeoJSONGeometry() {
            return null;
        }

        @Override
        public GeoJSONBoundingBox getGeoJSONBoundingBox() {
            ArrayList<GeoJSONBoundingBox> compactBoundingBox = new ArrayList<>();
            for (GeoJSONLineString lineString : lineStrings) {
                GeoJSONBoundingBox boundingBox = lineString.getGeoJSONBoundingBox();
                if (boundingBox != null) {
                    compactBoundingBox.add(boundingBox);
                }
            }
            return BoundingBox.getBest(compactBoundingBox);
        }

        @Override
        public ArrayList<GeoJSONPoint> getPoints() {
            ArrayList<GeoJSONPoint> points = new ArrayList<>();
            for (GeoJSONLineString linearRing : lineStrings) {
                if (linearRing.getPoints() != null) {
                    points.addAll(linearRing.getPoints());
                }
            }
            return points;
        }

        @Override
        public GeoJSONType getGeoJSONType() {
            return GeoJSONType.multiLineString;
        }

        @Override
        public ArrayList<GeoJSONLineString> getLineStrings() {
            return lineStrings;
        }

        @Override
        public JSONObject getGeoJSONDictionary() {
            return null;
        }

        @Override
        public String toString() {
            return  "MultiLineString {" + "\n line strings: " + lineStrings +
                    "\n}\n";
        }
    }

    //endregion

    //region MultiPolygon

    /**
     * Creates a new GeoJSONMultiPolygon object
     *
     * @param polygons array of polygons
     * @return GeoJSONMultiPolygon object
     * @throws IllegalArgumentException if coordinates cannot be parsed
     */

    @Override
    public GeoJSONMultiPolygon getMultiPolygon(ArrayList<GeoJSONPolygon> polygons) throws IllegalArgumentException {
        return new MultiPolygon(polygons);
    }

    public static class MultiPolygon implements GeoJSONMultiPolygon {

        ArrayList<GeoJSONPolygon> polygons = new ArrayList<>();


        public MultiPolygon(JSONArray coordinatesJSONArray) {
            ArrayList<GeoJSONPolygon> polygons = new ArrayList<>();
            int length = coordinatesJSONArray.length();
            for (int i = 0; i<length; i++) {
                try {
                    Polygon polygon =  new Polygon(coordinatesJSONArray.getJSONArray(i));
                    polygons.add(polygon);
                } catch (Exception e){
                    throw new IllegalArgumentException("Invalid Polygon in MultiPolygon");
                }
            }
            this.init(polygons);
        }

        MultiPolygon(ArrayList<GeoJSONPolygon> polygons) {
            this.init(polygons);
        }

        private void init(ArrayList<GeoJSONPolygon> polygons) {
            if (polygons.size() >= 1) {
                this.polygons = polygons;
            } else {
                throw new IllegalArgumentException("A valid MultiPolygon must have at least one Polygon");
            }
        }

        @Override
        public ArrayList<Object> getGeoJSONCoordinates() {
            ArrayList<Object> geoJSONCoordinates = new ArrayList<>();
            for (GeoJSONPolygon polygon : polygons) {
                ArrayList<Object> coordinates = polygon.getGeoJSONCoordinates();
                if (coordinates != null) {
                    geoJSONCoordinates.add(coordinates);
                }
            }
            return geoJSONCoordinates;
        }

        @Override
        public ArrayList<GeoJSONGeometry> getGeoJSONGeometry() {
            return null;
        }

        @Override
        public GeoJSONBoundingBox getGeoJSONBoundingBox() {
            ArrayList<GeoJSONBoundingBox> compactBoundingBox = new ArrayList<>();
            for (GeoJSONPolygon polygon : polygons) {
                GeoJSONBoundingBox boundingBox = polygon.getGeoJSONBoundingBox();
                if (boundingBox != null) {
                    compactBoundingBox.add(boundingBox);
                }
            }
            return BoundingBox.getBest(compactBoundingBox);
        }

        @Override
        public ArrayList<GeoJSONPoint> getPoints() {
            ArrayList<GeoJSONPoint> points = new ArrayList<>();
            for (GeoJSONPolygon linearRing : polygons) {
                if (linearRing.getPoints() != null) {
                    points.addAll(linearRing.getPoints());
                }
            }
            return points;
        }

        @Override
        public GeoJSONType getGeoJSONType() {
            return GeoJSONType.multiPolygon;
        }

        @Override
        public ArrayList<GeoJSONPolygon> getPolygons() {
            return polygons;
        }

        @Override
        public JSONObject getGeoJSONDictionary() {
            return null;
        }

        @Override
        public String toString() {
            return "MultiPolygon {" + "\n polygons: " + polygons +
                    "\n}\n";
        }
    }

    //endregion

    //region GeometryCollection

    /**
     * Creates a new GeoJSONGeometryCollection object
     *
     * @param geometries array of geometries
     * @return GeoJSONGeometryCollection object
     * @throws IllegalArgumentException if geometries cannot be parsed
     */

    @Override
    public GeoJSONGeometryCollection getGeometryCollection(ArrayList<GeoJSONGeometry> geometries) throws IllegalArgumentException {
        return new GeometryCollection(geometries);
    }

    public static class GeometryCollection implements GeoJSONGeometryCollection {

        ArrayList<GeoJSONGeometry> objectGeometries = new ArrayList<>();
        GeoJSONBoundingBox objectBoundingBox;


        public GeometryCollection(JSONObject geoJSONDictionary) {
            ArrayList<GeoJSONGeometry> geometries = new ArrayList<>();

            try {
                JSONArray geometriesJSON = geoJSONDictionary.getJSONArray("geometries");
                int length = geometriesJSON.length();
                for (int i = 0; i<length; i++) {
                    GeoJSONGeometry geometry = (GeoJSONGeometry) parser.geoJSONObject(geometriesJSON.getJSONObject(i));
                    if (geometry != null) {
                        geometries.add(geometry);
                    } else {
                        throw new IllegalArgumentException("Invalid Geometry for GeometryCollection");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("A valid GeometryCollection must have a geometries");
            }
            this.init(geometries);
        }

        GeometryCollection(ArrayList<GeoJSONGeometry> geometries) {
            this.init(geometries);
        }

        private void init(ArrayList<GeoJSONGeometry> geometries) {
            this.objectGeometries = geometries;

            ArrayList<GeoJSONBoundingBox> compactBoundingBox = new ArrayList<>();
            for (GeoJSONGeometry geometry : geometries) {
                GeoJSONBoundingBox boundingBox = geometry.getGeoJSONBoundingBox();
                if (boundingBox != null) {
                    compactBoundingBox.add(boundingBox);
                }
            }
            objectBoundingBox = BoundingBox.getBest(compactBoundingBox);
        }

        @Override
        public GeoJSONType getGeoJSONType() {
            return GeoJSONType.geometryCollection;
        }

        @Override
        public ArrayList<GeoJSONGeometry> getGeoJSONGeometry() {
            return objectGeometries;
        }

        @Override
        public GeoJSONBoundingBox getGeoJSONBoundingBox() {
            return objectBoundingBox;
        }

        @Override
        public JSONObject getGeoJSONDictionary() {
            ArrayList<JSONObject> geometries = new ArrayList<>();
            for (GeoJSONGeometry dictionary : objectGeometries) {
                JSONObject geoJSONDictionary = dictionary.getGeoJSONDictionary();
                if (geoJSONDictionary != null) {
                    geometries.add(geoJSONDictionary);
                }
            }

            JSONObject geoJSONDictionary = new JSONObject();
            try {
                geoJSONDictionary.put("type", getGeoJSONType().getKey());
                geoJSONDictionary.put("geometries", geometries);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return geoJSONDictionary;
        }

        @Override
        public String toString() {
            return "GeometryCollection {" + "\n geometries: " + objectGeometries +
                    "\n}\n";
        }
    }

    //endregion

    //region Feature

    /**
     * Creates a new GeoJSONFeature object
     *
     * @param geometry feature geometry
     * @param id feature id
     * @param properties photos properties
     * @return GeoJSONFeature object
     * @throws IllegalArgumentException if feature cannot be parsed
     */

    @Override
    public GeoJSONFeature getFeature(GeoJSONGeometry geometry, Object id, JSONObject properties) throws IllegalArgumentException {
        return new Feature(geometry, id, properties);
    }

    public static class Feature implements GeoJSONFeature {

        public GeoJSONGeometry geometry;
        JSONObject properties;
        ArrayList<GeoJSONGeometry> objectGeometries = new ArrayList<>();
        GeoJSONBoundingBox objectBoundingBox;

        String idString;
        Double idDouble;
        Integer idInteger;

        public Feature(JSONObject geoJSONDictionary) {
            try {

                Object idObject = geoJSONDictionary.get("id");
                JSONObject propertiesJSONObject = geoJSONDictionary.getJSONObject("properties");
                JSONObject geometryJSONObject = geoJSONDictionary.getJSONObject("geometry");

                String id = null;
                if (idObject != null && idObject instanceof String) id = (String) idObject;
                if (idObject != null && idObject instanceof Double) id = idObject.toString();
                if (idObject != null && idObject instanceof Integer) id = idObject.toString();

                if (geometryJSONObject == null) {
                    this.init(null, id, propertiesJSONObject);
                }

                GeoJSONGeometry geometry = (GeoJSONGeometry) parser.geoJSONObject(geometryJSONObject);
                if (geometry == null) {
                    throw new IllegalArgumentException("Feature must contain a valid geometry");
                }

                this.init(geometry, id, propertiesJSONObject);

            } catch (JSONException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Feature must contain a valid geometry");
            }
        }

        Feature(GeoJSONGeometry geometry, Object id, JSONObject properties) {
            this.init(geometry, id, properties);
        }

        private void init(GeoJSONGeometry geometry, Object id, JSONObject properties) {
            this.geometry = geometry;
            if (id instanceof String) {
                this.idString = String.valueOf(id);
            }
            if (id instanceof Double) {
                this.idDouble = (Double) id;
            }
            if (id instanceof Integer) {
                this.idInteger = (Integer) id;
            }

            this.properties = properties;

            if (geometry != null) {
                ArrayList<GeoJSONGeometry> geometries = new ArrayList<>();
                geometries.add(geometry);
                objectGeometries = geometries;
                objectBoundingBox = geometry.getGeoJSONBoundingBox();
            }
        }

        @Override
        public GeoJSONGeometry getGeometry() {
            return geometry;
        }

        @Override
        public Object getId() {
            if (idString != null) return idString;
            if (idDouble != null) return idDouble;
            if (idInteger != null) return  idInteger;
            return null;
        }

        @Override
        public String getIdAsString() {
            if (idString != null) return idString;
            if (idDouble != null) return idDouble.toString();
            if (idInteger != null) return idInteger.toString();
            return null;
        }

        @Override
        public GeoJSONType getGeoJSONType() {
            return GeoJSONType.feature;
        }

        @Override
        public JSONObject getProperties() {
            return properties;
        }

        @Override
        public ArrayList<GeoJSONGeometry> getGeoJSONGeometry() {
            return objectGeometries;
        }

        @Override
        public GeoJSONBoundingBox getGeoJSONBoundingBox() {
            return objectBoundingBox;
        }

        @Override
        public JSONObject getGeoJSONDictionary() {
            JSONObject geoJSONDictionary = new JSONObject();
            try {
                geoJSONDictionary.put("type", getGeoJSONType().getKey());
                geoJSONDictionary.put("geometries", geometry.getGeoJSONDictionary());
                geoJSONDictionary.put("properties", properties);

                if (getId() != null) {
                    geoJSONDictionary.put("id", getId());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return geoJSONDictionary;
        }

        @Override
        public PhotoProperties getPhotoProperties() {
            if (properties == null) {
                Log.i("getPhotoProperties", "There are no feature properties.");
                return null;
            }

            PhotoProperties featureProperties = new PhotoProperties();
            try {

                featureProperties.type = properties.optString("type");
                featureProperties.name = properties.optString("name");
                featureProperties.photoId = properties.optString("photoId");
                featureProperties.photoCount = properties.optInt("photoCount");
                featureProperties.image60Url = properties.optString("image60URL");
                featureProperties.image250Url = properties.optString("image250URL");

                if (properties.has("photoLocation") && !properties.isNull("photoLocation")) {
                    JSONArray locationJSONArray = properties.getJSONArray("photoLocation");
                    if (locationJSONArray.length() >= 2) {
                        Double longitude = locationJSONArray.getDouble(0);
                        Double latitude = locationJSONArray.getDouble(1);
                        featureProperties.photoLocation = new Coordinate(longitude, latitude);
                    }
                }

                return featureProperties;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String toString() {
            return "Feature {" + "\n geometry: " + geometry +
                    "\n}\n";
        }
    }

    //endregion

    //region FeatureCollection

    /**
     * Creates a new GeoJSONFeatureCollection object
     *
     * @param features array of GeoJSONFeature objects to add to the GeoJSONFeatureCollection
     * @return GeoJSONFeatureCollection object
     * @throws IllegalArgumentException if featuresJSON cannot be parsed
     */

    @Override
    public GeoJSONFeatureCollection getFeatureCollection(ArrayList<GeoJSONFeature> features) throws IllegalArgumentException {
        return new FeatureCollection(features);
    }

    public static class FeatureCollection implements GeoJSONFeatureCollection {
        ArrayList<GeoJSONFeature> features = new ArrayList<>();

        ArrayList<GeoJSONGeometry> objectGeometries = new ArrayList<>();
        GeoJSONBoundingBox objectBoundingBox;

        FeatureCollection(ArrayList<GeoJSONFeature> features) {
            this.init(features);
        }

        public FeatureCollection(JSONObject geoJSONDictionary) {

            ArrayList<GeoJSONFeature> features = new ArrayList<>();
            try {
                JSONArray featuresJSON = geoJSONDictionary.getJSONArray("features");
                int length = featuresJSON.length();
                for (int i = 0; i < length; i++) {
                    try {
                        Feature feature = new Feature(featuresJSON.getJSONObject(i));
                        features.add(feature);
                    } catch (Exception e){
                        throw new IllegalArgumentException("Invalid Feature in FeatureCollection");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("A valid FeatureCollection must have a features key.");
            }

            this.init(features);
        }

        private void init(ArrayList<GeoJSONFeature> features) {
            if (features.size() >= 1) {
                this.features = features;
            }

            ArrayList<GeoJSONGeometry> geometries = new ArrayList<>();
            for (GeoJSONFeature feature : features) {
                ArrayList<GeoJSONGeometry> geoJSONGeometry = feature.getGeoJSONGeometry();
                if (geoJSONGeometry != null) {
                    geometries.addAll(geoJSONGeometry);
                }
            }

            if (geometries.size() > 0 ) {
                this.objectGeometries = geometries;
            } else {
                this.objectGeometries = null;
            }

            ArrayList<GeoJSONBoundingBox> compactBoundingBox = new ArrayList<>();
            for (GeoJSONGeometry geometry : geometries) {
                GeoJSONBoundingBox geoJSONBoundingBox = geometry.getGeoJSONBoundingBox();
                if (geoJSONBoundingBox != null) {
                    compactBoundingBox.add(geoJSONBoundingBox);
                }
            }
            objectBoundingBox = BoundingBox.getBest(compactBoundingBox);
        }
        @Override
        public GeoJSONType getGeoJSONType() {
            return GeoJSONType.featureCollection;
        }

        @Override
        public ArrayList<GeoJSONFeature> getFeatures() {
            return features;
        }

        @Override
        public ArrayList<GeoJSONGeometry> getGeoJSONGeometry() {
            return objectGeometries;
        }

        @Override
        public GeoJSONBoundingBox getGeoJSONBoundingBox() {
            return objectBoundingBox;
        }

        @Override
        public JSONObject getGeoJSONDictionary() {
            JSONObject geoJSONDictionary = new JSONObject();
            try {
                geoJSONDictionary.put("type", getGeoJSONType().getKey());

                ArrayList<JSONObject> features = new ArrayList<>();
                for (GeoJSONFeature feature: this.features) {
                    JSONObject geoJSON = feature.getGeoJSONDictionary();
                    if (geoJSON != null) {
                        features.add(geoJSON);
                    }
                }
                geoJSONDictionary.put("features", features);

                return geoJSONDictionary;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return geoJSONDictionary;
        }

        @Override
        public String toString() {
            return "FeatureCollection {" + "\n features: " + features +
                    "\n}\n";
        }
    }

    //endregion
}
