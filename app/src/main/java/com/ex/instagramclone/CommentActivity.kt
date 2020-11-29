package com.ex.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.instagramclone.adapter.CommentAdapter
import com.ex.instagramclone.databinding.ActivityCommentBinding
import com.ex.instagramclone.model.Comment
import com.ex.instagramclone.model.Post
import com.ex.instagramclone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class CommentActivity : AppCompatActivity() {

    private lateinit var commentBinding: ActivityCommentBinding

    private var firebase_user : FirebaseUser?= null

    private var postId = ""
    private var publisherId = ""

    private var commentAdapter : CommentAdapter ?= null
    private var commentList : MutableList<Comment> ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commentBinding = ActivityCommentBinding.inflate(layoutInflater)
        val view = commentBinding.root
        setContentView(view)

        firebase_user = FirebaseAuth.getInstance().currentUser

        val intent = intent
        postId = intent.getStringExtra("postId").toString()
        publisherId = intent.getStringExtra("publisherId").toString()

        commentList = ArrayList()

        commentAdapter = CommentAdapter(this, commentList as ArrayList<Comment>)

        commentBinding.recyclerViewComment.setHasFixedSize(true)
        commentBinding.recyclerViewComment.layoutManager = LinearLayoutManager(this)
        commentBinding.recyclerViewComment.adapter = commentAdapter


        val firebase_firestore = Firebase.firestore
        val ref = firebase_firestore.collection("Users").document(firebase_user!!.uid)

        ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("CommentActivity", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("CommentActivity", "Current data: ${snapshot.data}")
                val user = snapshot.toObject(User::class.java)
                Picasso.get().load(user!!.photo_url).into(commentBinding.profileImageCommentCircle)
            } else {
                Log.d("CommentActivity", "Current data: null")
            }
        }

        readComments()
        retreivepostImage()

        commentBinding.postComment.setOnClickListener {
            if(commentBinding.addComment.text.toString() == ""){
                Toast.makeText(this, "Add comment", Toast.LENGTH_SHORT).show()
            }else{
                addcomment()
            }
        }
    }

    private fun addcomment() {
        val firebse_firestore = Firebase.firestore
        val ref = firebse_firestore.collection("Comments").document(postId)

        val hasmapcomment = hashMapOf(
            "comment" to commentBinding.addComment.text.toString(),
            "publisher" to firebase_user!!.uid
        )

        ref.set(hasmapcomment)

        commentBinding.addComment.text.clear()
    }

    private fun readComments(){
        val firebase_firestre = Firebase.firestore
        val commentref = firebase_firestre.collection("Comments")

        commentref.get().addOnSuccessListener { result  ->
            commentList?.clear()
            for (document in result) {
                Log.d("CommentActivity", "${document.id} => ${document.data}")
                val comment = document.toObject(Comment::class.java)
                commentList?.add(comment)
            }
            commentAdapter?.notifyDataSetChanged()
        }
    }

    private fun retreivepostImage(){
        val firebase_firestore = Firebase.firestore
        val ref = firebase_firestore.collection("Posts").document(postId)

        ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("CommentActivity", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("CommentActivity", "Current data: ${snapshot.data}")
                val user = snapshot.toObject(Post::class.java)
                Picasso.get().load(user!!.post_image_url).into(commentBinding.postImageComment)
            } else {
                Log.d("CommentActivity", "Current data: null")
            }
        }
    }


}