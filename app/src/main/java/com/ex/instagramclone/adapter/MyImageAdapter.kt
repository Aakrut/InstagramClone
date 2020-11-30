package com.ex.instagramclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ex.instagramclone.R
import com.ex.instagramclone.fragments.MyImageFragment
import com.ex.instagramclone.model.Post
import com.squareup.picasso.Picasso

class MyImageAdapter(val context: Context , val mPostList : List<Post>) : RecyclerView.Adapter<MyImageAdapter.MyImageViewHolder>() {

    inner class MyImageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var image_post_i : ImageView?= null
        init {
            image_post_i = itemView.findViewById(R.id.post_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyImageViewHolder {
        return MyImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_image_item_list,parent,false))
    }

    override fun onBindViewHolder(holder: MyImageViewHolder, position: Int) {
        val current_item  = mPostList[position]

        Picasso.get().load(current_item.post_image_url).into(holder.image_post_i)

        holder.image_post_i?.setOnClickListener {
            val image = context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit()
            image.putString("postId",current_item.post_id)
            image.apply()
            (context as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.container,MyImageFragment()).commit()
        }
    }

    override fun getItemCount(): Int {
        return mPostList.size
    }
}