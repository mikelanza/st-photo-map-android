package com.streetography.stphotomap.scenes.stphotomap.builders

import android.net.Uri
import com.streetography.stphotomap.BuildConfig
import com.streetography.stphotomap.extensions.uri.addParameters
import com.streetography.stphotomap.extensions.uri.excludeParameter
import com.streetography.stphotomap.models.parameters.KeyValue
import com.streetography.stphotomap.models.parameters.Parameters
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.scenes.stphotomap.parameters.STPhotoMapParametersHandler

class STPhotoMapUriBuilder {
    fun geojsonTileUri(tileCoordinate: TileCoordinate, parameters: ArrayList<KeyValue> = STPhotoMapParametersHandler.instance.parameters): Pair<String, String> {
        return this.tileUri(BuildConfig.GEOJSON_TILE_URL, tileCoordinate, parameters)
    }

    fun jpegTileUri(tileCoordinate: TileCoordinate, parameters: ArrayList<KeyValue> =  STPhotoMapParametersHandler.instance.parameters): Pair<String, String> {
        return this.tileUri(BuildConfig.JPEG_TILE_URL, tileCoordinate, parameters)
    }

    private fun tileUri(template: String, tileCoordinate: TileCoordinate, parameters: ArrayList<KeyValue> =  STPhotoMapParametersHandler.instance.parameters): Pair<String, String> {
        val downloadUri = this.tileUri(template, tileCoordinate.zoom, tileCoordinate.x, tileCoordinate.y, parameters)
        val keyUri = downloadUri.excludeParameter(KeyValue(Parameters.Keys.bbox, ""))
        return Pair(keyUri.toString(), downloadUri.toString())
    }

    fun tileUri(template: String, z: Int, x: Int, y: Int, parameters: ArrayList<KeyValue> =  STPhotoMapParametersHandler.instance.parameters): Uri {
        val uriString = String.format(template, z, x, y)
        val uri = Uri.parse(uriString)
        return uri.addParameters(parameters)
    }
}