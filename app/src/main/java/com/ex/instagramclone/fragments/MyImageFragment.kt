package com.ex.instagramclone.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ex.instagramclone.R
import com.ex.instagramclone.adapter.PostAdapter
import com.ex.instagramclone.databinding.FragmentMyImageBinding
import com.ex.instagramclone.model.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MyImageFragment : Fragment() {

    private val TAG = "MyImageFragment"

    private var postAdapter: PostAdapter ?= null
    private var post : MutableList<Post> ?= null
    private var post_Id : String = ""

    private lateinit var myImageBinding: FragmentMyImageBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myImageBinding = FragmentMyImageBinding.inflate(layoutInflater,container,false)
        val view = myImageBinding.root

        post = ArrayList()

        val prefs = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if(prefs != null){
            post_Id = prefs.getString("postId","none").toString()
        }

        myImageBinding.recyclerViewImageDetails.setHasFixedSize(true)
        myImageBinding.recyclerViewImageDetails.layoutManager = LinearLayoutManager(context)
        postAdapter = context?.let { PostAdapter(it, post as ArrayList<Post>) }
        myImageBinding.recyclerViewImageDetails.adapter = postAdapter

        retreivePosts()


        return view
    }

    private fun retreivePosts() {
        val db = Firebase.firestore
        val ref = db.collection("Posts").document(post_Id)

        ref.addSnapshotListener { value, error ->
            post!!.clear()
            if (error != null) {
                Log.w(TAG, "Listen failed.", error)
                return@addSnapshotListener
            }

            if (value != null && value.exists()) {
                Log.d(TAG, "Current data: ${value.data}")
                val post_i : Post? = value.toObject(Post::class.java)
                if (post_i != null) {
                    post?.add(post_i)
                    Log.d(TAG, "retrieveAllPost: SuccessFully Reached")
                }
                postAdapter?.notifyDataSetChanged()

            } else {
                Log.d(TAG, "Current data: null")
            }
        }

    }

}