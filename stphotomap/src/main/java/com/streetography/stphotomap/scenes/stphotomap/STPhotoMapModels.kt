package com.streetography.stphotomap.scenes.stphotomap

import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate

class STPhotoMapModels {
    class VisibleTiles {
        class Request(val tiles: ArrayList<TileCoordinate>) {
        }
    }

    class EntityZoomLevel {
        class Response(val entityLevel: EntityLevel) {
        }

        class ViewModel(val titleId: Int, val imageResourceId: Int) {
        }
    }
}