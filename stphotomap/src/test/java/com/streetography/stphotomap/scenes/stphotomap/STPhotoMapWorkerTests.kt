package com.streetography.stphotomap.scenes.stphotomap

import com.streetography.stphotomap.scenes.stphotomap.seeds.STPhotoMapSeeds
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.OperationQueueSpy
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class STPhotoMapWorkerTests: TestCase() {
    lateinit var sut: STPhotoMapWorker

    private fun setupSubjectUnderTest() {
        this.sut = STPhotoMapWorker(null)
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
    fun testGetGeojsonTileForCaching() {
        val operationQueueSpy = OperationQueueSpy()
        this.sut.geojsonTileCachingQueue = operationQueueSpy

        this.sut.getGeojsonTileForCaching(STPhotoMapSeeds().tileCoordinate, STPhotoMapSeeds().imageUrl, STPhotoMapSeeds().imageUrl)
        assertTrue(operationQueueSpy.addOperationCalled)
    }

    @Test
    fun testGetGeojsonTileForEntityLevel() {
        val operationQueueSpy = OperationQueueSpy()
        this.sut.geojsonEntityLevelQueue = operationQueueSpy

        this.sut.getGeojsonEntityLevel(STPhotoMapSeeds().tileCoordinate, STPhotoMapSeeds().imageUrl, STPhotoMapSeeds().imageUrl)
        assertTrue(operationQueueSpy.addOperationCalled)
    }

    @Test
    fun testCancellAllGeojsonTileForEntityLevel() {
        val operationQueueSpy = OperationQueueSpy()
        this.sut.geojsonEntityLevelQueue = operationQueueSpy

        this.sut.getGeojsonEntityLevel(STPhotoMapSeeds().tileCoordinate, STPhotoMapSeeds().imageUrl, STPhotoMapSeeds().imageUrl)
        assertTrue(operationQueueSpy.addOperationCalled)

        this.sut.cancelAllGeojsonEntityLevelOperations()
        assertEquals(0, this.sut.geojsonEntityLevelQueue.operationCount)
    }
}