package com.inv8solutions.mindcare

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.setPadding
import com.inv8solutions.mindcare.databinding.SelfAssessmentBinding

class SelfAssessment : ComponentActivity() {

    private lateinit var binding: SelfAssessmentBinding


    private val questions = listOf(
        "How are you feeling today?",
        "How well did you sleep last night?",
        "Are you feeling stressed recently?",
        "How often did you exercise this week?",
        "Do you feel emotionally supported?"
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SelfAssessmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load initial question
        loadQuestion(currentIndex)

        // Next Button Action
        binding.btnNext.setOnClickListener {
            if (currentIndex < questions.size - 1) {
                currentIndex++
                loadQuestion(currentIndex)
            } else {
                // End reached
                binding.tvProgressCount.text = "Completed!"
                binding.btnNext.text = "Done"
                binding.btnNext.isEnabled = false
            }
        }

        // Back button to finish activity
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadQuestion(index: Int) {

        // ConstraintLayout inside your CardView
        val cardContainer =
            binding.cardQuestion.getChildAt(0) as ConstraintLayout

        // 1️⃣ Clear previous question components
        cardContainer.removeAllViews()

        // 2️⃣ Create the new question TextView
        val tv = TextView(this).apply {
            id = View.generateViewId()
            text = questions[index]
            textSize = 18f
            setPadding(10)
            setTextColor(getColor(android.R.color.black))
        }

        // 3️⃣ Add it into card
        cardContainer.addView(
            tv,
            ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        // 4️⃣ Apply constraints so it stays top & centered
        ConstraintSet().apply {
            clone(cardContainer)
            connect(tv.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(tv.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(tv.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            applyTo(cardContainer)
        }

        // 5️⃣ Update progress bar and count text
        val progress = (((index + 1).toFloat() / questions.size) * 100).toInt()
        binding.progressBar.progress = progress
        binding.tvProgressCount.text = "Question ${index + 1} of ${questions.size}"
    }
}
