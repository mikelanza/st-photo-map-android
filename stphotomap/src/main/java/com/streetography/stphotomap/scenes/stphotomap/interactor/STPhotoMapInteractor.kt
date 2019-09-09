package com.streetography.stphotomap.scenes.stphotomap.interactor

import android.content.Context
import android.content.pm.PackageManager
import com.streetography.stphotomap.models.coordinate.Coordinate
import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.geojson.interfaces.GeoJSONObject
import com.streetography.stphotomap.models.location.STLocation
import com.streetography.stphotomap.models.parameters.KeyValue
import com.streetography.stphotomap.models.parameters.Parameters
import com.streetography.stphotomap.models.photo.STPhoto
import com.streetography.stphotomap.models.tile_coordinate.TileCoordinate
import com.streetography.stphotomap.operations.base.errors.OperationError
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapModels
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapPresentationLogic
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorker
import com.streetography.stphotomap.scenes.stphotomap.STPhotoMapWorkerDelegate
import com.streetography.stphotomap.scenes.stphotomap.builders.STPhotoMapUriBuilder
import com.streetography.stphotomap.scenes.stphotomap.cache.STPhotoMapGeojsonCache
import com.streetography.stphotomap.scenes.stphotomap.cache.STPhotoMapGeojsonCacheHandler
import com.streetography.stphotomap.scenes.stphotomap.entity_level.STPhotoMapEntityLevelHandler
import com.streetography.stphotomap.scenes.stphotomap.entity_level.STPhotoMapEntityLevelHandlerDelegate
import com.streetography.stphotomap.scenes.stphotomap.location_level.STPhotoMapLocationLevelHandler
import com.streetography.stphotomap.scenes.stphotomap.parameters.STPhotoMapParametersHandler
import com.streetography.stphotomap.scenes.stphotomap.user_location.STPhotoMapUserLocationHandler
import com.streetography.stphotomap.scenes.stphotomap.user_location.STPhotoMapUserLocationHandlerDelegate

interface STPhotoMapBusinessLogic {
    fun shouldUpdateVisibleTiles(request: STPhotoMapModels.VisibleTiles.Request)
    fun shouldUpdateBoundingBox(request: STPhotoMapModels.UpdateBoundingBox.Request)
    fun shouldDetermineEntityLevel()
    fun shouldCacheGeojsonObjects()
    fun shouldDetermineLocationLevel()
    fun shouldNavigateToPhotoDetails(request: STPhotoMapModels.PhotoDetailsNavigation.Request)
    fun shouldNavigateToPhotoCollection(request: STPhotoMapModels.PhotoCollectionNavigation.Request)
    fun shouldGetPhotoDetailsForPhotoMarker(request: STPhotoMapModels.PhotoDetails.Request)
    fun shouldZoomToCoordinate(request: STPhotoMapModels.CoordinateZoom.Request)
    fun shouldAskForLocationPermissions()
    fun shouldRequestUserLocation()

    fun shouldOpenDataSourcesLink()
    fun shouldOpenSettingsApplication()
}

