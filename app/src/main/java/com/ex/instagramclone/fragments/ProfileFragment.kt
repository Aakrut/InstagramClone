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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.instagramclone.EditProfileActivity
import com.ex.instagramclone.R
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


class ProfileFragment : Fragment() {

    private val TAG = "ProfileFragment"

    private lateinit var firebase_auth : FirebaseAuth

    private lateinit var firebase_firestorage : FirebaseStorage

    private lateinit var firebase_firestore : FirebaseFirestore

    private lateinit var profileBinding: FragmentProfileBinding

    private lateinit var profileID : String

    var postList : List<Post> ?= null

    var myImageAdapter : MyImagesAdapter ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        profileBinding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = profileBinding.root

        firebase_auth = Firebase.auth

        firebase_firestore = Firebase.firestore

        retreiveInfromation()

        val pref = context!!.getSharedPreferences("PREFS",Context.MODE_PRIVATE)
        if(pref != null){
            context.profileId = pref.getString("profileID","none").toString()
        }


        profileBinding.recyclerViewProfile.setHasFixedSize(true)
        profileBinding.recyclerViewProfile.layoutManager = LinearLayoutManager(context)



        postList = ArrayList()
        myImageAdapter = MyImagesAdapter(this, postList as ArrayList<Post>)


        recyclerview.adapter = myImageAdapter


        if(profileID == firebase_user!!.uid){
            button_edit_your_profile.text = "Edit Your Profile"
        }else if(profileID != firebase_user!!.uid){
            checkFollowandFollowingButtons()
        }


        val edit_profile : Button = findViewById(R.id.button_edit_your_profile)
        edit_profile.setOnClickListener {
            val choosebutton = edit_profile.text.toString()

            when{
                choosebutton == "Edit Your Profile" ->  {
                    val intent = Intent(this,ProfileSettingsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
                }

                choosebutton == "Follow" -> {
                    val firebase_firestore = Firebase.firestore

                    firebase_user?.uid.let {
                            it ->
                        val firebase_firestore = Firebase.firestore
                        val reff = firebase_firestore.collection("Follow").document(it.toString())

                        val hashmapref = hashMapOf(
                            profileID to true
                        )

                        reff.collection("Following").document(profileID).set(hashmapref)

                    }

                    firebase_user?.uid.let {
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

                    firebase_user?.uid.let {
                            it ->
                        val firebase_firestore = Firebase.firestore
                        val reff = firebase_firestore.collection("Follow").document(it.toString()).
                        collection("Following").document(profileID).delete()

                    }

                    firebase_user?.uid.let {
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