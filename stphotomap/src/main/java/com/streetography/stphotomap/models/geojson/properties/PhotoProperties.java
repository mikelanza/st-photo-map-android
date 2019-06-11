package com.streetography.stphotomap.models.geojson.properties;

import com.streetography.stphotomap.models.coordinate.Coordinate;

public class PhotoProperties {
    public String type  = "";
    public String name = "";
    public String photoId  = "";
    public String image60Url = "";
    public String image250Url = "";
    public Integer photoCount = 0;
    public Coordinate photoLocation;

    public PhotoProperties() {
    }
}
