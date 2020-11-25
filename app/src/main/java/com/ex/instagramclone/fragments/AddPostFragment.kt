package com.ex.instagramclone.fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ex.instagramclone.MainActivity
import com.ex.instagramclone.databinding.FragmentAddPostBinding
import com.theartofdev.edmodo.cropper.CropImage


class AddPostFragment : Fragment() {

    private val TAG = "AddPostFragment"

    private lateinit var addPostBinding: FragmentAddPostBinding
    
    private var resultUri : Uri ?= null 

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addPostBinding = FragmentAddPostBinding.inflate(inflater, container, false)
        val view = addPostBinding.root

        addPostBinding.imageSelectedPost.setOnClickListener {
            Log.d(TAG, "onCreateView: Post button Clicked")

            context?.let { it1 ->
                CropImage.activity().setAspectRatio(16,9)
                    .start(it1, this)
            }

        }
        
        addPostBinding.postRightButton.setOnClickListener { 
            postingImage()
        }

        return view
    }

    private fun postingImage() {
        if(resultUri == null){
            Toast.makeText(context, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }else if(addPostBinding.captionImageAddPost.text.toString() == null){
            Toast.makeText(context, "Please Add caption", Toast.LENGTH_SHORT).show()
        }else{
            Log.d(TAG, "postingImage: ${addPostBinding.captionImageAddPost.text.toString()} and uri ${resultUri.toString()}")
            val intent = Intent()
            intent.flags  = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(Intent(context,MainActivity::class.java))
            activity!!.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                resultUri = result.uri
                addPostBinding.imageSelectedPost.setImageURI(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }


}