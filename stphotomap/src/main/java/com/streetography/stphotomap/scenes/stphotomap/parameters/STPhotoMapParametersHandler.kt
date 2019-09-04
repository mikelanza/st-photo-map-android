package com.streetography.stphotomap.scenes.stphotomap.parameters

import com.streetography.stphotomap.models.parameters.KeyValue
import com.streetography.stphotomap.models.parameters.Parameters
import java.util.*

class STPhotoMapParametersHandler {
    var parameters: ArrayList<KeyValue>

    companion object {
        val instance = STPhotoMapParametersHandler()
    }

    init {
        this.parameters = defaultParameters()
    }

    private fun defaultParameters(): ArrayList<KeyValue> {
        return arrayListOf(
            KeyValue(Parameters.Keys.basemap, "yes"),
            KeyValue(Parameters.Keys.shadow, "yes"),
            KeyValue(Parameters.Keys.sort, "popular"),
            KeyValue(Parameters.Keys.tileSize, "256"),
            KeyValue(Parameters.Keys.pinOptimize, "4"),
            KeyValue(Parameters.Keys.sessionId,  UUID.randomUUID().toString())
        )
    }

    fun update(parameter: KeyValue) {
        this.parameters.removeAll {
            it.first == parameter.first
        }
        this.parameters.add(parameter)
    }

    fun remove(parameter: KeyValue) {
        this.parameters.removeAll {
            it.first == parameter.first
        }
    }

    fun reset() {
        this.parameters = this.defaultParameters()
    }

    fun userId(): String? {
        return this.parameters.firstOrNull { it.first == Parameters.Keys.userId }?.second
    }

    fun collectionId(): String? {
        return this.parameters.firstOrNull { it.first == Parameters.Keys.collectionId }?.second
    }
}