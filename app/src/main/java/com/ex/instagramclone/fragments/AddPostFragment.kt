package com.ex.instagramclone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ex.instagramclone.databinding.FragmentAddPostBinding


class AddPostFragment : Fragment() {


    private val TAG = "AddPostFragment"

    private lateinit var addPostBinding: FragmentAddPostBinding




    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addPostBinding = FragmentAddPostBinding.inflate(inflater, container, false)
        val view = addPostBinding.root



        return view
    }



}