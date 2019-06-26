package com.streetography.stphotomap.scenes.stphotomap

import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.scenes.stphotomap.builders.STPhotoMapUriBuilder
import com.streetography.stphotomap.scenes.stphotomap.cache.STPhotoMapGeojsonCache
import com.streetography.stphotomap.scenes.stphotomap.interactor.STPhotoMapInteractor
import com.streetography.stphotomap.scenes.stphotomap.seeds.STPhotoMapSeeds
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STPhotoMapPresentationLogicSpy
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STPhotoMapWorkerSpy
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLooper

@RunWith(RobolectricTestRunner::class)
class STPhotoMapInteractorTests: TestCase() {
    lateinit var sut: STPhotoMapInteractor
    lateinit var workerSpy: STPhotoMapWorkerSpy
    lateinit var presenterSpy: STPhotoMapPresentationLogicSpy

    val workerDelay: Long = 100

    private fun setupSubjectUnderTest() {
        this.sut = STPhotoMapInteractor()

        this.presenterSpy = STPhotoMapPresentationLogicSpy()
        this.workerSpy = STPhotoMapWorkerSpy(this.sut)

        this.sut.presenter = this.presenterSpy
        this.sut.worker = this.workerSpy
    }

    @Before
    public override fun setUp() {
        super.setUp()
        this.setupSubjectUnderTest()
    }

    @After
    public override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun testShouldCacheGeojsonObjectsWhenCacheIsEmptyForSuccessCase() {
        this.workerSpy.geojsonObject = STPhotoMapSeeds().geojsonObject()

        val tileCoordinates = STPhotoMapSeeds().tileCoordinates
        this.sut.cacheHandler.cache.removeAllTiles()
        this.sut.visibleTiles = tileCoordinates

        this.sut.shouldCacheGeojsonObjects()

        assertTrue(this.workerSpy.getGeojsonTileForCachingCalled)

        assertEquals(0, this.sut.cacheHandler.activeDownloadCount())
        assertEquals(tileCoordinates.size, this.sut.cacheHandler.cache.tileCount())
    }

    @Test
    fun testShouldCacheGeojsonObjectsWhenCacheIsEmptyForFailureCase() {
        this.workerSpy.shouldFailGetGeojsonTileForCaching = true

        val tileCoordinates = STPhotoMapSeeds().tileCoordinates
        this.sut.cacheHandler.cache.removeAllTiles()
        this.sut.cacheHandler.removeAllActiveDownloads()
        this.sut.visibleTiles = tileCoordinates

        this.sut.shouldCacheGeojsonObjects()

        assertTrue(this.workerSpy.getGeojsonTileForCachingCalled)

        assertEquals(0, this.sut.cacheHandler.activeDownloadCount())
        assertEquals(0, this.sut.cacheHandler.cache.tileCount())
    }

    @Test
    fun testShouldCacheGeojsonObjectsWhenCacheIsNotEmptyForSuccessCase() {
        val tileCoordinate = STPhotoMapSeeds().tileCoordinate
        val keyUrl = STPhotoMapUriBuilder().geojsonTileUri(tileCoordinate).first
        val geojsonObject = STPhotoMapSeeds().geojsonObject()

        this.sut.cacheHandler.removeAllActiveDownloads()
        this.sut.cacheHandler.cache.addTile(STPhotoMapGeojsonCache.Tile(keyUrl, geojsonObject))
        this.sut.visibleTiles = arrayListOf(tileCoordinate)

        this.sut.shouldCacheGeojsonObjects()

        assertFalse(this.workerSpy.getGeojsonTileForCachingCalled)

        assertEquals(1, this.sut.cacheHandler.cache.tileCount())
        assertEquals(0, this.sut.cacheHandler.activeDownloadCount())
    }

    @Test
    fun testShouldCacheGeojsonObjectsWhenCacheIsNotEmptyForFailureCase() {
        this.workerSpy.shouldFailGetGeojsonTileForCaching = true

        val tileCoordinate = STPhotoMapSeeds().tileCoordinate
        val keyUrl = STPhotoMapUriBuilder().geojsonTileUri(tileCoordinate).first
        val geojsonObject = STPhotoMapSeeds().geojsonObject()

        this.sut.cacheHandler.removeAllActiveDownloads()
        this.sut.cacheHandler.cache.addTile(STPhotoMapGeojsonCache.Tile(keyUrl, geojsonObject))
        this.sut.visibleTiles = arrayListOf(tileCoordinate)

        this.sut.shouldCacheGeojsonObjects()

        assertFalse(this.workerSpy.getGeojsonTileForCachingCalled)

        assertEquals(1, this.sut.cacheHandler.cache.tileCount())
        assertEquals(0, this.sut.cacheHandler.activeDownloadCount())
    }

