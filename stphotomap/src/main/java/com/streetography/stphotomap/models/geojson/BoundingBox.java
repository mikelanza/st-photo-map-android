package com.streetography.stphotomap.models.geojson;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.streetography.stphotomap.models.coordinate.Coordinate;
import com.streetography.stphotomap.models.coordinate.GeodesicPoint;
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONBoundingBox;

import java.util.ArrayList;

public class BoundingBox implements GeoJSONBoundingBox {
    private Double minLongitude;
    private Double minLatitude;
    private Double maxLongitude;
    private Double maxLatitude;

    private Double longitudeDelta;
    private Double latitudeDelta;

    private ArrayList<GeodesicPoint> points;
    private GeodesicPoint centroid;

    public BoundingBox(BoundingCoordinates boundingCoordinates) {
        this.minLongitude = boundingCoordinates.minLongitude;
        this.minLatitude = boundingCoordinates.minLatitude;
        this.maxLongitude = boundingCoordinates.maxLongitude;
        this.maxLatitude = boundingCoordinates.maxLatitude;

        this.points = new ArrayList<>();
        this.points.add(new Coordinate(minLongitude, minLatitude));
        this.points.add(new Coordinate(minLongitude, maxLatitude));
        this.points.add(new Coordinate(maxLongitude, maxLatitude));
        this.points.add(new Coordinate(maxLongitude, minLatitude));

        this.longitudeDelta = maxLongitude - minLongitude;
        this.latitudeDelta = maxLatitude - minLatitude;

        this.centroid = new Coordinate(maxLongitude - (longitudeDelta / 2), maxLatitude - (latitudeDelta / 2));
    }

    public String getDescription() {
        return String.valueOf(minLongitude) +
                "," + String.valueOf(minLatitude) +
                "," + String.valueOf(maxLongitude) +
                "," + String.valueOf(maxLatitude);
    }

    @Override
    public GeoJSONBoundingBox best(ArrayList<GeoJSONBoundingBox> boundingBoxes) {

        if (!(boundingBoxes.size() > 1)) return null;

        Double minLongitude = boundingBoxes.get(0).getMinLongitude();
        Double minLatitude = boundingBoxes.get(0).getMinLatitude();

        Double maxLongitude = boundingBoxes.get(0).getMaxLongitude();
        Double maxLatitude = boundingBoxes.get(0).getMaxLatitude();


        for (GeoJSONBoundingBox boundingBox: boundingBoxes) {
            if (boundingBox.getMinLongitude() < minLongitude) minLongitude = boundingBox.getMinLongitude();
            if (boundingBox.getMinLatitude() < minLatitude) minLatitude = boundingBox.getMinLatitude();

            if (boundingBox.getMaxLongitude() > maxLongitude) maxLongitude = boundingBox.getMaxLongitude();
            if (boundingBox.getMaxLatitude() > maxLatitude) maxLatitude = boundingBox.getMaxLatitude();
        }

        BoundingCoordinates boundingCoordinates = new BoundingCoordinates(minLongitude, minLatitude, maxLongitude, maxLatitude);
        return new BoundingBox(boundingCoordinates);
    }

    @Override
    public Double getMinLongitude() {
        return minLongitude;
    }

    @Override
    public Double getMinLatitude() {
        return minLatitude;
    }

    @Override
    public Double getMaxLongitude() {
        return maxLongitude;
    }

    @Override
    public Double getMaxLatitude() {
        return maxLatitude;
    }

    @Override
    public Double getLongitudeDelta() {
        return longitudeDelta;
    }

    @Override
    public Double getLatitudeDelta() {
        return latitudeDelta;
    }

    @Override
    public ArrayList<GeodesicPoint> getPoints() {
        return points;
    }

    @Override
    public GeodesicPoint getCentroid() {
        return centroid;
    }

    @Override
    public BoundingCoordinates getBoundingCoordinates() {
        return new BoundingCoordinates(minLongitude, minLatitude, maxLongitude, maxLatitude);
    }

    static GeoJSONBoundingBox getBest(ArrayList<GeoJSONBoundingBox> boundingBoxes) {
        if (boundingBoxes.isEmpty()) return null;
        GeoJSONBoundingBox firstBoundingBox = boundingBoxes.get(0);
        if (firstBoundingBox == null) return null;
        boundingBoxes.remove(0);
        if (boundingBoxes.isEmpty()) return firstBoundingBox;
        return firstBoundingBox.best(boundingBoxes);
    }

    public boolean equals(GeoJSONBoundingBox geoJSONBoundingBox) {
        return Double.compare(minLongitude, geoJSONBoundingBox.getMinLongitude()) == 0
                && Double.compare(minLatitude, geoJSONBoundingBox.getMinLatitude()) == 0
                && Double.compare(maxLongitude, geoJSONBoundingBox.getMaxLongitude()) == 0
                && Double.compare(maxLatitude, geoJSONBoundingBox.getMaxLatitude()) == 0;
    }

    public LatLngBounds getLatLagBounds() {
        LatLng southwest = new LatLng(minLatitude, minLongitude);
        LatLng northeast = new LatLng(maxLatitude, maxLongitude);
        return new LatLngBounds(southwest, northeast);
    }
}
