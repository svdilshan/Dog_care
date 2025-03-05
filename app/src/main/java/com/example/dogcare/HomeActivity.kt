package com.example.dogcare

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Check if the user is logged in
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // If no user is logged in, redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Close HomeActivity so the user can't go back to it
        }

        // Set up the Bottom Navigation
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set the default Fragment to show
        loadFragment(HomeFragment())

        // Handle BottomNavigationItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment()) // Load Home Fragment
                    true
                }
                R.id.nav_category -> {
                    loadFragment(CategoryFragment()) // Load Category Fragment
                    true
                }
                R.id.nav_blog -> {
                    loadFragment(BlogFragment()) // Load Blog Fragment
                    true
                }
                R.id.nav_cart -> {
                    loadFragment(CartFragment()) // Load Cart Fragment
                    true
                }
                else -> false
            }
        }

        // Handle profile image click to go to the Edit Profile page
        val profileImage: ImageView = findViewById(R.id.profileImage)
        profileImage.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }
}
