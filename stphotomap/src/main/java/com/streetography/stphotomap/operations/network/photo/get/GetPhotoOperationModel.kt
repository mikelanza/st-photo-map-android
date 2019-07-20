package com.streetography.stphotomap.operations.network.photo.get

import com.streetography.stphotomap.models.photo.STPhoto

class GetPhotoOperationModel {
    class Request(val photoId: String, val includeUser: Boolean = true)
    class Response(val photo: STPhoto)
}