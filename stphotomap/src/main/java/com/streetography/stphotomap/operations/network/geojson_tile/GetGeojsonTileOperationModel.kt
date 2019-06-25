package com.streetography.stphotomap.operations.network.geojson_tile

import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate

class GetGeojsonTileOperationModel {
    class Request(val tileCoordinate: TileCoordinate, val url: String)
    class Response(val geoJSONObject: GeoJSONObject)
}