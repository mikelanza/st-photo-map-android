package com.streetography.stphotomap.scenes.stphotomap

import com.google.android.gms.maps.model.LatLng
import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.geojson.BoundingBox
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.scenes.stphotomap.markers.photo.PhotoMarker

class STPhotoMapModels {
    class VisibleTiles {
        class Request(val tiles: ArrayList<TileCoordinate>) {
        }
    }

    class UpdateBoundingBox {
        class Request(val boundingBox: BoundingBox?) {
        }
    }

    class EntityZoomLevel {
        class Response(val entityLevel: EntityLevel) {
        }

        class ViewModel(val titleId: Int, val imageResourceId: Int) {
        }
    }

    class Marker(val id: String,
                 val imageUrl: String?,
                 val latitude: Double,
                 val longitude: Double) {

        fun toPhotoMarker(): PhotoMarker {
            return PhotoMarker(
                id,
                LatLng(latitude, longitude),
                imageUrl,
                null
            )
        }
    }

    class LocationMarkers {
        class Response(val markers: ArrayList<Marker>) {
        }

        class ViewModel(val markers: ArrayList<PhotoMarker>) {
        }
    }
}