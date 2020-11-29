package com.ex.instagramclone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ex.instagramclone.CommentActivity
import com.ex.instagramclone.MainActivity
import com.ex.instagramclone.R
import com.ex.instagramclone.model.Post
import com.ex.instagramclone.model.User
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
        var desc_text : TextView ?= null
        var fullname_text : TextView ?= null
        var post_i : ImageView ?= null
        var btn_like : ImageView ?= null
        var comment : ImageView ?= null
        var number_text_like : TextView ?= null
        var comment_text_like : TextView ?= null
        init {
            circle_image = itemView.findViewById(R.id.circle_image_home_item_list)
            text_username = itemView.findViewById(R.id.username_home_item_list)
            desc_text = itemView.findViewById(R.id.description_number_home_item_list)
            fullname_text = itemView.findViewById(R.id.fullname_home_item_list)
            post_i = itemView.findViewById(R.id.post_image)
            btn_like = itemView.findViewById(R.id.like_button)
            comment = itemView.findViewById(R.id.comment_button)
            number_text_like = itemView.findViewById(R.id.like_number_home_item_list)
            comment_text_like = itemView.findViewById(R.id.comment_text_home_item_list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
       val current_item : Post = mPostList[position]

        holder.text_username!!.text = current_item.publisher
        holder.desc_text!!.text = current_item.photo_caption
        Picasso.get().load(current_item.post_image_url).into(holder.post_i)

        islikes(current_item.post_id,holder.btn_like)

        numberofLikes(holder.number_text_like,current_item.post_id)

        numberofcomments(holder.comment_text_like,current_item.post_id)

        publisherInfo(holder.circle_image, holder.text_username!!,holder.fullname_text, current_item.publisher)

        holder.btn_like!!.setOnClickListener {
            if(holder.btn_like!!.tag == "Like" ){
                val firebase_firestore = Firebase.firestore
                val ref_like = firebase_firestore.collection("Likes").document(current_item.post_id)

                val hashmaplike = hashMapOf(
                        firebase_user!!.uid to true
                )

                ref_like.collection(firebase_user!!.uid).document(firebase_user!!.uid).set(hashmaplike)
            }else{

                val firebase_firestore = Firebase.firestore
                val ref_like = firebase_firestore.collection("Likes").document(current_item.post_id)

                ref_like.collection(firebase_user!!.uid).document(firebase_user!!.uid).delete()

                val intent = Intent(context,MainActivity::class.java)
                context.startActivity(intent)
            }
        }

        holder.comment!!.setOnClickListener {
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra("postId",current_item.post_id)
            intent.putExtra("publisherId",current_item.publisher)
            context.startActivity(intent)
        }

        holder.comment_text_like!!.setOnClickListener {
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra("postId",current_item.post_id)
            intent.putExtra("publisherId",current_item.publisher)
            context.startActivity(intent)
        }

    }


    private fun islikes(postId: String, btnLike: ImageView?) {
        val firebase_firestore = Firebase.firestore

        val likeRef = firebase_firestore.collection("Likes").document(postId).collection(firebase_user!!.uid).document(firebase_user!!.uid)

        likeRef.addSnapshotListener { snapshot, error ->

            if (error != null) {
                Log.w("PostAdapter", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("PostAdapter", "Current data: ${snapshot.data}")
                btnLike!!.setImageResource(R.drawable.ic_baseline_favorite_24)
                btnLike.tag = "Liked"
            } else {
                Log.d("PostAdapter", "Current data: null")
                btnLike!!.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                btnLike.tag = "Like"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun numberofcomments(commet: TextView?, postid: String) {
        val firebase_firestore = Firebase.firestore

        val number_ofcommentsRef = firebase_firestore.collection("Likes").document(postid)

        number_ofcommentsRef.get().addOnSuccessListener {
            document ->
            if (document != null) {
                Log.d("PostAdapter", "DocumentSnapshot data: ${document.data}")
                commet!!.text = "view all" + document.data?.size.toString() + "comments"
            } else {
                Log.d("PostAdapter", "No such document")
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun numberofLikes(likeText: TextView?, postid: String) {
        val firebase_firestore = Firebase.firestore

        val number_oflikeRef = firebase_firestore.collection("Likes").document(postid)

        number_oflikeRef.get().addOnSuccessListener {
            document ->
            if (document != null) {
                Log.d("PostAdapter", "DocumentSnapshot data: ${document.data}")
                likeText!!.text = document.data?.size.toString() + " likes"
            } else {
                Log.d("PostAdapter", "No such document")
            }
        }

    }


    override fun getItemCount(): Int {
        return mPostList.size
    }

    private fun publisherInfo(circleImage: CircleImageView?, textUsername: TextView, fullnameText: TextView?, publisher: String) {

        val  firebase_firestore = Firebase.firestore
        val ref = firebase_firestore.collection("Users").document(publisher)

        ref.addSnapshotListener { snapshot, error ->

            if (error != null) {
                Log.d("PostAdapter", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("PostAdapter", "Current data: ${snapshot.data}")
                val user = snapshot.toObject(User::class.java)
                textUsername.text = user!!.username

                fullnameText!!.text = user.fullname

                Picasso.get().load(user.photo_url).into(circleImage)
            } else {
                Log.d("PostAdapter", "Current data: null")
            }

        }
    }
}