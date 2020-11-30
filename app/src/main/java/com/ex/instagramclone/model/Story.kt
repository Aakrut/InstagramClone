package com.ex.instagramclone.model

data class Story(val imageurl : String,val timestart : Long,val timeend : Long , val story_id : String, val description : String){
            constructor():this("",0,0,"","")
}
