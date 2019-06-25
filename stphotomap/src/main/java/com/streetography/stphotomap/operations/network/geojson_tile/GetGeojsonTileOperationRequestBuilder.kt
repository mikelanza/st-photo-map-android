package com.streetography.stphotomap.operations.network.geojson_tile

import okhttp3.Request

class GetGeojsonTileOperationRequestBuilder(val model: GetGeojsonTileOperationModel.Request) {
    fun request(): Request {
        return Request.Builder().url(this.model.url).build()
    }
}