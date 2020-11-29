package com.ex.instagramclone.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.instagramclone.EditProfileActivity
import com.ex.instagramclone.R
import com.ex.instagramclone.adapter.MyImageAdapter
import com.ex.instagramclone.databinding.FragmentProfileBinding
import com.ex.instagramclone.model.Post
import com.ex.instagramclone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class ProfileFragment : Fragment() {

    private val TAG = "ProfileFragment"

    private lateinit var firebase_auth : FirebaseAuth

    private lateinit var firebase_firestorage : FirebaseStorage

    private lateinit var firebase_firestore : FirebaseFirestore

    private lateinit var profileBinding: FragmentProfileBinding

    private lateinit var profileID : String

    var postList : List<Post> ?= null

    var myImageAdapter : MyImageAdapter ?= null

    private lateinit var follower_number : TextView
    private lateinit var following_number : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        profileBinding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = profileBinding.root

        firebase_auth = Firebase.auth

        firebase_firestore = Firebase.firestore

        follower_number = profileBinding.root.findViewById(R.id.follower_number)
        following_number = profileBinding.root.findViewById(R.id.following_number)

        retreiveInfromation()

        val pref = context!!.getSharedPreferences("PREFS",Context.MODE_PRIVATE)
        if(pref != null){
            context.profileID = pref.getString("profileID","none").toString()
        }


        profileBinding.recyclerViewProfile.setHasFixedSize(true)
        profileBinding.recyclerViewProfile.layoutManager = LinearLayoutManager(context)



        postList = ArrayList()
        myImageAdapter = MyImageAdapter(context!!, postList as ArrayList<Post>)


        profileBinding.recyclerViewProfile.adapter = myImageAdapter


        if(profileID == firebase_auth.currentUser!!.uid){
            profileBinding.buttonEditProfile.text = "Edit Your Profile"
        }else if(profileID != firebase_auth.currentUser!!.uid){
            checkFollowandFollowingButtons()
        }



        profileBinding.buttonEditProfile.setOnClickListener {
            val choosebutton = profileBinding.buttonEditProfile.text.toString()

            when{
                choosebutton == "Edit Your Profile" ->  {
                    val intent = Intent(context,EditProfileActivity::class.java)
                    startActivity(intent)
                }

                choosebutton == "Follow" -> {
                    val firebase_firestore = Firebase.firestore

                    firebase_auth.currentUser?.uid.let {
                            it ->
                        val firebase_firestore = Firebase.firestore
                        val reff = firebase_firestore.collection("Follow").document(it.toString())

                        val hashmapref = hashMapOf(
                            profileID to true
                        )

                        reff.collection("Following").document(profileID).set(hashmapref)

                    }

                    firebase_auth.currentUser?.uid.let {
                            it ->
                        val firebase_firestore = Firebase.firestore
                        val reff = firebase_firestore.collection("Follow").document(profileID)

                        val hashmapref = hashMapOf(
                            profileID to true
                        )

                        reff.collection("Followers").document(it.toString()).set(hashmapref)

                    }


                }

                choosebutton == "Following" -> {
                    val firebase_firestore = Firebase.firestore

                    firebase_auth.currentUser?.uid.let {
                            it ->
                        val firebase_firestore = Firebase.firestore
                        val reff = firebase_firestore.collection("Follow").document(it.toString()).
                        collection("Following").document(profileID).delete()

                    }

                    firebase_auth.currentUser?.uid.let {
                            it ->
                        val firebase_firestore = Firebase.firestore
                        val reff = firebase_firestore.collection("Follow").document(profileID).
                        collection("Followers").document(it.toString()).delete()

                    }


                }



            }
        }



        retreiveFollowers()
        retreiveFollowings()
        myPhotos()


        profileBinding.buttonEditProfile.setOnClickListener {
            startActivity(Intent(context,EditProfileActivity::class.java))
        }

        return view
    }

    private fun myPhotos(){
        val firebase_firestore = Firebase.firestore
        val ref = firebase_firestore.collection("Posts")

        ref.get().addOnSuccessListener {result ->
            (postList as ArrayList<Post>).clear()
            for (document in result) {
                Log.d("ProfileActivity", "${document.id} => ${document.data}")
                val post = document.toObject(Post::class.java)
                if(post.publisher.equals(profileID)){
                    (postList as ArrayList<Post>).add(post)
                }
                Collections.reverse(postList)
                myImageAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun retreiveInfromation() {
        firebase_firestore.collection("Users").document(firebase_auth.currentUser!!.uid).addSnapshotListener { snapshot, e ->

            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                val user : User?= snapshot.toObject(User::class.java)
                profileBinding.usernameProfile.setText(user!!.username)
                profileBinding.fullNameTextProfile.setText(user.fullname)
                profileBinding.bioTextProfile.setText(user.bio)

                Picasso.get().load(user.photo_url).into(profileBinding.profileImageCircle)

            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    private fun checkFollowandFollowingButtons() {
        val firebase_firestore = Firebase.firestore
        val followingref = firebase_auth.currentUser?.uid.let {
                it ->
            firebase_firestore.collection("Follow").document(it.toString()).collection("Following").document(profileID)

        }

        if(followingref != null){
            followingref.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("ProfileActivity", "Listen failed.", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("ProfileActivity", "Current data: ${snapshot.data}")
                    profileBinding.buttonEditProfile.text = "Following"
                } else {
                    Log.d("ProfileActivity", "Current data: null")
                    profileBinding.buttonEditProfile.text = "Follow"
                }
            }
        }
    }

    private fun retreiveFollowers(){
        val firebase_firestore = Firebase.firestore
        val followerref = firebase_auth.currentUser?.uid.let {
                it ->
            firebase_firestore.collection("Follow").document(it.toString()).collection("Followers")

        }

        followerref.get().addOnSuccessListener {
                document ->
            if (document != null) {
                Log.d("UserAdapter", "DocumentSnapshot data: $document")

                follower_number.text = document.size().toString()
            } else {
                Log.d("UserAdapter", "No such document")
            }
        }
    }

    private fun retreiveFollowings(){
        val firebase_firestore = Firebase.firestore
        val followerref =
            firebase_firestore.collection("Follow").document(profileID).collection("Following")


        followerref.get().addOnSuccessListener {
                document ->
            if (document != null) {
                Log.d("ProfileActivity", "DocumentSnapshot data: $document")
                following_number.text = document.size().toString()
            } else {
                Log.d("ProfileActivity", "No such document")
            }
        }
    }



    override fun onPause() {
        super.onPause()
        val pref = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
        pref.putString("profileID", firebase_auth.currentUser!!.uid)
        pref.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        val pref = context!!.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit()
        pref.putString("profileID",firebase_auth.currentUser!!.uid)
        pref.apply()
    }

}