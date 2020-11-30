package com.ex.instagramclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ex.instagramclone.R
import com.ex.instagramclone.model.Story
import de.hdodenhof.circleimageview.CircleImageView

class StoryAdapter(val context: Context,val mStoryList : List<Story>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        var story_image : CircleImageView ?= null
        var story_image_seen : CircleImageView ?= null
        var story_username : TextView ?= null

        var story_image_plus_btn : ImageView ?= null
        var add_story : TextView ?= null

        init {

            story_image = itemView.findViewById(R.id.story_image)
            story_image_seen = itemView.findViewById(R.id.story_image_seen)
            story_username = itemView.findViewById(R.id.username_story_item)

            story_image_plus_btn = itemView.findViewById(R.id.add_story_image_plus)
            add_story = itemView.findViewById(R.id.add_story_text)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        if(viewType == 0){
            return StoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.add_story_item,parent,false))
        }else{
            return StoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.story_item,parent,false))
        }
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val current_item = mStoryList[position]


    }

    override fun getItemCount(): Int {
        return mStoryList.size
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0){
            return  0
        }
        return  1
    }
}