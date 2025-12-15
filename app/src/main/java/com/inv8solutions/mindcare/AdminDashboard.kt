package com.inv8solutions.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboard : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_dashboard)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Check if user is admin
        checkAdminAccess()

        // Set up card click listeners
        findViewById<CardView>(R.id.cardValidateUsers).setOnClickListener {
            startActivity(Intent(this, ValidateRegistrations::class.java))
        }

        findViewById<CardView>(R.id.cardViewQuestionnaires).setOnClickListener {
            startActivity(Intent(this, ViewQuestionnaires::class.java))
        }

        findViewById<CardView>(R.id.cardListLSN).setOnClickListener {
            startActivity(Intent(this, ListLSNUsers::class.java))
        }

        findViewById<CardView>(R.id.cardLogout).setOnClickListener {
            auth.signOut()
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun checkAdminAccess() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            redirectToLogin()
            return
        }

        firestore.collection("admins")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // User is admin, allow access
                    Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show()
                } else {
                    // User is not admin
                    Toast.makeText(this, "Access Denied: Admin privileges required", Toast.LENGTH_LONG).show()
                    redirectToLogin()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error checking admin access", Toast.LENGTH_SHORT).show()
                redirectToLogin()
            }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

