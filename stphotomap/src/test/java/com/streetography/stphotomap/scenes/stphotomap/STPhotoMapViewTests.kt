package com.streetography.stphotomap.scenes.stphotomap

import android.content.Context
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STPhotoMapBusinessLogicSpy
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class STPhotoMapViewTests: TestCase() {
    lateinit var sut: STPhotoMapView
    lateinit var interactorSpy: STPhotoMapBusinessLogicSpy

    @Mock
    private lateinit var mockContext: Context

    private fun setupSubjectUnderTest() {
        this.sut = STPhotoMapView(this.mockContext)

        this.interactorSpy = STPhotoMapBusinessLogicSpy()
        this.sut.interactor = this.interactorSpy
    }

    override fun setUp() {
        super.setUp()
        this.setupSubjectUnderTest()
    }

    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun test() {
        assertTrue(true)
    }

    @Test
    fun testShouldUpdateVisibleTilesWhenCameraIdle() {
        this.sut?.onCameraIdle()
        Assert.assertTrue(this.interactorSpy.shouldUpdateVisibleTilesCalled)
    }
}