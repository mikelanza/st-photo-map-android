package com.streetography.stphotomap.models.photo

import com.streetography.stphotomap.models.user.STUser
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class STPhoto(var id: String, var createdAt: Date) {
    var user: STUser? = null

    var imageUrl: String? = null
    var image1200Url: String? = null
    var image750Url: String? = null
    var image650Url: String? = null

    var text: String = ""
    var fhUsername: String = ""

    companion object {
        fun fromJSON(jsonObject: JSONObject): STPhoto {
            val id = jsonObject.getString(Parameters.ObjectId.value)
            val createdAtString = jsonObject.getString(Parameters.CreatedAt.value)
            val createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(createdAtString)
            val imageUrl = jsonObject.getString(Parameters.ImageOriginalURL.value)
            val image1200Url = jsonObject.getString(Parameters.ImageWidth1200URL.value)
            val image750Url = jsonObject.getString(Parameters.ImageWidth750URL.value)
            val image650Url = jsonObject.getString(Parameters.ImageWidth650URL.value)
            val text = jsonObject.getString(Parameters.DescriptionText.value)
            val fhUsername = jsonObject.getString(Parameters.FhOwnerUsername.value)
            val userObject: JSONObject? =
                if (jsonObject.has(Parameters.User.value) && !jsonObject.isNull(Parameters.User.value)) jsonObject.getJSONObject(Parameters.User.value) else null

            val photo = STPhoto(id, createdAt)
            photo.imageUrl = imageUrl
            photo.image1200Url = image1200Url
            photo.image750Url = image750Url
            photo.image650Url = image650Url
            photo.text = text
            photo.fhUsername = fhUsername
            photo.user = if (userObject != null) STUser.fromJSON(userObject) else null
            return photo
        }

        enum class Parameters(val value: String) {
            ObjectId("objectId"),
            CreatedAt("createdAt"),
            User("owner"),
            DescriptionText("descriptionText"),
            FhOwnerUsername("fhOwnerUsername"),
            ImageOriginalURL("imageOriginalURL"),
            ImageWidth1200URL("imageWidth1200URL"),
            ImageWidth750URL("imageWidth750URL"),
            ImageWidth650URL("imageWidth650URL")
        }
    }
}