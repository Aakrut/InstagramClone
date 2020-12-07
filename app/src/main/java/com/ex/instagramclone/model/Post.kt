package com.ex.instagramclone.model

import com.google.firebase.Timestamp

data class Post(val post_id : String, val photo_caption : String, val post_image_url : String , val publisher : String,val publish_time : Timestamp){
    constructor() :this("","","","", Timestamp.now())
}
