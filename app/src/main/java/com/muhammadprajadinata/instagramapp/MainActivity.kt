package com.muhammadprajadinata.instagramapp

import SearchFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.muhammadprajadinata.instagramapp.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav.setOnNavigationItemSelectedListener(onBottomNavListener)

        val frag = supportFragmentManager.beginTransaction()
        frag.add(R.id.fragContainer, HomeFragment())
        frag.commit()
    }

    private val onBottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener { i->
        var selectedFragment: Fragment = HomeFragment()

        when(i.itemId) {
            R.id.itemHome -> {
                selectedFragment = HomeFragment()
            }
            R.id.itemSearch -> {
                selectedFragment = SearchFragment()
            }
            R.id.itemAddPost -> {
                selectedFragment = AddPostFragment()
            }
            R.id.itemLike -> {
                selectedFragment = LikeFragment()
            }
            R.id.itemProfil -> {
                selectedFragment = ProfilFragment()
            }
        }

        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.fragContainer, selectedFragment)
        frag.commit()

        true
    }
}