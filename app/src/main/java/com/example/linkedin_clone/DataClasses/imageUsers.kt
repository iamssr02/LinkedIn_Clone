package com.example.linkedin_clone.DataClasses

import java.util.*

data class imageUsers(
//    var likes : Int = 0,
    var id: String,
    var caption: String,
    var imageURL: String,
    var profileImageURL: String,
    var uploadedBy: String,
    var name: String,
    var headline: String,
    var timeStamp: String
) {
    constructor() : this("","", "", "", "","","", "")
}
