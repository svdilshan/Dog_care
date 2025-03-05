package com.example.dogcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // Log a test event
        val bundle = Bundle()
        bundle.putString("test_param", "200")
        firebaseAnalytics.logEvent("test_event", bundle)

        // Check if the user is logged in
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // If no user is logged in, redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Close MainActivity to prevent going back to it
        }

        // Find buttons by their ID
        val loginButton: Button = findViewById(R.id.button)
        val signupButton: Button = findViewById(R.id.button2)

        // Set onClickListener for Login button
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent) // Navigate to LoginActivity
        }

        // Set onClickListener for Signup button
        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent) // Navigate to SignupActivity
        }
    }
}
