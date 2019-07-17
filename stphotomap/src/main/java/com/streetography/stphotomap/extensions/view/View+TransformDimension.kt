package com.streetography.stphotomap.extensions.view

import android.util.TypedValue
import android.view.View

fun View.transformDimension(value: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, this.resources.displayMetrics)
}