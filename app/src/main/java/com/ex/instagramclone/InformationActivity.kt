package com.ex.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ex.instagramclone.databinding.ActivityInformationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InformationActivity : AppCompatActivity() {

    private lateinit var firebase_auth : FirebaseAuth


    private lateinit var informationBinding: ActivityInformationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        informationBinding = ActivityInformationBinding.inflate(layoutInflater)
        val view = informationBinding.root
        setContentView(view)

        firebase_auth = Firebase.auth


        informationBinding.buttonSignup.setOnClickListener {
            signup()
        }
    }

    private fun signup() {
        val username : String = informationBinding.usernameSignup.text.toString()
        val fullname : String = informationBinding.fullnameSignup.text.toString()

        if(username == ""){
            Toast.makeText(this, "Please Enter The Username", Toast.LENGTH_SHORT).show()
        }else if(fullname == ""){
            Toast.makeText(this, "Please Enter The FullName", Toast.LENGTH_SHORT).show()
        }else if(username == "" || fullname == ""){
            Toast.makeText(this, "Please Fill All The Fields", Toast.LENGTH_SHORT).show()
        }else{
            val uid = firebase_auth.currentUser?.uid
            val hashamp_info = hashMapOf(
                "username" to username,
                "fullname" to fullname,
                "photo_url" to "https://firebasestorage.googleapis.com/v0/b/social-networks-clone2.appspot.com/o/image.png?alt=media&token=efa0c406-3d7a-4b39-9ccc-a9216ae9752e",
                "bio" to "Instagram-Clone",
                "uid" to uid
            )

           val db = Firebase.firestore
           db.collection("Users").document(uid!!).set(hashamp_info).addOnSuccessListener {
               Toast.makeText(this, "SuccessFully Signed Up", Toast.LENGTH_SHORT).show()
               val intent = Intent()
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
               startActivity(Intent(this,MainActivity::class.java))
               finish()
           }.addOnFailureListener {
               Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
           }
        }
    }
}