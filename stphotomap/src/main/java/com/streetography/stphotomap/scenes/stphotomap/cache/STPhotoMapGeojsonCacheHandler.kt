package com.streetography.stphotomap.scenes.stphotomap.cache

import java.util.concurrent.CopyOnWriteArrayList

class STPhotoMapGeojsonCacheHandler() {
    var cache: STPhotoMapGeojsonCache
    var activeDownloads: CopyOnWriteArrayList<String>

    init {
        this.cache = STPhotoMapGeojsonCache()
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

    fun shouldPrepareTileForCaching(url: String): Boolean {
        if (this.hasActiveDownload(url)) {
            return false
        }
        return this.cache.getTile(url) == null
    }

    fun activeDownloadCount(): Int {
        return this.activeDownloads.size
    }
}