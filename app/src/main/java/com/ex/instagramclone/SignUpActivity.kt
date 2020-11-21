package com.ex.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ex.instagramclone.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpBinding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = signUpBinding.root

        setContentView(view)
    }
}