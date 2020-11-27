package com.ex.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ex.instagramclone.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.FirebaseAuthKtxRegistrar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.sign

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebase_auth : FirebaseAuth
    private val TAG = "SignUpActivity"

    private lateinit var signUpBinding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = signUpBinding.root
        setContentView(view)


        signUpBinding.buttonSignupA.setOnClickListener {
            signup()
        }
    }

    private fun signup() {
        if(signUpBinding.emailSignup.text.toString() == ""){
            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show()
        }else if(signUpBinding.passwordSignup.text.toString() == ""){
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show()
        }else if(signUpBinding.emailSignup.text.toString() == "" || signUpBinding.passwordSignup.text.toString() == ""){
            Toast.makeText(this, "Please Enter All Fields", Toast.LENGTH_SHORT).show()
        }else{
            val email : String = signUpBinding.emailSignup.text.toString()
            val password : String = signUpBinding.passwordSignup.text.toString()
            firebase_auth = Firebase.auth
            firebase_auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "SuccessFully Created New User", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "signup: SuccessFully Done")
                    val intent = Intent()
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(Intent(this,InformationActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Some thing Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }
}