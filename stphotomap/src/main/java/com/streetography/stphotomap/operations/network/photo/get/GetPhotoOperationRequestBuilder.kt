package com.streetography.stphotomap.operations.network.photo.get

import okhttp3.HttpUrl
import okhttp3.Request

class GetPhotoOperationRequestBuilder(val model: GetPhotoOperationModel.Request) {
    fun request(): Request {
        val url = "https://prod.streetography.com/v1/photos/" + this.model.photoId
        val urlBuilder = HttpUrl.parse(url)!!.newBuilder()
        urlBuilder.addQueryParameter("apisecret", "k9f2Hje7DM03Jyhf73hJ")
        urlBuilder.addQueryParameter("includeOwner", this.model.includeUser.toString())

        val buildUrl = urlBuilder.build().toString()
        return Request.Builder().url(buildUrl).build()
    }
}