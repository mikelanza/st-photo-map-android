package com.streetography.stphotomap.models.tile_coordinate

import android.util.Log
import kotlin.math.max
import kotlin.math.min

class TileCoordinate(val zoom: Int, val x: Int, val y: Int) {
    fun log() {
        Log.i("TileCoordinate", "********** Tile Coordinate **********")
        Log.i("TileCoordinate", "x: " + this.x)
        Log.i("TileCoordinate", "y: " + this.y)
        Log.i("TileCoordinate", "Zoom: " + this.zoom)
        Log.i("TileCoordinate", "********** Tile Coordinate **********")
    }

    fun maxX(relation: TileCoordinate): Int {
        return max(this.x, relation.x)
    }

    fun minX(relation: TileCoordinate): Int {
        val minX = min(this.x, this.x)
        return if (minX > 0) minX else 0
    }

    fun maxY(relation: TileCoordinate): Int {
        return max(this.y, relation.y)
    }

    fun minY(relation: TileCoordinate): Int {
        val minY = min(this.y, this.y)
        return if (minY > 0) minY else 0
    }
}