package com.ex.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ex.instagramclone.databinding.ActivityMainBinding
import com.ex.instagramclone.fragments.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        loadFragment(HomeFragment())

        bottomNav()

    }

    private fun bottomNav() {
        mainBinding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.search -> {
                    loadFragment(SearchFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.add_post -> {
                    loadFragment(AddPostFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.notification -> {
                    loadFragment(NotificationFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                else -> return@setOnNavigationItemSelectedListener false
            }

        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}