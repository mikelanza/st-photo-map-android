package com.streetography.stphotomap.scenes.stphotomap.interactor

import com.streetography.stphotomap.R
import com.streetography.stphotomap.models.geojson.GeoJSON
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import org.json.JSONObject

fun STPhotoMapInteractor.getCarouselGeojson(): GeoJSONObject {
    val inputStream = context?.resources?.openRawResource(R.raw.carousel_geojson)
    val inputString = inputStream?.bufferedReader().use { it?.readText() }
    inputStream?.close()
    return GeoJSON().parse( JSONObject(inputString))
}