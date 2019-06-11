package com.streetography.stphotomap.models.user

class STUser(var id: String) {
    var firstName: String = ""
    var lastName: String = ""

    val name: String
        get() = this.firstName + " " + this.lastName
}