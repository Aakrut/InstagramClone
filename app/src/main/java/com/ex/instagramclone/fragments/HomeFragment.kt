package com.ex.instagramclone.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.instagramclone.adapter.PostAdapter
import com.ex.instagramclone.adapter.StoryAdapter
import com.ex.instagramclone.databinding.FragmentHomeBinding
import com.ex.instagramclone.model.Post
import com.ex.instagramclone.model.Story
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private lateinit var homeBinding: FragmentHomeBinding

    //Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    private var post : MutableList<Post> ?= null

    private var story : MutableList<Story> ?= null

    private var followingList : MutableList<String> ?= null

    private var postAdapter: PostAdapter ?= null

    private var storyAdapter: StoryAdapter ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        val view = homeBinding.root

        firebaseAuth = Firebase.auth

        post = ArrayList()

        story = ArrayList()

        postAdapter = context?.let { PostAdapter(it, post as ArrayList<Post>) }

        homeBinding.recyclerViewHome.setHasFixedSize(true)
        homeBinding.recyclerViewHome.layoutManager = LinearLayoutManager(context)
        homeBinding.recyclerViewHome.adapter = postAdapter

        storyAdapter = context?.let { StoryAdapter(it, post as ArrayList<Story>) }

        homeBinding.recyclerViewHomeStory.setHasFixedSize(true)
        homeBinding.recyclerViewHomeStory.layoutManager = LinearLayoutManager(context)
        homeBinding.recyclerViewHomeStory.adapter = storyAdapter

        checkFollowing()

        return view
    }

    private fun checkFollowing() {
        followingList = ArrayList()

        val db = Firebase.firestore

        val reff = db.collection("Follow").document(firebaseAuth.currentUser!!.uid).collection("Following")

        reff.get().addOnSuccessListener {
            result ->

            (followingList as ArrayList<String>).clear()

            for (document in result) {
                Log.d(TAG, "${document.id} => ${document.data}")


                document.id.let{
                    (followingList as ArrayList<String>).add(it)
                }

                retreivePosts()
                retrieveStory()

            }

        }.addOnFailureListener{
            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun retrieveStory() {
        val db = Firebase.firestore
        db.collection("Story").document().get().addOnSuccessListener {

            result ->

        val timeCurrent = System.currentTimeMillis()


                (story as ArrayList<Story>).clear()

                firebaseAuth.currentUser?.let { it1 -> Story("",0,0,"", it1.uid).toString() }?.let { it2 -> (followingList as ArrayList<String>).add(it2) }


            for(id in followingList!!){
                var countStory = 0

                var story : Story ?= null

                for (document in result.getDocumentReference(id)!!.path) {
                    Log.d(TAG, "${document} => ${document}")
                    story = result.toObject(Story::class.java)
                    if(timeCurrent>story!!.timestart && timeCurrent<story!!.timeend){
                        countStory++
                    }
                    if(countStory>0){
                        (story as ArrayList<Story>).add(story)
                    }
                }
                storyAdapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun retreivePosts() {
        val db = Firebase.firestore
        val ref = db.collection("Posts").orderBy("publish_time", Query.Direction.DESCENDING)

        ref.get().addOnSuccessListener { result ->

            post?.clear()
            for (document in result) {
                Log.d(TAG, "${document.id} => ${document.data}")
                val post_i : Post = document.toObject(Post::class.java)
                Log.d(TAG, "retrieveAllPost: $post_i")
                for(userId in (followingList as ArrayList<String>)){
                    if(post_i.publisher == userId){
                        post?.add(post_i)
                        Log.d(TAG, "retrieveAllPost: SuccessFully Reached")
                    }
                }
                postAdapter?.notifyDataSetChanged()
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }
    }
}


