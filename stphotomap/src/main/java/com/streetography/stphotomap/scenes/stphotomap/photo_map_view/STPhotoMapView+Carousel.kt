package com.streetography.stphotomap.scenes.stphotomap.photo_map_view

import android.graphics.*
import android.os.Handler
import android.os.Looper
import com.google.android.gms.maps.model.*
import com.streetography.stphotomap.R
import com.streetography.stphotomap.models.geojson.GeoJSON
import com.streetography.stphotomap.models.geojson.geometry.GeoJSONLineString
import com.streetography.stphotomap.models.geojson.geometry.GeoJSONMultiLineString
import com.streetography.stphotomap.models.geojson.geometry.GeoJSONMultiPolygon
import com.streetography.stphotomap.models.geojson.geometry.GeoJSONPolygon
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONGeometry
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import org.json.JSONObject
import kotlin.concurrent.fixedRateTimer

class CarouselOverlay {
    var polygon: Polygon? = null
    var polygonOptions: PolygonOptions? = null

    var polyline: Polyline? = null
    var polylineOptions: PolylineOptions? = null

    var groundOverlay: GroundOverlay? = null
    var groundOverlayOptions: GroundOverlayOptions? = null

    var bitmap: Bitmap? = null

    var didChangeImage: Boolean = false
}

fun STPhotoMapView.startCarouselTimer() {
    fixedRateTimer("carouselTimer", false, 3 * 1000, 3 * 1000) {
        Handler(Looper.getMainLooper()).post {
            updateCarouselOverlays()
        }
    }
}

fun STPhotoMapView.updateCarouselOverlays() {
    this.carouselOverlays.forEach { this.updateCarouselOverlay(it) }
}

fun STPhotoMapView.updateCarouselOverlay(overlay: CarouselOverlay) {
    val id = if (overlay.didChangeImage) R.drawable.cat else R.drawable.watch
    val bitmap = BitmapFactory.decodeResource(this.resources, id)
    overlay.bitmap = bitmap

    overlay.polygon?.let {
        val overlayBitmap = overlayBitmap(Point(bitmap.width, bitmap.height), boundsFor(it.points), it.points, bitmap)
        overlay.groundOverlay?.setImage(BitmapDescriptorFactory.fromBitmap(overlayBitmap))
    }

    overlay.didChangeImage = !overlay.didChangeImage
}

fun STPhotoMapView.drawRomaniaGroundOverlay() {
    val geoJSONObject = this.getRomaniaGeoJSONObject()
    this.carouselOverlays = this.carouselOverlaysFor(geoJSONObject)
    this.carouselOverlays.forEach { this.updateCarouselOverlayOptions(it) }
}

fun STPhotoMapView.updateCarouselOverlayOptions(carouselOverlay: CarouselOverlay) {
    carouselOverlay.polygonOptions?.let { polygonOptions ->
        this.mapView?.addPolygon(polygonOptions)?.let { polygon ->
            carouselOverlay.bitmap?.let { bitmap ->
                groundOverlayOptions(polygon, bitmap)?.let { groundOverlayOptions ->
                    carouselOverlay.polygon = polygon
                    carouselOverlay.groundOverlayOptions = groundOverlayOptions
                    carouselOverlay.groundOverlay = this.mapView?.addGroundOverlay(groundOverlayOptions)
                }
            }
        }
    }
}

fun STPhotoMapView.carouselOverlaysFor(geoJSONObject: GeoJSONObject): ArrayList<CarouselOverlay> {
    if (geoJSONObject.geoJSONGeometry.isEmpty()) return ArrayList()
    return ArrayList(geoJSONObject.geoJSONGeometry.flatMap { this.carouselOverlaysFor(it) })
}

fun STPhotoMapView.carouselOverlaysFor(geoJSONGeometry: GeoJSONGeometry): ArrayList<CarouselOverlay> {
    when (geoJSONGeometry) {
        is GeoJSONLineString -> return arrayListOf(carouselOverlayFor(geoJSONGeometry))
        is GeoJSONMultiLineString -> return ArrayList(geoJSONGeometry.lineStrings.flatMap { this.carouselOverlaysFor(it) })
        is GeoJSONPolygon -> return arrayListOf(carouselOverlayFor(geoJSONGeometry))
        is GeoJSONMultiPolygon -> return ArrayList(geoJSONGeometry.polygons.flatMap { this.carouselOverlaysFor(it) })
        else -> return ArrayList()
    }
}

fun STPhotoMapView.carouselOverlayFor(geoJSONPolygon: GeoJSONPolygon): CarouselOverlay {
    val carouselOverlay = CarouselOverlay()
    carouselOverlay.polygonOptions = polygonOptionsFor(geoJSONPolygon)
    carouselOverlay.bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.watch)
    return carouselOverlay
}

fun STPhotoMapView.carouselOverlayFor(geoJSONLineString: GeoJSONLineString): CarouselOverlay {
    val carouselOverlay = CarouselOverlay()
    carouselOverlay.polylineOptions = polylineOptionsFor(geoJSONLineString)
    carouselOverlay.bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.watch)
    return carouselOverlay
}

