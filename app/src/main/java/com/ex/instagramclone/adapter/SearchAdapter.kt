package com.ex.instagramclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ex.instagramclone.R
import com.ex.instagramclone.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter(val context : Context,val mUserList : List<User>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            var cirlce_profile : CircleImageView ?= null
            var text_username : TextView ?= null
            var text_fullname : TextView ?= null

        init {
            cirlce_profile = itemView.findViewById(R.id.circle_image_search_item_list)
            text_username = itemView.findViewById(R.id.text_username_search_item_list)
            text_fullname = itemView.findViewById(R.id.text_fullname_search_item_list)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_item_list,parent,false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val current_item = mUserList[position]

        holder.text_username?.text = current_item.username
        holder.text_fullname?.text = current_item.fullname

        Picasso.get().load(current_item.photo_url).into(holder.cirlce_profile)
    }

    override fun getItemCount(): Int {
       return mUserList.size
    }
}