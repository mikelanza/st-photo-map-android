package com.streetography.stphotomap.scenes.stphotomap.cache

import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import java.util.concurrent.CopyOnWriteArrayList

class STPhotoMapGeojsonCache() {
    class Tile(val keyUrl: String, var geojsonObject: GeoJSONObject) {
    }

    var tiles: CopyOnWriteArrayList<Tile>

    init {
        this.tiles = CopyOnWriteArrayList()
    }

    fun addTile(tile: Tile) {
        this.tiles.add(tile)
    }

    fun removeAllTiles() {
        this.tiles.clear()
    }

    fun getTiles(urls: ArrayList<String>): ArrayList<Tile> {
        return ArrayList(this.tiles.filter { urls.contains(it.keyUrl) })
    }

    fun getTile(keyUrl: String): Tile? {
        return this.tiles.firstOrNull { it.keyUrl == keyUrl }
    }

    fun tileCount(): Int {
        return this.tiles.size
    }
}