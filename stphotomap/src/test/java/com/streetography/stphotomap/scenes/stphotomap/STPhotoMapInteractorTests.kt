package com.streetography.stphotomap.scenes.stphotomap

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
}