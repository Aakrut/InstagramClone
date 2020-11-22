package com.ex.instagramclone.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ex.instagramclone.EditProfileActivity
import com.ex.instagramclone.R
import com.ex.instagramclone.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var profileBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        profileBinding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = profileBinding.root

        profileBinding.buttonEditProfile.setOnClickListener {
            startActivity(Intent(context,EditProfileActivity::class.java))
        }

        return view
    }

}