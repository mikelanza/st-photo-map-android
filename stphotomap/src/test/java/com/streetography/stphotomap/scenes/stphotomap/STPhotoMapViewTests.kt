package com.streetography.stphotomap.scenes.stphotomap

import android.view.View
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.maps.model.LatLng
import com.streetography.stphotomap.R
import com.streetography.stphotomap.models.coordinate.Coordinate
import com.streetography.stphotomap.models.entity_level.EntityLevel
import com.streetography.stphotomap.models.location.STLocation
import com.streetography.stphotomap.scenes.stphotomap.seeds.STPhotoMapSeeds
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STEntityLevelViewSpy
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STPhotoMapBusinessLogicSpy
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STPhotoMapViewDelegateSpy
import com.streetography.stphotomap.scenes.stphotomap.views.STLocationOverlayView
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

    @Test
    fun testShouldNavigateToPhotoDetailsWhenPhotoMapMarkerHandlerDidReselectPhoto() {
        this.sut.photoMapMarkerHandlerDidReselectPhoto("photoId")
        assertTrue(this.interactorSpy.shouldNavigateToPhotoDetailsCalled)
    }

    @Test
    fun testShouldGetPhotoDetailsForPhotoMarkerWhenPhotoMapMarkerHandlerDidSelectPhoto() {
        this.sut.photoMapMarkerHandlerDidSelectPhoto("photoId")
        assertTrue(this.interactorSpy.shouldGetPhotoDetailsForPhotoMarkerCalled)
    }

    @Test
    fun testShouldNavigateToPhotoCollectionWhenOnClickOnMap() {
        this.sut.onMapClick(LatLng(50.0, 50.0))
        assertTrue(this.interactorSpy.shouldNavigateToPhotoCollectionCalled)
    }

    @Test
    fun testShouldZoomToCoordinateForWhenPhotoMarker() {
        this.sut.photoMapMarkerHandlerZoomToCoordinate(Coordinate(50.0, 50.0))
        assertTrue(this.interactorSpy.shouldZoomToCoordinateCalled)
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

    @Test
    fun testDisplayNavigateToPhotoDetails() {
        val delegateSpy = STPhotoMapViewDelegateSpy()
        this.sut.delegate = delegateSpy

        this.sut.displayNavigateToPhotoDetails(STPhotoMapModels.PhotoDetailsNavigation.ViewModel("photoId"))
        assertTrue(delegateSpy.photoMapViewNavigateToPhotoDetailsForPhotoIdCalled)
    }

    @Test
    fun testDisplayNavigateToPhotoCollection() {
        val delegateSpy = STPhotoMapViewDelegateSpy()
        this.sut.delegate = delegateSpy

        this.sut.displayNavigateToPhotoCollection(STPhotoMapModels.PhotoCollectionNavigation.ViewModel(STLocation(50.0, 50.0), EntityLevel.city, "userId", null))
        assertTrue(delegateSpy.photoMapViewNavigateToPhotoCollectionCalled)
    }

    @Test
    fun testDisplayLocationOverlayShouldShowLocationOverlayView() {
        this.sut.locationOverlayView?.visibility = View.GONE

        this.sut.displayLocationOverlay(STPhotoMapModels.LocationOverlay.ViewModel("photoId", "Title", "27 May, 2019", "Description"))

        this.sut.post {
            assertEquals(View.VISIBLE, this.sut.locationOverlayView?.visibility)
        }
    }

    @Test
    fun testDisplayLocationOverlayShouldUpdateLocationOverlayViewModel() {
        this.sut.locationOverlayView?.model = STLocationOverlayView.Model("photoId", "Title", "27 May, 2019", "Description")

        val photoId = "newPhotoId"
        val title = "New title"
        val time = "19 July, 2019"
        val description = "New description"
        this.sut.displayLocationOverlay(STPhotoMapModels.LocationOverlay.ViewModel(photoId, title, time, description))

        this.sut.post {
            assertEquals(photoId, this.sut.locationOverlayView?.model?.photoId)
            assertEquals(title, this.sut.locationOverlayView?.model?.title)
            assertEquals(time, this.sut.locationOverlayView?.model?.time)
            assertEquals(description, this.sut.locationOverlayView?.model?.description)
        }
    }

    @Test
    fun testDisplayRemoveLocationOverlayShouldHideLocationOverlayView() {
        this.sut.locationOverlayView?.visibility = View.VISIBLE

        this.sut.displayRemoveLocationOverlay()

        this.sut.post {
            assertEquals(View.GONE, this.sut.locationOverlayView?.visibility)
        }
    }

    @Test
    fun testDisplayRemoveLocationOverlayShouldRemoveLocationOverlayModel() {
        this.sut.locationOverlayView?.model = STLocationOverlayView.Model("photoId", "Title", "27 May, 2019", "Description")

        this.sut.displayRemoveLocationOverlay()

        this.sut.post {
            assertNull(this.sut.locationOverlayView?.model)
        }
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
        }, 5000)
    }

    @Test
    fun testDisplayRemoveLocationMarkers() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                sut.displayRemoveLocationMarkers()

                assertEquals(sut.markerHandler?.markers?.size, 0)
                assertEquals(sut.markerHandler?.clusterManager?.markerCollection?.markers?.size, 0)
            }
        }, 5000)
    }
    //endregion
}