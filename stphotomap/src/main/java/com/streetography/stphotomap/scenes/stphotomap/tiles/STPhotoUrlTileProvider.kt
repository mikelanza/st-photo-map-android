package com.streetography.stphotomap.scenes.stphotomap.tiles

import com.google.android.gms.maps.model.UrlTileProvider
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.scenes.stphotomap.builders.STPhotoMapUriBuilder
import java.net.MalformedURLException
import java.net.URL

class STPhotoUrlTileProvider(p0: Int, p1: Int) : UrlTileProvider(p0, p1) {
    override fun getTileUrl(x: Int, y: Int, zoom: Int): URL {
        val uri = STPhotoMapUriBuilder().jpegTileUri(TileCoordinate(zoom, x, y)).second
        try {
            return URL(uri)
        } catch (e: MalformedURLException) {
            throw AssertionError(e)
        }
    }
}