package com.ex.instagramclone.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ex.instagramclone.R
import com.ex.instagramclone.SearchActivity
import com.ex.instagramclone.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var searchBinding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        searchBinding = FragmentSearchBinding.inflate(inflater,container,false)
        val view = searchBinding.root

        searchBinding.searchImage.setOnClickListener {
            startActivity(Intent(context,SearchActivity::class.java))
        }
        return view
    }

}