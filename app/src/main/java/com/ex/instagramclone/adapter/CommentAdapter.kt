package com.ex.instagramclone.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ex.instagramclone.R
import com.ex.instagramclone.model.Comment
import com.ex.instagramclone.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter(val context: Context, val mCommentList : List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var profile_image  : CircleImageView? = null
        var username_text   : TextView ? = null
        var user_comment  : TextView? = null

        init {
            profile_image = itemView.findViewById(R.id.user_profile_image_comment)
            username_text = itemView.findViewById(R.id.user_name_comment)
            user_comment = itemView.findViewById(R.id.comment_comment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_item_list,parent,false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
            val current_item = mCommentList[position]


        holder.user_comment!!.text = current_item.comment

        getUserInfo(holder.profile_image,holder.username_text,current_item.publisher)
    }

    private fun getUserInfo(profileImage: CircleImageView?, usernameText: TextView?, publisher: String) {
        val firebase_firestore = Firebase.firestore
        val ref = firebase_firestore.collection("Users").document(publisher)


        ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("CommentAdapter", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("CommentAdapter", "Current data: ${snapshot.data}")
                val user = snapshot.toObject(User::class.java)
                Picasso.get().load(user!!.photo_url).into(profileImage)
                usernameText!!.text = user.fullname
            } else {
                Log.d("CommentActivity", "Current data: null")
            }
        }


    }


    override fun getItemCount(): Int {
       return mCommentList.size
    }

}