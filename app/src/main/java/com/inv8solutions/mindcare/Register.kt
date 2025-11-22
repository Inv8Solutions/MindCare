package com.inv8solutions.mindcare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var progressBar: ProgressBar
    private lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        // Firebase instances
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Find views
        val nameInput = findViewById<EditText>(R.id.etName)
        val emailInput = findViewById<EditText>(R.id.etEmail)
        val passwordInput = findViewById<EditText>(R.id.etPass)
        val confirmPasswordInput = findViewById<EditText>(R.id.etConfirm)
        registerBtn = findViewById(R.id.btnCreateAccount)
        progressBar = findViewById(R.id.progressBar)

        // Click listener
        registerBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            val errorMessage = when {
                name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ->
                    "Please fill all fields"
                password.length < 6 ->
                    "Password must be at least 6 characters"
                password != confirmPassword ->
                    "Passwords do not match"
                else -> null
            }

            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            } else {
                registerUser(name, email, password)
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        // Show loader and disable button
        progressBar.visibility = View.VISIBLE
        registerBtn.isEnabled = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { authTask ->
                if (authTask.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val uid = currentUser.uid
                        val userMap = mapOf(
                            "uid" to uid,
                            "fullName" to name,
                            "email" to email
                        )

                        firestore.collection("users")
                            .document(uid)
                            .set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Registered successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this, Login::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Firestore error: ${e.localizedMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBar.visibility = View.GONE
                                registerBtn.isEnabled = true
                            }
                    } else {
                        Toast.makeText(this, "Registration failed: User is null", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                        registerBtn.isEnabled = true
                    }
                } else {
                    Toast.makeText(
                        this,
                        authTask.exception?.localizedMessage ?: "Registration failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                    registerBtn.isEnabled = true
                }
            }
    }
}
