package com.inv8solutions.mindcare

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth   // <-- NEW: Firebase import

class Dashboard : ComponentActivity() {

    private lateinit var auth: FirebaseAuth   // <-- NEW: FirebaseAuth instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dashboard)

        // ==================================
        // 🔹 NEW: Initialize Firebase Auth
        // ==================================
        auth = FirebaseAuth.getInstance()

        // ==================================
        // 🔹 Back Press -> Show Logout Dialog
        // ==================================
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showLogoutDialog()
            }
        })

        findViewById<CardView>(R.id.daily_reminders).setOnClickListener {
            startActivity(Intent(this, DailyReminders::class.java))
        }

        findViewById<CardView>(R.id.daily_journal).setOnClickListener {
            startActivity(Intent(this, DailyJournal::class.java))
        }

        findViewById<CardView>(R.id.self_assessment).setOnClickListener {
            startActivity(Intent(this, SelfAssessment::class.java))
        }

        findViewById<CardView>(R.id.hotline_access).setOnClickListener {
            startActivity(Intent(this, HotlineAccessActivity::class.java))
        }
    }

    // ===================================================
    // 🔹 Updated: Firebase signOut + Redirect to Login
    // ===================================================
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->

                // ------------ NEW: Firebase logout ------------
                auth.signOut()

                // ------------ Redirect to Login ------------
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
