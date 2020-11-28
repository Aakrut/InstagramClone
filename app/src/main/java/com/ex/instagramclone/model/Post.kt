package com.ex.instagramclone.model

data class Post(val post_id : String, val photo_caption : String, val post_image_url : String , val publisher : String, val publish_time : String){
    constructor() :this("","","","","")
}
