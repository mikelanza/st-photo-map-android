package com.streetography.stphotomap.models.geojson;

public class BoundingCoordinates {
    public Double minLongitude;
    public Double minLatitude;
    public Double maxLongitude;
    public Double maxLatitude;

    public BoundingCoordinates(Double minLongitude, Double minLatitude, Double maxLongitude, Double maxLatitude) {
        this.minLongitude = minLongitude;
        this.minLatitude = minLatitude;
        this.maxLongitude = maxLongitude;
        this.maxLatitude = maxLatitude;
    }
}
