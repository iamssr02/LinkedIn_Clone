package com.example.linkedin_clone.DataClasses

data class ConnectionRequestUser(
    var id : String,
    var firstName : String? = null,
    var headline : String? = null,
    var profileImage : String? = null,
    var coverImage : String? = null
){
    constructor(): this("", "","","","")

    override fun toString(): String {
        return "ConnectionRequestUser(id='$id', firstName=$firstName, headline=$headline, profileImage=$profileImage, coverImage=$coverImage)"
    }


}
