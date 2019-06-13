package com.streetography.stphotomap.extensions.uri

import android.net.Uri
import com.streetography.stphotomap.models.parameters.KeyValue

fun Uri.excludeParameter(parameter: KeyValue): Uri {
    val parameterNames = this.queryParameterNames
    val uriBuilder = this.buildUpon().clearQuery()
    parameterNames.forEach {
        if (it != parameter.first) {
            uriBuilder.appendQueryParameter(it, this.getQueryParameter(it))
        }
    }
    return uriBuilder.build()
}

fun Uri.addParameters(parameters: ArrayList<KeyValue>): Uri {
    val uriBuilder = this.buildUpon()
    parameters.forEach {
        uriBuilder.appendQueryParameter(it.first, it.second)
    }
    return uriBuilder.build()
}