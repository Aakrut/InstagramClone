package com.ex.instagramclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ex.instagramclone.R

class MyImageAdapter : RecyclerView.Adapter<MyImageAdapter.MyImageViewHolder>() {

    inner class MyImageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var image_post_i : ImageView?= null
        init {
            image_post_i = itemView.findViewById(R.id.post_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyImageViewHolder {
        return MyImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.))
    }

    override fun onBindViewHolder(holder: MyImageViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}