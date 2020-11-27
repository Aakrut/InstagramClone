package com.ex.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.instagramclone.adapter.SearchAdapter
import com.ex.instagramclone.databinding.ActivitySearchBinding
import com.ex.instagramclone.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchActivity : AppCompatActivity() {

    val TAG = "SearchActivity"

    private lateinit var searchBinding: ActivitySearchBinding

    private var searchAdapter: SearchAdapter ?= null

    private var mUserlist : List<User> ?= null

    private lateinit var firebaseFirestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        val view = searchBinding.root
        setContentView(view)

        firebaseFirestore = Firebase.firestore

        mUserlist = ArrayList()

        searchAdapter = SearchAdapter(this,mUserlist as ArrayList<User>)

        searchBinding.searchRecyclerView.setHasFixedSize(true)
        searchBinding.searchRecyclerView.layoutManager = LinearLayoutManager(this)
        searchBinding.searchRecyclerView.adapter = searchAdapter

        searchBinding.searchEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                searchBinding.cancelSearch.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString() != null){
                    searchBinding.cancelSearch.visibility = View.VISIBLE
                    searchUsers(s.toString().toLowerCase())
                }else{
                    searchBinding.cancelSearch.visibility = View.GONE
                }
            }
        })

        searchBinding.searchEditText.text.clear()

        searchBinding.cancelSearch.setOnClickListener {
            searchBinding.searchEditText.text.clear()

        }
        searchBinding.cancelSearch.visibility = View.GONE
    }

    private fun searchUsers(input: String) {
                firebaseFirestore.collection("Users").orderBy("search").limit(3)
                        .startAt(input).endAt("$input/uf8ff")
                        .addSnapshotListener { value, e ->
                            (mUserlist as ArrayList<User>).clear()
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e)
                                return@addSnapshotListener
                            }else{
                                (mUserlist as ArrayList<User>).clear()
                                for (doc in value!!) {
                                    val user : User = doc.toObject(User::class.java)
                                    Log.d(TAG, "Current cites in CA: $user")
                                    if (user != null) {
                                        (mUserlist as ArrayList<User>).add(user)
                                    }
                                }
                                searchAdapter!!.notifyDataSetChanged()
                            }

                        }


    }
}