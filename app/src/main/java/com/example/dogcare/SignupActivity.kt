package com.example.dogcare

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val nameEditText: EditText = findViewById(R.id.etName)
        val emailEditText: EditText = findViewById(R.id.etEmail)
        val passwordEditText: EditText = findViewById(R.id.etPassword)
        val signUpButton: Button = findViewById(R.id.btnSignUp)

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validate inputs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else {
                // Sign up user with Firebase Authentication
                signUpUser(name, email, password)
            }
        }
    }

    private fun signUpUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Successfully created the user
                    val user = auth.currentUser

                    user?.let {
                        val uid = it.uid
                        // Save additional user data (e.g., name) to Firebase Realtime DB or Firestore
                        saveUserDataToDatabase(uid, name, email)
                    }

                    // Show success message
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                    finish()  // Close the signup activity
                } else {
                    // Handle failure
                    task.exception?.let { exception ->
                        val errorMessage = exception.message ?: "Unknown error"
                        Log.e("SignupError", "Authentication failed: $errorMessage")

                        // Specific error handling
                        if (exception is FirebaseAuthUserCollisionException) {
                            Toast.makeText(this, "This email is already in use. Please try a different one.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Authentication failed: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    private fun saveUserDataToDatabase(uid: String, name: String, email: String) {
        // Example of saving additional user data to Firebase Realtime Database
        val userData = mapOf(
            "uid" to uid,
            "name" to name,
            "email" to email
        )

        // Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().getReference("users")
        database.child(uid).setValue(userData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Data saved successfully
                    Toast.makeText(this, "User data saved", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle the error
                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
