package com.example.linkedin_clone.DataClasses

data class ConnectionRequestUser(

    var firstName : String? = null
){
    constructor(): this("")

    override fun toString(): String {
        return "ConnectionRequestUser(firstName=$firstName)"
    }

}
