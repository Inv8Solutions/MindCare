package com.inv8solutions.mindcare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.setPadding
import com.inv8solutions.mindcare.databinding.SelfAssessmentBinding
import kotlin.inc
import kotlin.text.compareTo

class SelfAssessment : ComponentActivity() {

    private lateinit var binding: SelfAssessmentBinding

    // 1. The Questions list
    private val questions = listOf(
        "Are you currently living away from home?",
        "Are you a scholar?",
        "Are you a student athlete?",
        "Are you a young parent?",
        "Are you a working student?",
        "Are you currently experiencing grief or bereavement?",
        "Do you require any special facilities or services to facilitate your studies?",
        "Do you suffer from any clinical diseases which require regular attention?",
        "Do you have problems with your vision?",
        "Do you have problems with your hearing?",
        "Are you currently receiving psychological services, professional counseling, or other mental health services?",
        "Have you been prescribed psychiatric prescription medication in the past?",
        "Are you currently taking any psychiatric prescription medication?",
        "Have you wished you were dead or wished you could go to sleep and not wake up?",
        "Have you actually had any thoughts of killing yourself?",
        "Have you been thinking about how you might do this?",
        "Have you had these thoughts and had some intention of acting on them?",
        "Have you started to work out or worked out the details of how to kill yourself?",
        "Did you intend to carry out this plan?",
        "Have you done anything, started to do anything, or prepared to do anything to end your life?"
    )

    // 2. Storage for answers (Index -> "Yes" or "No")
    private val answers = mutableMapOf<Int, String>()

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SelfAssessmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadQuestion(currentIndex)

        binding.btnNext.setOnClickListener {
            // Save currently selected answer before moving on
            if (saveCurrentAnswer()) {
                if (currentIndex < questions.size - 1) {
                    currentIndex++
                    loadQuestion(currentIndex)
                } else {
                    binding.tvProgressCount.text = "Completed!"
                    binding.btnNext.text = "Done"
                    binding.btnNext.isEnabled = true  // Keep enabled so user can click it

                    // Set new click listener for Done button
                    binding.btnNext.setOnClickListener {
                        // Navigate back to Dashboard
                        val intent = Intent(this, Dashboard::class.java)
                        startActivity(intent)
                        finish()
                    }

                    // Optional: Print results to Logcat or Toast
                    Toast.makeText(this, "Assessment Saved", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                loadQuestion(currentIndex)
            } else {
                finish()
            }
        }
    }

    /**
     * Finds the RadioGroup in the current view and saves the selection to the map.
     * Returns true if an answer was selected, false otherwise.
     */
    private fun saveCurrentAnswer(): Boolean {
        val cardContainer = binding.cardQuestion.getChildAt(0) as ConstraintLayout

        // Find the RadioGroup by tag or type iteration
        for (i in 0 until cardContainer.childCount) {
            val child = cardContainer.getChildAt(i)
            if (child is RadioGroup) {
                val selectedId = child.checkedRadioButtonId
                if (selectedId != -1) {
                    val radioButton = child.findViewById<RadioButton>(selectedId)
                    answers[currentIndex] = radioButton.text.toString()
                    return true
                }
            }
        }
        return false
    }

    private fun loadQuestion(index: Int) {
        val cardContainer = binding.cardQuestion.getChildAt(0) as ConstraintLayout
        cardContainer.removeAllViews()

        // --- 1. Create Question Text ---
        val tvQuestion = TextView(this).apply {
            id = View.generateViewId()
            text = questions[index]
            textSize = 18f
            setPadding(20)
            setTextColor(getColor(android.R.color.black))
        }

        // --- 2. Create RadioGroup (The Buttons) ---
        val radioGroup = RadioGroup(this).apply {
            id = View.generateViewId()
            orientation = RadioGroup.VERTICAL
            setPadding(20, 20, 20, 20)
        }

        val rbYes = RadioButton(this).apply { text = "Yes" }
        val rbNo = RadioButton(this).apply { text = "No" }

        radioGroup.addView(rbYes)
        radioGroup.addView(rbNo)

        // Restore previous answer if it exists
        if (answers.containsKey(index)) {
            if (answers[index] == "Yes") rbYes.isChecked = true
            if (answers[index] == "No") rbNo.isChecked = true
        }

        // --- 3. Add Views to Layout ---
        cardContainer.addView(tvQuestion, ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        cardContainer.addView(radioGroup, ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ))

        // --- 4. Apply Constraints ---
        ConstraintSet().apply {
            clone(cardContainer)

            // Pin Question to Top
            connect(tvQuestion.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(tvQuestion.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(tvQuestion.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

            // Pin RadioGroup below Question
            connect(radioGroup.id, ConstraintSet.TOP, tvQuestion.id, ConstraintSet.BOTTOM)
            connect(radioGroup.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(radioGroup.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

            applyTo(cardContainer)
        }

        // --- 5. Update Progress ---
        val progress = (((index + 1).toFloat() / questions.size) * 100).toInt()
        binding.progressBar.progress = progress
        binding.tvProgressCount.text = "Question ${index + 1} of ${questions.size}"

        // Reset button text if we moved back from "Done"
        if (currentIndex < questions.size -1) {
            binding.btnNext.text = "Next"
            binding.btnNext.isEnabled = true
        }
    }
}