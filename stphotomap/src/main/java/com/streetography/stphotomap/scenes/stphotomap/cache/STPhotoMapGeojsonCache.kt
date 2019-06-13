package com.streetography.stphotomap.scenes.stphotomap.cache

import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject

class STPhotoMapGeojsonCache() {
    class Tile(val keyUrl: String, var geojsonObject: GeoJSONObject) {
    }

    var tiles: ArrayList<Tile>

    init {
        this.tiles = ArrayList()
    }

    fun addTile(tile: Tile) {
        this.tiles.add(tile)
    }

    fun removeAllTiles() {
        this.tiles.clear()
    }

    fun getTiles(urls: ArrayList<String>): ArrayList<Tile> {
        val filteredTiles =  this.tiles.filter { tile ->
            urls.contains(tile.keyUrl)
        }
        return ArrayList(filteredTiles)
    }

    fun getTile(keyUrl: String): Tile {
        return this.tiles.first { tile ->
            tile.keyUrl == keyUrl
        }
    }

    fun tileCount(): Int {
        return this.tiles.size
    }
}