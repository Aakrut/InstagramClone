package com.ex.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.ex.instagramclone.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var searchBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        val view = searchBinding.root
        setContentView(view)

        searchBinding.searchEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                searchBinding.cancelSearch.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               if(s.toString() != null){
                   searchBinding.cancelSearch.visibility = View.VISIBLE
               }else{
                   searchBinding.cancelSearch.visibility = View.GONE
               }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        searchBinding.searchEditText.text.clear()

        searchBinding.cancelSearch.setOnClickListener {
            searchBinding.searchEditText.text.clear()
        }

    }
}