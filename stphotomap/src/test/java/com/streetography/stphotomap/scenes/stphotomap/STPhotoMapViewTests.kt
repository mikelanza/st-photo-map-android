package com.streetography.stphotomap.scenes.stphotomap

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

}