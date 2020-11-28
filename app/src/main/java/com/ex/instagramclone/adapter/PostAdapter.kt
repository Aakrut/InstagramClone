package com.ex.instagramclone.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ex.instagramclone.R
import com.ex.instagramclone.model.Post
import com.ex.instagramclone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PostAdapter(val context : Context, val mPostList : List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var firebase_user : FirebaseUser? = Firebase.auth.currentUser

    inner class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var circle_image : CircleImageView ?= null
        var text_username : TextView ?= null
        var text_time : TextView ?= null
        var desc_text : TextView ?= null
        var fullname_text : TextView ?= null
        var post_i : ImageView ?= null
        var btn_like : ImageView ?= null
        var comment : ImageView ?= null
        var number_text_like : TextView ?= null
        init {
            circle_image = itemView.findViewById(R.id.circle_image_home_item_list)
            text_username = itemView.findViewById(R.id.username_home_item_list)
            text_time = itemView.findViewById(R.id.time_home_item_list)
            desc_text = itemView.findViewById(R.id.description_number_home_item_list)
            fullname_text = itemView.findViewById(R.id.fullname_home_item_list)
            post_i = itemView.findViewById(R.id.post_image)
            btn_like = itemView.findViewById(R.id.like_button)
            comment = itemView.findViewById(R.id.comment_button)
            number_text_like = itemView.findViewById(R.id.like_number_home_item_list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
       val current_item : Post = mPostList[position]

       holder.text_username!!.text = current_item.publisher
       holder.text_time!!.text = current_item.publish_time

        publisherInfo(holder.circle_image, holder.text_username!!,holder.fullname_text, current_item.publisher)
    }



    override fun getItemCount(): Int {
        return mPostList.size
    }

    private fun publisherInfo(circleImage: CircleImageView?, textUsername: TextView, fullnameText: TextView?, publisher: String) {

        val  firebase_firestore = Firebase.firestore
        val ref = firebase_firestore.collection("Users").document(firebase_user!!.uid)

        ref.addSnapshotListener { snapshot, error ->

            if (error != null) {
                Log.d("PostAdapter", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("PostAdapter", "Current data: ${snapshot.data}")
                val user = snapshot.toObject(User::class.java)
                textUsername!!.text = user!!.username

                fullnameText!!.text = user.fullname

                Picasso.get().load(user.photo_url).into(circleImage)
            } else {
                Log.d("PostAdapter", "Current data: null")
            }

        }
    }
}