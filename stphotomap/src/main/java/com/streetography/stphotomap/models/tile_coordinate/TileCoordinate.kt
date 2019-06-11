package com.streetography.stphotomap.models.tile_coordinate

import android.util.Log

class TileCoordinate(val zoom: Int, val x: Int, val y: Int) {
    fun log() {
        Log.i("TileCoordinate", "********** Tile Coordinate **********")
        Log.i("TileCoordinate", "x: " + this.x)
        Log.i("TileCoordinate", "y: " + this.y)
        Log.i("TileCoordinate", "Zoom: " + this.zoom)
        Log.i("TileCoordinate", "********** Tile Coordinate **********")
    }
}