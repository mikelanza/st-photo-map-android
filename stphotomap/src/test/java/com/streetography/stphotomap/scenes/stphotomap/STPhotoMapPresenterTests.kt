package com.streetography.stphotomap.scenes.stphotomap

import com.streetography.stphotomap.scenes.stphotomap.test.doubles.STPhotoMapDisplayLogicSpy
import junit.framework.TestCase
import org.junit.Test
import java.lang.ref.WeakReference

class STPhotoMapPresenterTests: TestCase() {
    lateinit var sut: STPhotoMapPresenter
    lateinit var displayerSpy: STPhotoMapDisplayLogicSpy

    private fun setupSubjectUnderTest() {
        this.sut = STPhotoMapPresenter()

        this.displayerSpy = STPhotoMapDisplayLogicSpy()
        this.sut.displayer = WeakReference(this.displayerSpy)
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