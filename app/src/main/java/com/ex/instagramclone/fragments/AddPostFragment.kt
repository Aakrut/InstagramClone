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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.theartofdev.edmodo.cropper.CropImage
import java.sql.Time
import java.util.*


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

    private fun postingImage() {
        when{
            resultUri == null ->  Toast.makeText(context, "Please Select an Image", Toast.LENGTH_SHORT).show()
            addPostBinding.captionImageAddPost.text.toString() == null ->  Toast.makeText(context, "Please Add caption", Toast.LENGTH_SHORT).show()

            else -> {
                Log.d(TAG, "postingImage: ${addPostBinding.captionImageAddPost.text.toString()} and uri ${resultUri.toString()}")
                val image_storage = FirebaseStorage.getInstance().reference

                val file_ref = image_storage.child(System.currentTimeMillis().toString() + ".jpg")

                val uploadTask : StorageTask<*>
                uploadTask = file_ref.putFile(resultUri!!)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    file_ref.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val url = downloadUri.toString()

                        val db = Firebase.firestore
                        val ref = db.collection("Posts")
                        val post_id = ref.document().id

                        val hasmap_post = hashMapOf(
                                "post_id" to post_id,
                                "photo_caption" to addPostBinding.captionImageAddPost.text.toString(),
                                "post_image_url" to url,
                                "publisher" to Firebase.auth.currentUser?.uid,
                                "publish_time" to System.currentTimeMillis()
                        )

                        ref.document(post_id).set(hasmap_post).addOnSuccessListener {
                            Toast.makeText(context, "SuccessFully Posted", Toast.LENGTH_SHORT).show()
                            val intent = Intent()
                            intent.flags  = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(Intent(context,MainActivity::class.java))
                            activity!!.onBackPressed()
                        }.addOnFailureListener{
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}