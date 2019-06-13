package com.streetography.stphotomap.scenes.stphotomap.cache

class STPhotoMapGeojsonCacheHandler() {
    var cache: STPhotoMapGeojsonCache
    var activeDownloads: ArrayList<String>

    init {
        this.cache = STPhotoMapGeojsonCache()
        this.activeDownloads = ArrayList()
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