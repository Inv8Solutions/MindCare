package com.inv8solutions.mindcare

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NewJournalEntryActivity : ComponentActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnSaveEntry: Button
    private lateinit var etEntryTitle: EditText
    private lateinit var etYourThoughts: EditText

    // Category layouts
    private lateinit var layoutPersonal: LinearLayout
    private lateinit var layoutAcademic: LinearLayout
    private lateinit var layoutWellness: LinearLayout
    private lateinit var layoutSocial: LinearLayout
    private lateinit var layoutGoals: LinearLayout
    private lateinit var layoutGratitude: LinearLayout

    private var selectedCategory: String = ""

    // Firebase instances
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_journal_entry)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        btnSaveEntry = findViewById(R.id.btnSaveEntry)
        etEntryTitle = findViewById(R.id.etEntryTitle)
        etYourThoughts = findViewById(R.id.etYourThoughts)

        layoutPersonal = findViewById(R.id.layoutPersonal)
        layoutAcademic = findViewById(R.id.layoutAcademic)
        layoutWellness = findViewById(R.id.layoutWellness)
        layoutSocial = findViewById(R.id.layoutSocial)
        layoutGoals = findViewById(R.id.layoutGoals)
        layoutGratitude = findViewById(R.id.layoutGratitude)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener { finish() }

        layoutPersonal.setOnClickListener { selectCategory("Personal") }
        layoutAcademic.setOnClickListener { selectCategory("Academic") }
        layoutWellness.setOnClickListener { selectCategory("Wellness") }
        layoutSocial.setOnClickListener { selectCategory("Social") }
        layoutGoals.setOnClickListener { selectCategory("Goals") }
        layoutGratitude.setOnClickListener { selectCategory("Gratitude") }

        btnSaveEntry.setOnClickListener { saveJournalEntry() }
    }

    private fun selectCategory(category: String) {
        selectedCategory = category

        // Reset alpha for all categories
        layoutPersonal.alpha = 0.5f
        layoutAcademic.alpha = 0.5f
        layoutWellness.alpha = 0.5f
        layoutSocial.alpha = 0.5f
        layoutGoals.alpha = 0.5f
        layoutGratitude.alpha = 0.5f

        // Highlight selected
        when (category) {
            "Personal" -> layoutPersonal.alpha = 1.0f
            "Academic" -> layoutAcademic.alpha = 1.0f
            "Wellness" -> layoutWellness.alpha = 1.0f
            "Social" -> layoutSocial.alpha = 1.0f
            "Goals" -> layoutGoals.alpha = 1.0f
            "Gratitude" -> layoutGratitude.alpha = 1.0f
        }

        Toast.makeText(this, "$category selected", Toast.LENGTH_SHORT).show()
    }

    private fun saveJournalEntry() {
        val title = etEntryTitle.text.toString().trim()
        val thoughts = etYourThoughts.text.toString().trim()

        if (selectedCategory.isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        if (thoughts.isEmpty()) {
            Toast.makeText(this, "Please write your thoughts", Toast.LENGTH_SHORT).show()
            return
        }

        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare data
        val journalData = hashMapOf(
            "title" to title,
            "thoughts" to thoughts,
            "category" to selectedCategory,
            "timestamp" to System.currentTimeMillis()
        )

        // Save to Firestore under users/{uid}/journals
        firestore.collection("users")
            .document(user.uid)
            .collection("journals")
            .add(journalData)
            .addOnSuccessListener {
                Toast.makeText(this, "Journal entry saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save journal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
