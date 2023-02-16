package com.example.linkedin_clone.DataClasses

class User {
    private var name: String = ""
    private var email: String = ""
    private var uid: String = ""

    constructor()
    constructor(
        email: String,
        name: String,
        uid: String
    ) {
        this.email = email
        this.name = name
        this.uid = uid
    }

    fun getName(): String {
        return name
    }

    fun setName(mobile: String) {
        this.name = mobile
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(mobile: String) {
        this.email = mobile
    }
    fun getUID() : String {
        return uid
    }
    fun setUID(uid: String) {
        this.uid = uid
    }
}