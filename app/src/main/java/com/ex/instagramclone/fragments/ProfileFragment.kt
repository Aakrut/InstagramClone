package com.ex.instagramclone.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ex.instagramclone.EditProfileActivity
import com.ex.instagramclone.R
import com.ex.instagramclone.databinding.FragmentProfileBinding
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

}