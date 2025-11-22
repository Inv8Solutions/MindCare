package com.inv8solutions.mindcare

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewJournalEntryActivity : AppCompatActivity() {

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
        btnBack.setOnClickListener {
            finish()
        }
        
        layoutPersonal.setOnClickListener {
            selectCategory("Personal")
        }
        
        layoutAcademic.setOnClickListener {
            selectCategory("Academic")
        }
        
        layoutWellness.setOnClickListener {
            selectCategory("Wellness")
        }
        
        layoutSocial.setOnClickListener {
            selectCategory("Social")
        }
        
        layoutGoals.setOnClickListener {
            selectCategory("Goals")
        }
        
        layoutGratitude.setOnClickListener {
            selectCategory("Gratitude")
        }
        
        btnSaveEntry.setOnClickListener {
            saveJournalEntry()
        }
    }

    private fun selectCategory(category: String) {
        selectedCategory = category
        
        // Reset all category layouts alpha
        layoutPersonal.alpha = 0.5f
        layoutAcademic.alpha = 0.5f
        layoutWellness.alpha = 0.5f
        layoutSocial.alpha = 0.5f
        layoutGoals.alpha = 0.5f
        layoutGratitude.alpha = 0.5f
        
        // Highlight selected category
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
        
        // TODO: Save to database or Firebase
        Toast.makeText(this, "Journal entry saved!", Toast.LENGTH_SHORT).show()
        finish()
    }
}

