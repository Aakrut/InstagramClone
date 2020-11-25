package com.ex.instagramclone

import android.R.attr
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ex.instagramclone.databinding.ActivityEditProfileBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class EditProfileActivity : AppCompatActivity() {

    private val TAG = "EditProfileActivity"

    private lateinit var editProfileBinding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = editProfileBinding.root
        setContentView(view)

        editProfileBinding.circleImageViewProfileEdit.setOnClickListener {
            Log.d(TAG, "onCreateView: Post button Clicked")

            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
        }

        editProfileBinding.cancelEditProfile.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === RESULT_OK) {
                val resultUri: Uri = result.uri
                editProfileBinding.circleImageViewProfileEdit.setImageURI(resultUri)
                Log.d(TAG, "onActivityResult: Image Selected SuccessFully")
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}