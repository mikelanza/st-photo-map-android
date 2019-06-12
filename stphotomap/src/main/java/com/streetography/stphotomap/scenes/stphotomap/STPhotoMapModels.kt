package com.streetography.stphotomap.scenes.stphotomap

import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate

class STPhotoMapModels {
    class VisibleTiles {
        class Request {
            val tiles: ArrayList<TileCoordinate>

            constructor(tiles: ArrayList<TileCoordinate>) {
                this.tiles = tiles
            }
        }
    }
}