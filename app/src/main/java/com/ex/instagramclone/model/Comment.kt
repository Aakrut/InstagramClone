package com.ex.instagramclone.model

data class Comment(val comment : String, val publisher : String){
    constructor() : this("","")
}
