package com.ex.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ex.instagramclone.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {

    private lateinit var firebase_auth : FirebaseAuth
    private val TAG = "LogInActivity"

    private lateinit var logInBinding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logInBinding = ActivityLogInBinding.inflate(layoutInflater)
        val view = logInBinding.root
        setContentView(view)

        firebase_auth = Firebase.auth

        logInBinding.buttonLogin.setOnClickListener {
            login()
        }

        logInBinding.textSignup.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
    }

    private fun login() {
        val email : String = logInBinding.emailLogin.text.toString()
        val password : String = logInBinding.passwordLogin.text.toString()

        if(email == ""){
            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show()
        }else if(password == ""){
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show()
        }else if(email == "" || password == ""){
            Toast.makeText(this, "Please Enter All The Fields", Toast.LENGTH_SHORT).show()
        }else{

            firebase_auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    Log.d(TAG, "login: SuccessFully Logged In")
                    val intent = Intent()
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}