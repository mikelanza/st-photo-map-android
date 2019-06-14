package com.streetography.stphotomap.scenes.stphotomap

import android.view.View
import androidx.test.core.app.ApplicationProvider
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STPhotoMapBusinessLogicSpy
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class STPhotoMapViewTests: TestCase() {
    lateinit var sut: STPhotoMapView
    lateinit var interactorSpy: STPhotoMapBusinessLogicSpy

    private fun setupSubjectUnderTest() {
        this.sut = STPhotoMapView(ApplicationProvider.getApplicationContext())

        this.interactorSpy = STPhotoMapBusinessLogicSpy()
        this.sut.interactor = this.interactorSpy
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
    //endregion
}