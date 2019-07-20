package com.streetography.stphotomap.scenes.stphotomap

import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.photo.STPhoto
import com.streetography.stphotomap.scenes.stphotomap.seeds.STPhotoMapSeeds
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STPhotoMapDisplayLogicSpy
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.ref.WeakReference
import java.util.*

@RunWith(RobolectricTestRunner::class)
class STPhotoMapPresenterTests: TestCase() {
    lateinit var sut: STPhotoMapPresenter
    lateinit var displayerSpy: STPhotoMapDisplayLogicSpy

    private fun setupSubjectUnderTest() {
        this.sut = STPhotoMapPresenter()

        this.displayerSpy = STPhotoMapDisplayLogicSpy()
        this.sut.displayer = WeakReference(this.displayerSpy)
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
    fun testPresentLoadingState() {
        this.sut.presentLoadingState()
        assertTrue(this.displayerSpy.displayLoadingStateCalled)
    }

    @Test
    fun testPresentNotLoadingState() {
        this.sut.presentNotLoadingState()
        assertTrue(this.displayerSpy.displayNotLoadingStateCalled)
    }

    @Test
    fun testPresentEntityLevel() {
        val response = STPhotoMapModels.EntityZoomLevel.Response(EntityLevel.block)
        this.sut.presentEntityLevel(response)
        assertTrue(this.displayerSpy.displayEntityLevelCalled)
    }

    @Test
    fun testPresentLocationMarkers() {
        val response = STPhotoMapModels.LocationMarkers.Response(STPhotoMapSeeds().markers())
        this.sut.presentLocationMarkers(response)

        assertTrue(this.displayerSpy.displayLocationMarkersCalled)
    }

    @Test
    fun testPresentRemoveLocationMarkers() {
        this.sut.presentRemoveLocationMarkers()
        assertTrue(this.displayerSpy.displayRemoveLocationMarkersCalled)
    }

    @Test
    fun testPresentNavigateToPhotoDetails() {
        this.sut.presentNavigateToPhotoDetails(STPhotoMapModels.PhotoDetailsNavigation.Response("photoId"))
        assertTrue(this.displayerSpy.displayNavigateToPhotoDetailsCalled)
    }

    @Test
    fun testPresentLocationOverlay() {
        this.sut.presentLocationOverlay(STPhotoMapModels.LocationOverlay.Response(STPhoto("photoId", Date())))
        assertTrue(this.displayerSpy.displayLocationOverlayCalled)
    }

    @Test
    fun testPresentRemoveLocationOverlay() {
        this.sut.presentRemoveLocationOverlay()
        assertTrue(this.displayerSpy.displayRemoveLocationOverlayCalled)
    }
}