    @Test
    fun testShouldCacheGeojsonObjectsWhenCacheIsEmptyAndThereAreActiveDownloadsForSuccessCase() {
        val tileCoordinate = STPhotoMapSeeds().tileCoordinate
        val keyUrl = STPhotoMapUriBuilder().geojsonTileUri(tileCoordinate).first

        this.sut.cacheHandler.cache.removeAllTiles()
        this.sut.cacheHandler.addActiveDownload(keyUrl)
        this.sut.visibleTiles = arrayListOf(tileCoordinate)

        this.sut.shouldCacheGeojsonObjects()

        assertFalse(this.workerSpy.getGeojsonTileForCachingCalled)

        assertEquals(0, this.sut.cacheHandler.cache.tileCount())
        assertEquals(1, this.sut.cacheHandler.activeDownloadCount())
    }

    @Test
    fun testShouldCacheGeojsonObjectsWhenCacheIsEmptyAndThereAreActiveDownloadsForFailureCase() {
        this.workerSpy.shouldFailGetGeojsonTileForCaching = true

        val tileCoordinate = STPhotoMapSeeds().tileCoordinate
        val keyUrl = STPhotoMapUriBuilder().geojsonTileUri(tileCoordinate).first

        this.sut.cacheHandler.cache.removeAllTiles()
        this.sut.cacheHandler.addActiveDownload(keyUrl)
        this.sut.visibleTiles = arrayListOf(tileCoordinate)

        this.sut.shouldCacheGeojsonObjects()

        assertFalse(this.workerSpy.getGeojsonTileForCachingCalled)

        assertEquals(0, this.sut.cacheHandler.cache.tileCount())
        assertEquals(1, this.sut.cacheHandler.activeDownloadCount())
    }

    @Test
    fun testShouldCacheGeojsonObjectsWhenCacheIsNotEmptyAndThereAreActiveDownloadsForSuccessCase() {
        val tileCoordinates = STPhotoMapSeeds().tileCoordinates
        val keyUrl = STPhotoMapUriBuilder().geojsonTileUri(tileCoordinates.first()).first
        val activeDownloadUrl = STPhotoMapUriBuilder().geojsonTileUri(tileCoordinates.last()).first
        val geojsonObject = STPhotoMapSeeds().geojsonObject()

        this.workerSpy.geojsonObject = geojsonObject

        this.sut.cacheHandler.addActiveDownload(activeDownloadUrl)
        this.sut.cacheHandler.cache.addTile(STPhotoMapGeojsonCache.Tile(keyUrl, geojsonObject))
        this.sut.visibleTiles = tileCoordinates

        this.sut.shouldCacheGeojsonObjects()

        assertTrue(this.workerSpy.getGeojsonTileForCachingCalled)

        assertEquals(2, this.sut.cacheHandler.cache.tileCount())
        assertEquals(1, this.sut.cacheHandler.activeDownloadCount())
    }

    @Test
    fun testShouldCacheGeojsonObjectsWhenCacheIsNotEmptyAndThereAreActiveDownloadsForFailureCase() {
        this.workerSpy.shouldFailGetGeojsonTileForCaching = true

        val tileCoordinates = STPhotoMapSeeds().tileCoordinates
        val keyUrl = STPhotoMapUriBuilder().geojsonTileUri(tileCoordinates.first()).first
        val activeDownloadUrl = STPhotoMapUriBuilder().geojsonTileUri(tileCoordinates.last()).first
        val geojsonObject = STPhotoMapSeeds().geojsonObject()

        this.sut.cacheHandler.addActiveDownload(activeDownloadUrl)
        this.sut.cacheHandler.cache.addTile(STPhotoMapGeojsonCache.Tile(keyUrl, geojsonObject))
        this.sut.visibleTiles = tileCoordinates

        this.sut.shouldCacheGeojsonObjects()

        assertTrue(this.workerSpy.getGeojsonTileForCachingCalled)

        assertEquals(1, this.sut.cacheHandler.cache.tileCount())
        assertEquals(1, this.sut.cacheHandler.activeDownloadCount())
    }

