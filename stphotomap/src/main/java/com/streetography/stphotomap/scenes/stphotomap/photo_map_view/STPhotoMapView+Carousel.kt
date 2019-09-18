package com.streetography.stphotomap.scenes.stphotomap.photo_map_view

import android.graphics.*
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

class CarouselOverlay {
    var polygonOptions: PolygonOptions? = null
    var polylineOptions: PolylineOptions? = null
    var bitmap: Bitmap? = null
}

fun STPhotoMapView.drawRomaniaGroundOverlay() {
    val geoJSONObject = this.getRomaniaGeoJSONObject()
    val carouselOverlays = this.carouselOverlaysFor(geoJSONObject)
    val groundOverlaysOptions = carouselOverlays.mapNotNull { this.groundOverlayOptionsFor(it) }
    groundOverlaysOptions.forEach { this.mapView?.addGroundOverlay(it) }
}

fun STPhotoMapView.groundOverlayOptionsFor(carouselOverlay: CarouselOverlay): GroundOverlayOptions? {
    this.polygonFor(carouselOverlay)?.let { polygon ->
        carouselOverlay.bitmap?.let { bitmap ->
            return groundOverlayOptions(polygon, bitmap)
        }
    }
    return null
}

fun STPhotoMapView.polygonFor(carouselOverlay: CarouselOverlay): Polygon? {
    carouselOverlay.polygonOptions?.let {
        return this.mapView?.addPolygon(it)
    }
    return null
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

fun groundOverlayOptions(
    polygon: Polygon,
    bitmap: Bitmap
): GroundOverlayOptions {
    val bounds = boundsFor(polygon.points)
    val overlayBitmap = overlayBitmap(bounds, polygon.points, bitmap)
    return GroundOverlayOptions()
        .image(BitmapDescriptorFactory.fromBitmap(overlayBitmap))
        .positionFromBounds(bounds)
        .zIndex(2F)
}

fun boundsFor(polygon: List<LatLng>): LatLngBounds {
    val builder = LatLngBounds.Builder()
    polygon.forEach { builder.include(it) }
    return builder.build()
}

fun overlayBitmap(
    bounds: LatLngBounds,
    polygon: List<LatLng>,
    bitmap: Bitmap
): Bitmap {
    val canvasBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val path = pathFor(polygon, bitmap.width, bitmap.height, bounds)
    val rect = Rect(0, 0, bitmap.width, bitmap.height)
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