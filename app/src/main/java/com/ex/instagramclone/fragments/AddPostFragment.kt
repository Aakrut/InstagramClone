package com.ex.instagramclone.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.ex.instagramclone.databinding.FragmentAddPostBinding
import com.ex.instagramclone.util.FilePath
import com.ex.instagramclone.util.FileSearch


class AddPostFragment : Fragment() {


    private val TAG = "AddPostFragment"

    private lateinit var addPostBinding: FragmentAddPostBinding

    private lateinit var directories : ArrayList<String>



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addPostBinding = FragmentAddPostBinding.inflate(inflater, container, false)
        val view = addPostBinding.root





        return view
    }

    private fun init() {
        val filePaths = FilePath()

        val fileSearch = FileSearch()

        //check for other folders indide "/storage/emulated/0/pictures"

        //check for other folders indide "/storage/emulated/0/pictures"
        if (fileSearch.getDirectoryPaths(filePaths.PATH_ROOT_DIR) != null) {
            directories = fileSearch.getDirectoryPaths(filePaths.PICTURES)!!
        }
        directories.add(filePaths.CAMERA)

        val directoryNames: ArrayList<String> = ArrayList()
        for (i in 0 until directories.size) {
            Log.d(TAG, "init: directory: " + directories.get(i))
            val index: Int = directories.get(i).lastIndexOf("/")
            val string: String = directories.get(i).substring(index)
            directoryNames.add(string)
        }

        val adapter = ArrayAdapter(activity!!,
                android.R.layout.simple_spinner_item, directoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addPostBinding.simpleSpinnerDropdownItem.setAdapter(adapter)

        addPostBinding.simpleSpinnerDropdownItem.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                Log.d(TAG, "onItemClick: selected: " + directories.get(position))

                //setup our image grid for the directory chosen
//                setupGridView(directories.get(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }


}