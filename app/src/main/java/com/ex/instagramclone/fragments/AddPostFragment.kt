package com.ex.instagramclone.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ex.instagramclone.databinding.FragmentAddPostBinding
import java.net.URI


class AddPostFragment : Fragment() {

    private val TAG = "AddPostFragment"

    private lateinit var addPostBinding: FragmentAddPostBinding

    private val REQUEST_CODE = 253

    private  var PHOTO_URI : Uri ?= null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addPostBinding = FragmentAddPostBinding.inflate(inflater, container, false)
        val view = addPostBinding.root

        addPostBinding.imageSelectedPost.setOnClickListener {
            Log.d(TAG, "onCreateView: Post button Clicked")
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent,REQUEST_CODE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                PHOTO_URI  = data?.data
                addPostBinding.imageSelectedPost.setImageURI(PHOTO_URI)
            }else{
                Toast.makeText(context, "Not Seected", Toast.LENGTH_SHORT).show()
            }
        }
    }


}