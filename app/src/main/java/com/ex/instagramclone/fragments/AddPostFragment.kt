package com.ex.instagramclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ex.instagramclone.R
import com.ex.instagramclone.databinding.FragmentAddPostBinding


class AddPostFragment : Fragment() {


    private lateinit var addPostBinding: FragmentAddPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addPostBinding = FragmentAddPostBinding.inflate(inflater,container,false)
        val view = addPostBinding.root



        return view
    }


}