class STPhotoMapInteractor(context: Context): STPhotoMapBusinessLogic,
    STPhotoMapWorkerDelegate, STPhotoMapEntityLevelHandlerDelegate, STPhotoMapUserLocationHandlerDelegate {
    var worker: STPhotoMapWorker? = STPhotoMapWorker(this)
    var presenter: STPhotoMapPresentationLogic? = null

    var visibleTiles: ArrayList<TileCoordinate>
    var cacheHandler: STPhotoMapGeojsonCacheHandler
    var entityLevelHandler: STPhotoMapEntityLevelHandler
    var locationLevelHandler: STPhotoMapLocationLevelHandler
    var userLocationHandler: STPhotoMapUserLocationHandler?

    init {
        this.visibleTiles = ArrayList()
        this.cacheHandler = STPhotoMapGeojsonCacheHandler()
        this.entityLevelHandler = STPhotoMapEntityLevelHandler(this)
        this.locationLevelHandler = STPhotoMapLocationLevelHandler()
        this.userLocationHandler = STPhotoMapUserLocationHandler(context, this)
    }

    //region Business logic
    override fun shouldUpdateVisibleTiles(request: STPhotoMapModels.VisibleTiles.Request) {
        this.visibleTiles = request.tiles
    }

    override fun shouldUpdateBoundingBox(request: STPhotoMapModels.UpdateBoundingBox.Request) {
        request.boundingBox?.description?.let {
            STPhotoMapParametersHandler.instance.update(KeyValue(Parameters.Keys.bbox, it))
        }
    }

    override fun shouldDetermineEntityLevel() {
        this.cancelAllGeojsonEntityLevelOperations()

        val cachedTiles = this.getVisibleCachedTiles()
        if (cachedTiles.size > 0) {
            this.calculateEntityLevelFor(cachedTiles)
        } else {
            this.entityLevelGeojsonObjectsFor(this.prepareTilesForEntityLevel())
        }

        this.handleLoadingStateForEntityLevel()
    }

    override fun shouldDetermineLocationLevel() {
        if (!this.isLocationLevel()) { return }

        val cachedTiles = this.getVisibleCachedTiles()
        this.presentPhotoMarkersForCached(cachedTiles)
        this.locationLevelGeojsonObjectsFor(this.prepareTilesForLocationLevel())
    }

    override fun shouldNavigateToPhotoDetails(request: STPhotoMapModels.PhotoDetailsNavigation.Request) {
        this.presenter?.presentNavigateToPhotoDetails(STPhotoMapModels.PhotoDetailsNavigation.Response(request.photoId))
    }

    override fun shouldNavigateToPhotoCollection(request: STPhotoMapModels.PhotoCollectionNavigation.Request) {
        if (this.entityLevelHandler.entityLevel == EntityLevel.unknown) { return }

        val userId = STPhotoMapParametersHandler.instance.parameters.filter {
            it.first == Parameters.Keys.userId
        }.firstOrNull()?.second

        val collectionId = STPhotoMapParametersHandler.instance.parameters.filter {
            it.first == Parameters.Keys.collectionId
        }.firstOrNull()?.second

        request.latLng?.let {
            this.presenter?.presentNavigateToPhotoCollection(STPhotoMapModels.PhotoCollectionNavigation.Response(
                STLocation(it.latitude, it.longitude), this.entityLevelHandler.entityLevel, userId, collectionId));
        }
    }

    override fun shouldGetPhotoDetailsForPhotoMarker(request: STPhotoMapModels.PhotoDetails.Request) {
        if (this.isLocationLevel()) {
            this.presenter?.presentLoadingState()
            this.worker?.getPhotoDetailsForPhotoMarker(request.photoId)
        }
    }

    override fun shouldZoomToCoordinate(request: STPhotoMapModels.CoordinateZoom.Request) {
        this.presenter?.presentZoomToCoordinate(STPhotoMapModels.CoordinateZoom.Response(request.coordinate))
    }
    //endregion

    private fun getVisibleCachedTiles(): ArrayList<STPhotoMapGeojsonCache.Tile> {
        return ArrayList(this.visibleTiles.mapNotNull { tile ->
            val uri = STPhotoMapUriBuilder().geojsonTileUri(tile)
            this.cacheHandler.cache.getTile(uri.first)
        })
    }

    //region Entity level
    override fun photoMapEntityLevelHandler(newEntityLevel: EntityLevel) {
        this.worker?.cancelAllGeojsonEntityLevelOperations()
        this.worker?.cancelAllGeojsonLocationLevelOperations()

        this.presenter?.presentRemoveLocationMarkers()
        this.presenter?.presentEntityLevel(STPhotoMapModels.EntityZoomLevel.Response(newEntityLevel))
    }

    override fun photoMapEntityLevelHandlerNewLocationLevel(level: EntityLevel) {
        this.worker?.cancelAllGeojsonEntityLevelOperations()

        this.presenter?.presentEntityLevel(STPhotoMapModels.EntityZoomLevel.Response(level))
        this.shouldDetermineLocationLevel()
    }

    override fun successDidGetGeojsonTileForEntityLevel(
        tileCoordinate: TileCoordinate,
        keyUrl: String,
        downloadUrl: String,
        geojsonObject: GeoJSONObject
    ) {
        this.entityLevelHandler.removeActiveDownload(keyUrl)
        this.didGetGeojsonTileForEntityLevel(tileCoordinate, geojsonObject)
    }

    override fun failureDidGetGeojsonTileForEntityLevel(
        tileCoordinate: TileCoordinate,
        keyUrl: String,
        downloadUrl: String,
        error: OperationError
    ) {
        this.entityLevelHandler.removeActiveDownload(keyUrl)
        this.handleLoadingStateForEntityLevel()
    }
    //endregion

    //region Geojson caching
    override fun shouldCacheGeojsonObjects() {
        val tiles = this.prepareTilesForCaching()
        this.cacheGeojsonObjects(tiles)
    }

    override fun successDidGetGeojsonTileForCaching(
        tileCoordinate: TileCoordinate,
        keyUrl: String,
        downloadUrl: String,
        geojsonObject: GeoJSONObject
    ) {
        this.cacheHandler.cache.addTile(STPhotoMapGeojsonCache.Tile(keyUrl, geojsonObject))
        this.cacheHandler.removeActiveDownload(keyUrl)
    }

    override fun failureDidGetGeojsonTileForCaching(
        tileCoordinate: TileCoordinate,
        keyUrl: String,
        downloadUrl: String,
        error: OperationError
    ) {
        this.cacheHandler.removeActiveDownload(keyUrl)
    }
    //endregion

    //region Location level
    override fun successDidGetGeojsonTileForLocationLevel(
        tileCoordinate: TileCoordinate,
        keyUrl: String,
        downloadUrl: String,
        geojsonObject: GeoJSONObject
    ) {
        this.locationLevelHandler.removeActiveDownload(keyUrl)
        this.didGetGeojsonTileForLocationLevel(geojsonObject)
    }

    override fun failureDidGetGeojsonTileForLocationLevel(
        tileCoordinate: TileCoordinate,
        keyUrl: String,
        downloadUrl: String,
        error: OperationError
    ) {
        this.locationLevelHandler.removeActiveDownload(keyUrl)
    }
    //endregion

    //region Photo details
    override fun successDidGetPhotoDetailsForPhotoMarker(photoId: String, photo: STPhoto) {
        this.presenter?.presentNotLoadingState()
        this.shouldPresentLocationOverlay(photo)
    }

    override fun failureDidGetPhotoDetailsForPhotoMarker(photoId: String, error: OperationError) {
        this.presenter?.presentNotLoadingState()
    }

    private fun shouldPresentLocationOverlay(photo: STPhoto) {
        if (this.isLocationLevel()) {
            this.presenter?.presentLocationOverlay(STPhotoMapModels.LocationOverlay.Response(photo))
        }
    }
    //endregion

    //region Current user location
    override fun shouldAskForLocationPermissions() {
        when (this.userLocationHandler?.locationPermissionStatus) {
            PackageManager.PERMISSION_GRANTED -> return this.handleGrantedLocationPermissions()
            PackageManager.PERMISSION_DENIED -> return this.handleNotDeterminedLocationPermissions()
            else -> return this.handleNotDeterminedLocationPermissions()
        }
    }

    private fun handleGrantedLocationPermissions() {
        this.userLocationHandler?.requestUserLocation()
    }

    private fun handleNotDeterminedLocationPermissions() {
        this.presenter?.presentRequestLocationPermissions()
    }

    override fun userLocationHandler(
        handler: STPhotoMapUserLocationHandler?,
        centerToCoordinate: Coordinate
    ) {
        this.presenter?.presentCenterToCoordinate(STPhotoMapModels.CoordinateCenter.Response(centerToCoordinate, EntityLevel.block))
    }

    override fun shouldRequestUserLocation() {
        this.userLocationHandler?.requestUserLocation()
    }
    //endregion

    //region Open applications
    override fun shouldOpenDataSourcesLink() {
        this.presenter?.presentOpenDataSourcesLink()
    }

    override fun shouldOpenSettingsApplication() {
        this.presenter?.presentOpenSettingsApplication()
    }
    //endregion
}