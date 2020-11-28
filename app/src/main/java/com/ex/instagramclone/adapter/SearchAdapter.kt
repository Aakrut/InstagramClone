package com.ex.instagramclone.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ex.instagramclone.R
import com.ex.instagramclone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter(val context : Context,val mUserList : List<User>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    val TAG = "SearchAdapter"

    private  var firebase_auth : FirebaseUser? = Firebase.auth.currentUser

    inner class SearchViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            var cirlce_profile : CircleImageView ?= null
            var text_username : TextView ?= null
            var text_fullname : TextView ?= null
            var button_follow_unfo : Button ?= null

        init {
            cirlce_profile = itemView.findViewById(R.id.circle_image_search_item_list)
            text_username = itemView.findViewById(R.id.text_username_search_item_list)
            text_fullname = itemView.findViewById(R.id.text_fullname_search_item_list)
            button_follow_unfo = itemView.findViewById(R.id.button_follow)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_item_list,parent,false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val current_item = mUserList[position]

        holder.text_username?.text = current_item.username
        holder.text_fullname?.text = current_item.fullname
        Picasso.get().load(current_item.photo_url).into(holder.cirlce_profile)

        checkFollowinigStatus(current_item.uid,holder.button_follow_unfo)

        holder.button_follow_unfo?.setOnClickListener {
            if(holder.button_follow_unfo!!.text.toString() == "Follow"){
                firebase_auth?.uid.let {
                    it ->
                    val firebase_firestore = Firebase.firestore
                    val reff = firebase_firestore.collection("Follow").document(it.toString())

                    val hashmapref = hashMapOf(
                            current_item.uid to true
                    )



                    reff.collection("Following")
                            .document(current_item.uid).set(hashmapref).addOnCompleteListener {
                                task ->
                                if(task.isSuccessful){
                                    firebase_auth?.uid.let { it ->
                                        val firebase_firestore = Firebase.firestore
                                        val ref = firebase_firestore.collection("Follow")
                                                .document(current_item.uid)

                                        val hashmapfollow = hashMapOf(
                                                it.toString() to true
                                        )

                                        ref.collection("Followers")
                                                .document(it.toString()).set(hashmapfollow)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {

                                                    }
                                                }
                                    }
                                }
                            }
                }
            }else{
                firebase_auth?.uid.let {
                    it ->
                    val firebase_firestore = Firebase.firestore
                    val ref = firebase_firestore.collection("Follow").document(it.toString()).collection("Following")
                            .document(current_item.uid).delete().addOnCompleteListener {
                                task ->
                                if(task.isSuccessful){
                                    firebase_auth?.uid.let { it ->
                                        val firebase_firestore = Firebase.firestore
                                        val ref = firebase_firestore.collection("Follow")
                                                .document(current_item.uid).collection("Followers")
                                                .document(it.toString()).delete()
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {

                                                    }
                                                }
                                    }
                                }
                            }
                }
            }
        }
    }

    private fun checkFollowinigStatus(uid: String, buttonFollowUnfo: Button?) {
        val firebase_firestore = Firebase.firestore
        val followingref = firebase_auth?.uid.let {
            it ->
            firebase_firestore.collection("Follow").document(it.toString()).collection("Following").document(uid)
        }

        followingref.addSnapshotListener { snapshot, error ->

            if (error != null) {
                Log.w("UserAdapter", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("UserAdapter", "Current data: ${snapshot.data}")
                buttonFollowUnfo?.text = "Following"
            } else {
                Log.d("UserAdapter", "Current data: null")
                buttonFollowUnfo?.text = "Follow"
            }
        }
    }


    override fun getItemCount(): Int {
       return mUserList.size
    }


}