package com.streetography.stphotomap.scenes.stphotomap.entity_level

import com.streetography.stphotomap.models.entity_level.EntityLevel

interface STPhotoMapEntityLevelHandlerDelegate {
    fun photoMapEntityLevelHandler(newEntityLevel: EntityLevel)
    fun photoMapEntityLevelHandlerNewLocationLevel(level: EntityLevel)
}

class STPhotoMapEntityLevelHandler {
    var entityLevel: EntityLevel
    var activeDownloads: ArrayList<String>
    val delegate: STPhotoMapEntityLevelHandlerDelegate?

    constructor(delegate: STPhotoMapEntityLevelHandlerDelegate?) {
        this.entityLevel = EntityLevel.unknown
        this.activeDownloads = ArrayList()
        this.delegate = delegate
    }

    fun hasActiveDownload(url: String): Boolean {
        return this.activeDownloads.contains(url)
    }

    fun addActiveDownload(url: String) {
        this.activeDownloads.add(url)
    }

    fun removeActiveDownload(url: String) {
        this.activeDownloads.remove(url)
    }

    fun removeAllActiveDownloads() {
        this.activeDownloads.clear()
    }

    fun change(entityLevel: EntityLevel) {
        if (entityLevel != EntityLevel.unknown && this.entityLevel != entityLevel) {
            this.entityLevel = entityLevel

            when(entityLevel) {
                EntityLevel.location -> this.delegate?.photoMapEntityLevelHandler(entityLevel)
                else -> this.delegate?.photoMapEntityLevelHandlerNewLocationLevel(entityLevel)
            }
        }
    }
}