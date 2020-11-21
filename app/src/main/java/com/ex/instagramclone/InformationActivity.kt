package com.ex.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ex.instagramclone.databinding.ActivityInformationBinding

class InformationActivity : AppCompatActivity() {

    private lateinit var informationBinding: ActivityInformationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        informationBinding = ActivityInformationBinding.inflate(layoutInflater)
        val view = informationBinding.root
        setContentView(view)
    }
}