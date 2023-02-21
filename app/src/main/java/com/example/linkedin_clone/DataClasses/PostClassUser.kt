package com.example.linkedin_clone.DataClasses

import java.util.Date

data class PostClassUser(
    var id: String,
    var caption: String,
    var imageURL: String,
    var uploadedBy: String,
    var timeStamp: Date
) {
   constructor(): this("","","","",Date())

}