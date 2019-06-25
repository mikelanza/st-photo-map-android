package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import android.content.Context
import com.streetography.stphotomap.scenes.stphotomap.views.STEntityLevelView

class STEntityLevelViewSpy(context: Context): STEntityLevelView(context) {
    var setTitleCalled: Boolean = false
    var setImageCalled: Boolean = false
    var showCalled: Boolean = false

    override fun setTitle(titleId: Int) {
        this.setTitleCalled = true
    }

    override fun setImage(resourceId: Int) {
        this.setImageCalled = true
    }

    override fun show() {
        this.showCalled = true
    }
}