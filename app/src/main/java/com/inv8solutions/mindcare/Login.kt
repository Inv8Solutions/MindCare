package com.inv8solutions.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load your login.xml layout
        setContentView(R.layout.login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Find views
        val emailInput = findViewById<EditText>(R.id.etEmail)
        val passwordInput = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerBtn = findViewById<TextView>(R.id.tvRegister)
        val adminLoginBtn = findViewById<TextView>(R.id.tvAdminLogin)


        // Register button click listener
        registerBtn.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        // Admin login button click listener
        adminLoginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                loginAsAdmin(email, password)
            }
        }

        // Login button click listener
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validate input
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }
    }

    // Function to handle Firebase login
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                    // Navigate to HomeActivity
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish() // Close login screen
                } else {
                    // Login failed
                    val errorMessage = task.exception?.message ?: "Login failed."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginAsAdmin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        // Check if user is admin
                        firestore.collection("admins")
                            .document(currentUser.uid)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    Toast.makeText(this, "Admin login successful!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, AdminDashboard::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    auth.signOut()
                                    Toast.makeText(this, "Access Denied: Not an admin account", Toast.LENGTH_LONG).show()
                                }
                            }
                            .addOnFailureListener {
                                auth.signOut()
                                Toast.makeText(this, "Error checking admin privileges", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Login failed."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
