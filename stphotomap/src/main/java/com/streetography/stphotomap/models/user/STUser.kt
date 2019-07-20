package com.streetography.stphotomap.models.user

import org.json.JSONObject

class STUser(var id: String) {
    var firstName: String = ""
    var lastName: String = ""

    val name: String
        get() = this.firstName + " " + this.lastName

    companion object {
        fun fromJSON(jsonObject: JSONObject): STUser {
            val id = jsonObject.getString(Parameters.ObjectId.value)
            val firstName = jsonObject.getString(Parameters.FirstName.value)
            val lastName = jsonObject.getString(Parameters.LastName.value)

            val user = STUser(id)
            user.firstName = firstName
            user.lastName = lastName
            return user
        }

        enum class Parameters(val value: String) {
            ObjectId("objectId"),
            FirstName("firstName"),
            LastName("lastName")
        }
    }
}