fun polygonOptionsFor(geoJSONPolygon: GeoJSONPolygon): PolygonOptions {
    val polygonOptions = PolygonOptions()
        .strokeColor(Color.TRANSPARENT)
        .visible(false)

    val linearRingsCoordinates = ArrayList(
        geoJSONPolygon.linearRings.map { it0 ->
            it0.points.map { it1 ->
                it1.locationCoordinate
            }
        }
    )
    polygonOptions.addAll(linearRingsCoordinates.first())

    val interiorPolygons = linearRingsCoordinates.drop(0)
    interiorPolygons.forEach { polygonOptions.addHole(it) }

    return polygonOptions
}

fun polylineOptionsFor(geoJSONLineString: GeoJSONLineString): PolylineOptions {
    val polylineOptions = PolylineOptions()
        .color(Color.TRANSPARENT)
        .visible(false)
    polylineOptions.addAll(geoJSONLineString.points.map { it.locationCoordinate })
    return polylineOptions
}

fun STPhotoMapView.getRomaniaGeoJSONObject(): GeoJSONObject {
    val inputStream = this.context?.resources?.openRawResource(R.raw.carousel_geojson)
    val inputString = inputStream?.bufferedReader().use { it?.readText() }
    inputStream?.close()
    return GeoJSON().parse(JSONObject(inputString))
}

fun STPhotoMapView.groundOverlayOptions(
    polygon: Polygon,
    bitmap: Bitmap
): GroundOverlayOptions? {
    val bounds = boundsFor(polygon.points)
    val size = Point(bitmap.width, bitmap.height)
    val overlayBitmap = overlayBitmap(size, bounds, polygon.points, bitmap)
    return GroundOverlayOptions()
        .image(BitmapDescriptorFactory.fromBitmap(overlayBitmap))
        .positionFromBounds(bounds)
        .zIndex(2F)
}

/*
fun STPhotoMapView.bitmapSizeFor(polygon: List<LatLng>, bitmap: Bitmap): Point {
    val projection = this.mapView?.projection ?: run { return Point() }

    val bounds = boundsFor(polygon)
    val boundsWidth = bounds.northeast.longitude - bounds.southwest.longitude
    val boundsHeight = bounds.northeast.latitude - bounds.southwest.latitude
    val northEast = projection.toScreenLocation(bounds.northeast)
    val southWest = projection.toScreenLocation(bounds.southwest)

    val screenBoundHeight = northEast.y - southWest.y
    val screenBoundWidth = northEast.x - southWest.x
    val scale = abs(screenBoundHeight.toDouble() / screenBoundWidth.toDouble())

    val overlayBitmapWidth: Int
    val overlayBitmapHeight: Int
    val maximumOverlaySize = 1200
    if (abs(boundsWidth) > abs(boundsHeight)) {
        overlayBitmapWidth = maximumOverlaySize
        overlayBitmapHeight = (overlayBitmapWidth * scale).toInt()
    } else {
        overlayBitmapHeight = maximumOverlaySize
        overlayBitmapWidth = (overlayBitmapHeight * scale).toInt()
    }

    if (bitmap.width > overlayBitmapWidth && bitmap.height > overlayBitmapHeight) return Point(bitmap.width, bitmap.height)
    return Point(overlayBitmapWidth, overlayBitmapHeight)
}
*/

fun boundsFor(polygon: List<LatLng>): LatLngBounds {
    val builder = LatLngBounds.Builder()
    polygon.forEach { builder.include(it) }
    return builder.build()
}

fun overlayBitmap(
    size: Point,
    bounds: LatLngBounds,
    polygon: List<LatLng>,
    bitmap: Bitmap
): Bitmap {
    val canvasBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888)
    val path = pathFor(polygon, size.x, size.y, bounds)
    val rect = Rect(0, 0, size.x, size.y)
    val canvas = Canvas(canvasBitmap)

    val paint = Paint()
    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL
    canvas.drawPath(path, paint)

    paint.isDither = true
    paint.isFilterBitmap = true
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, rect, rect, paint)

    paint.xfermode = null
    paint.style = Paint.Style.STROKE
    paint.color = Color.argb(255, 65, 171, 255)
    paint.strokeWidth = 12F
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeMiter = 2F
    paint.strokeJoin = Paint.Join.ROUND
    canvas.drawPath(path, paint)

    return canvasBitmap
}

fun pathFor(
    polygon: List<LatLng>,
    width: Int,
    height: Int,
    bounds: LatLngBounds
): Path {
    val points = ArrayList<Point>(polygon.size)
    val boundsHeight: Double = bounds.northeast.latitude - bounds.southwest.latitude
    val boundsWidth: Double = bounds.northeast.longitude - bounds.southwest.longitude

    for (i in 0 until polygon.size) {
        val latLng = polygon[i]
        val point = Point()
        point.x = (width * (latLng.longitude - bounds.southwest.longitude) / boundsWidth).toInt()
        point.y = height - (height * (latLng.latitude - bounds.southwest.latitude) / boundsHeight).toInt()
        points.add(point)
    }

    val path = Path()
    path.moveTo(points.first().x.toFloat(), points.first().y.toFloat())
    for (i in 1 until polygon.size) {
        path.lineTo(points[i].x.toFloat(), points[i].y.toFloat())
    }
    path.close()

    return path
}