package com.streetography.stphotomap.models.photo

import com.streetography.stphotomap.models.user.STUser
import java.util.*

class STPhoto(var id: String, var createdAt: Date) {
    var user: STUser? = null

    var imageUrl: String? = null
    var image1200Url: String? = null
    var image750Url: String? = null
    var image650Url: String? = null

    var text: String = ""
    var fhUsername: String = ""
}