    //region Entity level
    @Test
    fun testShouldDetermineEntityLevelWhenCacheIsEmptyAndThereAreNoActiveDownloadsForSuccessCase() {
        this.workerSpy.geojsonObject = STPhotoMapSeeds().geojsonObject()
        this.workerSpy.delay = this.workerDelay

        this.sut.cacheHandler.cache.removeAllTiles()
        this.sut.cacheHandler.removeAllActiveDownloads()
        this.sut.visibleTiles = arrayListOf(STPhotoMapSeeds().tileCoordinate)

        this.sut.shouldDetermineEntityLevel()

        assertTrue(this.workerSpy.cancelAllGeojsonEntityLevelOperationsCalled)
        assertTrue(this.workerSpy.getGeojsonTileForEntityLevelCalled)


        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

        assertTrue(this.presenterSpy.presentLoadingStateCalled)
        assertTrue(this.presenterSpy.presentNotLoadingStateCalled)
        assertTrue(this.presenterSpy.presentEntityLevelCalled)
    }

    @Test
    fun testShouldDetermineEntityLevelWhenCacheIsEmptyAndThereAreNoActiveDownloadsForFailureCase() {
        this.workerSpy.shouldFailGetGeojsonTileForEntityLevel = true

        this.sut.cacheHandler.cache.removeAllTiles()
        this.sut.cacheHandler.removeAllActiveDownloads()
        this.sut.visibleTiles = arrayListOf(STPhotoMapSeeds().tileCoordinate)

        this.sut.shouldDetermineEntityLevel()

        assertTrue(this.workerSpy.cancelAllGeojsonEntityLevelOperationsCalled)
        assertTrue(this.workerSpy.getGeojsonTileForEntityLevelCalled)
        assertFalse(this.presenterSpy.presentLoadingStateCalled)
        assertTrue(this.presenterSpy.presentNotLoadingStateCalled)
        assertFalse(this.presenterSpy.presentEntityLevelCalled)
    }

    @Test
    fun testShouldDetermineEntityLevelWhenCacheIsEmptyAndThereAreActiveDownloads() {
        val tileCoordinate = STPhotoMapSeeds().tileCoordinate
        val keyUri = STPhotoMapUriBuilder().geojsonTileUri(tileCoordinate).first

        this.sut.cacheHandler.cache.removeAllTiles()
        this.sut.cacheHandler.removeAllActiveDownloads()

        this.sut.entityLevelHandler.addActiveDownload(keyUri)
        this.sut.visibleTiles = arrayListOf(tileCoordinate)

        this.sut.shouldDetermineEntityLevel()

        assertTrue(this.workerSpy.cancelAllGeojsonEntityLevelOperationsCalled)
        assertFalse(this.workerSpy.getGeojsonTileForEntityLevelCalled)
        assertTrue(this.presenterSpy.presentLoadingStateCalled)
        assertFalse(this.presenterSpy.presentNotLoadingStateCalled)
        assertFalse(this.presenterSpy.presentEntityLevelCalled)
    }

    @Test
    fun testShouldDetermineEntityLevelWhenCacheIsNotEmptyAndNoActiveDownloads() {
        val tileCoordinate = STPhotoMapSeeds().tileCoordinate
        val keyUri = STPhotoMapUriBuilder().geojsonTileUri(tileCoordinate).first

        val geojsonObject = STPhotoMapSeeds().geojsonObject()

        this.sut.cacheHandler.removeAllActiveDownloads()
        this.sut.cacheHandler.cache.addTile(STPhotoMapGeojsonCache.Tile(keyUri, geojsonObject))
        this.sut.visibleTiles = arrayListOf(tileCoordinate)

        this.sut.shouldDetermineEntityLevel()

        assertTrue(this.workerSpy.cancelAllGeojsonEntityLevelOperationsCalled)
        assertFalse(this.workerSpy.getGeojsonTileForCachingCalled)
        assertTrue(this.presenterSpy.presentNotLoadingStateCalled)
        assertTrue(this.presenterSpy.presentEntityLevelCalled)
    }

    @Test
    fun testShouldDetermineEntityLevelWhenNewEntityLevelIsNotChanged() {
        this.workerSpy.geojsonObject = STPhotoMapSeeds().geojsonObject()

        this.sut.cacheHandler.cache.removeAllTiles()
        this.sut.cacheHandler.removeAllActiveDownloads()

        this.sut.entityLevelHandler.entityLevel = EntityLevel.city

        this.sut.visibleTiles = arrayListOf(STPhotoMapSeeds().tileCoordinate)

        this.sut.shouldDetermineEntityLevel()

        assertTrue(this.workerSpy.cancelAllGeojsonEntityLevelOperationsCalled)
        assertTrue(this.workerSpy.getGeojsonTileForEntityLevelCalled)
        assertFalse(this.presenterSpy.presentLoadingStateCalled)
        assertTrue(this.presenterSpy.presentNotLoadingStateCalled)
        assertFalse(this.presenterSpy.presentEntityLevelCalled)
    }

