package com.ex.instagramclone.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ex.instagramclone.R
import com.ex.instagramclone.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var searchBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        val view = searchBinding.root
        setContentView(view)



    }
}