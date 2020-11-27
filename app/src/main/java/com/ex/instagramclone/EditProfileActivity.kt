package com.ex.instagramclone


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ex.instagramclone.databinding.ActivityEditProfileBinding
import com.ex.instagramclone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class EditProfileActivity : AppCompatActivity() {

    private val TAG = "EditProfileActivity"

    private lateinit var firebase_auth : FirebaseAuth

    private lateinit var firebase_firestorage : FirebaseStorage

    private var resultUri : Uri ?= null

    private lateinit var firebase_firestore : FirebaseFirestore

    private lateinit var editProfileBinding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = editProfileBinding.root
        setContentView(view)

        firebase_auth = Firebase.auth

        firebase_firestore = Firebase.firestore

        firebase_firestorage = Firebase.storage

        editProfileBinding.circleImageViewProfileEdit.setOnClickListener {
            Log.d(TAG, "onCreateView: Post button Clicked")

            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
        }

        retreiveInfromation()

        editProfileBinding.cancelEditProfile.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        editProfileBinding.buttonLogout.setOnClickListener {
            firebase_auth.signOut()
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(Intent(this,LogInActivity::class.java))
            finish()
        }

        editProfileBinding.buttonUpdateProfile.setOnClickListener {
            updateProfile()
        }


    }

    private fun retreiveInfromation() {
        firebase_firestore.collection("Users").document(firebase_auth.currentUser!!.uid).addSnapshotListener { snapshot, e ->
            val username : String = editProfileBinding.usernameEditProfile.text.toString()
            val fullname : String = editProfileBinding.fullnameEditProfile.text.toString()
            val bio : String = editProfileBinding.bioEditProfile.text.toString()
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                val user : User ?= snapshot.toObject(User::class.java)
                editProfileBinding.usernameEditProfile.setText(user!!.username)
                editProfileBinding.fullnameEditProfile.setText(user!!.fullname)
                editProfileBinding.bioEditProfile.setText(user!!.bio)

                Picasso.get().load(user.photo_url).into(editProfileBinding.circleImageViewProfileEdit)
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                resultUri = result.uri
                editProfileBinding.circleImageViewProfileEdit.setImageURI(resultUri)
                Log.d(TAG, "onActivityResult: Image Selected SuccessFully")
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d(TAG, "onActivityResult : $error")
            }
        }
    }

    //Firebase
    private fun updateProfile() {
        val username : String = editProfileBinding.usernameEditProfile.text.toString()
        val fullname : String = editProfileBinding.fullnameEditProfile.text.toString()
        val bio : String = editProfileBinding.bioEditProfile.text.toString()
           when {
               username == "" -> Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show()
               bio == "" -> Toast.makeText(this, "Please Enter Bio", Toast.LENGTH_SHORT).show()
               fullname == "" -> Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show()
               resultUri == null -> Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()

               else -> {
                   val ref = firebase_firestorage.reference.child("User-profiles").child(System.currentTimeMillis().toString() + ".jpg")

                   var uploadTask : StorageTask<*>
                   uploadTask = ref.putFile(resultUri!!)

                   val urlTask = uploadTask.continueWithTask { task ->
                       if (!task.isSuccessful) {
                           task.exception?.let {
                               throw it
                           }
                       }
                       ref.downloadUrl
                   }.addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           val downloadUri = task.result
                           val url = downloadUri.toString()

                           val hashmap_info = hashMapOf(
                               "bio" to bio,
                               "photo_url" to url,
                               "fullname" to fullname,
                               "username" to username
                           )

                           firebase_firestore.collection("Users").document(firebase_auth.currentUser!!.uid).update(
                               hashmap_info as Map<String, Any>
                           ).addOnSuccessListener {
                               Toast.makeText(this, "SuccessFully Updated Profile", Toast.LENGTH_SHORT).show()
                               val intent = Intent()
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                               startActivity(Intent(this,MainActivity::class.java))
                               finish()
                               Log.d(TAG, "updateProfile: Profile Updated")
                           }.addOnFailureListener{
                               Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                           }

                       } else {
                           Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                       }
                   }
               }
           }
    }

}