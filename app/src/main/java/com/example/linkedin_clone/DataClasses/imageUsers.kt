package com.example.linkedin_clone.DataClasses

data class imageUsers(var caption : String,
                      var imageURL : String? = null
){
    constructor(): this("", "")

    override fun toString(): String {
        return "imageUsers(caption='$caption', imageURL=$imageURL)"
    }


}
