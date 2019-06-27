package com.streetography.stphotomap.scenes.stphotomap.location_level

import java.util.concurrent.CopyOnWriteArrayList

class STPhotoMapLocationLevelHandler() {
    var activeDownloads: CopyOnWriteArrayList<String>

    init {
        this.activeDownloads = CopyOnWriteArrayList()
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
}