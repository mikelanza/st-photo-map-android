package com.streetography.stphotomap.scenes.stphotomap.seeds

import com.google.android.gms.maps.model.LatLng
import com.streetography.stphotomap.models.geojson.GeoJSON
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.location.STLocation
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapModels
import com.streetography.stphotomap.scenes.stphotomap.markers.PhotoMarker
import org.json.JSONObject

class STPhotoMapSeeds {
    val tileCoordinate = TileCoordinate(10, 1, 2)
    val tileCoordinates = arrayListOf(
        TileCoordinate(10, 1, 3),
        TileCoordinate(11, 2, 4),
        TileCoordinate(12, 3, 5))

    val imageUrl = "https://strtgrph.s3-us-west-1.amazonaws.com/processed/81d50005056139e81c0ddd2d98e0ec0e_60_60.jpg"
    val coordinate = LatLng(50.0, 50.0)
    val location = STLocation(50.0, 50.0)

    fun geojsonObject(): GeoJSONObject {
        val jsonObject = this.loadJSONObjectFromFile("geojson_object.json")
        return GeoJSON().parse(jsonObject)
    }

    fun locationGeojsonObject(): GeoJSONObject {
        val jsonObject = this.loadJSONObjectFromFile("geojson_object_location.json")
        return GeoJSON().parse(jsonObject)
    }

    private fun loadJSONObjectFromFile(filename: String): JSONObject {
        val inputStream = this.javaClass.classLoader?.getResourceAsStream(filename)
        val inputString = inputStream?.bufferedReader().use { it?.readText() }
        inputStream?.close()

        return JSONObject(inputString)
    }

    fun markers(): ArrayList<STPhotoMapModels.Marker> {
        val firstMarker = STPhotoMapModels.Marker("1", "https://strtgrph.s3-us-west-1.amazonaws.com/processed/964d83ac70036166b4fb43c93516ab25_250_250.jpg",37.896175586962535, -122.5092990375)
        val secondMarker = STPhotoMapModels.Marker("2","https://strtgrph.s3-us-west-1.amazonaws.com/processed/1575c2eeef87a57256a03b8e6d9d8eec_250_250.jpg",37.92717416710873,-122.51521212095439)
        return arrayListOf(firstMarker, secondMarker)
    }

    fun photoMarkers(): ArrayList<PhotoMarker> {
        val photoMarkers = this.markers().map {
            it.toPhotoMarker()
        }
        return ArrayList(photoMarkers)
    }
}