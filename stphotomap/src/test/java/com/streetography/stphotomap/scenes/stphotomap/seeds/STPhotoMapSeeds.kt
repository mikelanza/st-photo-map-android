package com.streetography.stphotomap.scenes.stphotomap.seeds

import com.google.android.gms.maps.model.LatLng
import com.streetography.stphotomap.models.geojson.GeoJSON
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.location.STLocation
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import org.json.JSONObject

class STPhotoMapSeeds {
    val tileCoordinate = TileCoordinate(10, 1, 2)
    val tileCoordinates = arrayListOf(
        TileCoordinate(10, 1, 3),
        TileCoordinate(11, 2, 4),
        TileCoordinate(12, 3, 5))

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
}