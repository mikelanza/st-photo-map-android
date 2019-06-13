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
        val inputStream = this.javaClass.classLoader?.getResourceAsStream("geojson_object.json")
        val inputString = inputStream?.bufferedReader().use { it?.readText() }
        inputStream?.close()

        val jsonObject = JSONObject(inputString)
        return GeoJSON().parse(jsonObject)

    }

    fun locationGeojsonObject(): GeoJSONObject {
        val inputStream = this.javaClass.classLoader?.getResourceAsStream("geojson_object_location.json")
        val inputString = inputStream?.bufferedReader().use { it?.readText() }
        inputStream?.close()

        val jsonObject = JSONObject(inputString)
        return GeoJSON().parse(jsonObject)
    }
}