    @Test
    fun testShouldDetermineEntityLevelWhenNewEntityLevelIsChanged () {

        this.workerSpy.geojsonObject = STPhotoMapSeeds().geojsonObject()

        this.sut.cacheHandler.cache.removeAllTiles()
        this.sut.cacheHandler.removeAllActiveDownloads()

        this.sut.entityLevelHandler.entityLevel = EntityLevel.unknown

        this.sut.visibleTiles = arrayListOf(STPhotoMapSeeds().tileCoordinate)

        this.sut.shouldDetermineEntityLevel()

        assertTrue(this.workerSpy.cancelAllGeojsonEntityLevelOperationsCalled)
        assertTrue(this.workerSpy.getGeojsonTileForEntityLevelCalled)
        assertTrue(this.presenterSpy.presentNotLoadingStateCalled)
        assertTrue(this.presenterSpy.presentEntityLevelCalled)
    }

    @Test
    fun testShouldDetermineEntityLevelWhenNewEntityLevelIsLocation() {
        workerSpy.geojsonObject = STPhotoMapSeeds().locationGeojsonObject()


        this.sut.cacheHandler.cache.removeAllTiles()
        this.sut.cacheHandler.removeAllActiveDownloads()

        this.sut.entityLevelHandler.entityLevel = EntityLevel.unknown
        this.sut.visibleTiles = arrayListOf(STPhotoMapSeeds().tileCoordinate)


        this.sut.cacheHandler.removeAllActiveDownloads()


        this.sut.shouldDetermineEntityLevel()

        assertTrue(this.workerSpy.cancelAllGeojsonEntityLevelOperationsCalled)
        assertTrue(this.workerSpy.getGeojsonTileForEntityLevelCalled)
        assertTrue(this.presenterSpy.presentNotLoadingStateCalled)
        assertTrue(this.presenterSpy.presentEntityLevelCalled)
    }

    @Test
    fun testShouldDetermineEntityLevelWhenDownloadedTileIsNotStillVisible() {
        this.workerSpy.delay = this.workerDelay
        this.workerSpy.geojsonObject = STPhotoMapSeeds().geojsonObject()

        this.sut.visibleTiles = arrayListOf(STPhotoMapSeeds().tileCoordinate)

        this.sut.shouldDetermineEntityLevel()

        assertTrue(this.workerSpy.cancelAllGeojsonEntityLevelOperationsCalled)
        assertTrue(this.workerSpy.getGeojsonTileForEntityLevelCalled)

        this.sut.visibleTiles.clear()

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

        assertFalse(this.presenterSpy.presentEntityLevelCalled)
    }

    @Test
    fun testShouldDetermineEntityLevelWhenDownloadedTileIsStillVisible() {
        this.workerSpy.geojsonObject = STPhotoMapSeeds().geojsonObject()

        this.sut.visibleTiles = arrayListOf(STPhotoMapSeeds().tileCoordinate)

        this.sut.shouldDetermineEntityLevel()

        assertTrue(this.workerSpy.cancelAllGeojsonEntityLevelOperationsCalled)
        assertTrue(this.workerSpy.getGeojsonTileForEntityLevelCalled)
        assertTrue(this.presenterSpy.presentEntityLevelCalled)
    }
    //endregion

    //region Location level

    @Test
    fun testShouldDetermineLocationLevelWhenCacheIsNotEmptyAndEntityLevelIsLocation() {
        val tileCoordinate = STPhotoMapSeeds().tileCoordinate
        val keyUrl = STPhotoMapUriBuilder().geojsonTileUri(tileCoordinate).first
        val geojsonObject = STPhotoMapSeeds().locationGeojsonObject()

        this.workerSpy.geojsonObject = geojsonObject

        this.sut.cacheHandler.removeAllActiveDownloads()
        this.sut.cacheHandler.cache.addTile(STPhotoMapGeojsonCache.Tile(keyUrl, geojsonObject))
        this.sut.visibleTiles = arrayListOf(tileCoordinate)

        this.sut.entityLevelHandler.entityLevel = EntityLevel.location

        this.sut.shouldDetermineLocationLevel()

        assertTrue(this.presenterSpy.presentLocationMarkersCalled)
    }

    //endregion

}