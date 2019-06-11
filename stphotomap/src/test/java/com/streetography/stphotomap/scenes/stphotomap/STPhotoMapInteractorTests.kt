package com.streetography.stphotomap.scenes.stphotomap

import com.streetography.stphotomap.scenes.stphotomap.interactor.STPhotoMapInteractor
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STPhotoMapPresentationLogicSpy
import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STPhotoMapWorkerSpy
import junit.framework.TestCase
import org.junit.Test

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
}