package com.streetography.stphotomap.scenes.stphotomap

import android.view.View
import androidx.test.core.app.ApplicationProvider
import com.streetography.stphotomap.R
import com.streetography.stphotomap.scenes.stphotomap.seeds.STPhotoMapSeeds
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STEntityLevelViewSpy
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STPhotoMapBusinessLogicSpy
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class STPhotoMapViewTests: TestCase() {
    lateinit var sut: STPhotoMapView
    lateinit var interactorSpy: STPhotoMapBusinessLogicSpy
    lateinit var entityLevelViewSpy: STEntityLevelViewSpy

    private fun setupSubjectUnderTest() {
        this.sut = STPhotoMapView(ApplicationProvider.getApplicationContext())

        this.interactorSpy = STPhotoMapBusinessLogicSpy()
        this.sut.interactor = this.interactorSpy
        this.entityLevelViewSpy = STEntityLevelViewSpy(ApplicationProvider.getApplicationContext())
        this.sut.entityLevelView = this.entityLevelViewSpy
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

    //region Business logic tests
    @Test
    fun testShouldUpdateVisibleTilesWhenCameraIdle() {
        this.sut.onCameraIdle()
        assertTrue("The interactor should update visible tiles when camera is on idle.", this.interactorSpy.shouldUpdateVisibleTilesCalled)
    }

    @Test
    fun testShouldDetermineEntityLevelWhenCameraIdle() {
        this.sut.onCameraIdle()
        assertTrue("The interactor should determine entity level when camera is on idle.", this.interactorSpy.shouldDetermineEntityLevelCalled)
    }

    @Test
    fun testShouldCacheGeojsonObjectsWhenCameraIdle() {
        this.sut.onCameraIdle()
        assertTrue(this.interactorSpy.shouldCacheGeojsonObjectsCalled)
    }

    @Test
    fun testShouldUpdateBoundingBoxParameterWhenOnCameraMove() {
        this.sut.onCameraMove()
        assertTrue(this.interactorSpy.shouldUpdateBoundingBoxCalled)
    }

    @Test
    fun testShouldDetermineLocationLevelWhenCameraIdle() {
        this.sut.onCameraIdle()
        assertTrue("The interactor should determine location level when camera is on idle.", this.interactorSpy.shouldDetermineLocationLevelCalled)
    }
    //endregion

    //region Display logic tests
    @Test
    fun testDisplayLoadingState() {
        this.sut.progressBar?.visibility = View.GONE
        this.sut.progressBar?.progress = 0

        this.sut.displayLoadingState()

        this.sut.post {
            assertEquals(View.VISIBLE, this.sut.progressBar?.visibility)
            assertEquals(100, this.sut.progressBar?.progress)
        }
    }

    @Test
    fun testDisplayNotLoadingState() {
        this.sut.progressBar?.visibility = View.VISIBLE
        this.sut.progressBar?.progress = 100

        this.sut.displayNotLoadingState()

        this.sut.post {
            assertEquals(View.GONE, this.sut.progressBar?.visibility)
            assertEquals(0, this.sut.progressBar?.progress)
        }
    }

    @Test
    fun testDisplayEntityLevel() {
        val viewModel = STPhotoMapModels.EntityZoomLevel.ViewModel(R.string.st_photo_map_block_level_title, R.drawable.st_entity_level_location)
        this.sut.displayEntityLevel(viewModel)

        assertTrue(this.entityLevelViewSpy.setTitleCalled)
        assertTrue(this.entityLevelViewSpy.setImageCalled)
        assertTrue(this.entityLevelViewSpy.showCalled)
    }
    //endregion

    //region Location level
    @Test
    fun testDisplayLocationMarkers() {
        val photoMarkers = STPhotoMapSeeds().photoMarkers()

        Timer().schedule(object : TimerTask() {
            override fun run() {
                sut.moveMapCameraTo(photoMarkers.get(0).position)

                val viewModel = STPhotoMapModels.LocationMarkers.ViewModel(photoMarkers)
                sut.displayLocationMarkers(viewModel)

                assertEquals(sut.markerHandler?.markers?.size, photoMarkers.size)
                assertEquals(sut.markerHandler?.clusterManager?.markerCollection?.markers?.size, photoMarkers.size)
            }
        }, 2500)
    }
    //endregion
}