package com.inv8solutions.mindcare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.inv8solutions.mindcare.models.QuestionnaireResponse

class ViewQuestionnaires : ComponentActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: TextView
    private val questionnaires = mutableListOf<QuestionnaireResponse>()
    private lateinit var adapter: QuestionnairesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_questionnaires)

        firestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.recyclerViewQuestionnaires)
        progressBar = findViewById(R.id.progressBar)
        emptyView = findViewById(R.id.tvEmptyView)

        adapter = QuestionnairesAdapter(questionnaires) { questionnaire ->
            showQuestionnaireDetails(questionnaire)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadQuestionnaires()
    }

    private fun loadQuestionnaires() {
        progressBar.visibility = View.VISIBLE
        emptyView.visibility = View.GONE

        firestore.collection("questionnaires")
            .orderBy("submittedDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                questionnaires.clear()
                for (document in documents) {
                    val questionnaire = document.toObject(QuestionnaireResponse::class.java)
                    questionnaires.add(questionnaire)
                }
                adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE

                if (questionnaires.isEmpty()) {
                    emptyView.visibility = View.VISIBLE
                    emptyView.text = "No questionnaire responses found"
                }
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Error loading questionnaires: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showQuestionnaireDetails(questionnaire: QuestionnaireResponse) {
        val dialog = android.app.AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_questionnaire_details, null)

        val tvUserName = dialogView.findViewById<TextView>(R.id.tvUserName)
        val tvUserEmail = dialogView.findViewById<TextView>(R.id.tvUserEmail)
        val tvRiskLevel = dialogView.findViewById<TextView>(R.id.tvRiskLevel)
        val tvResponses = dialogView.findViewById<TextView>(R.id.tvResponses)

        tvUserName.text = "Name: ${questionnaire.userName}"
        tvUserEmail.text = "Email: ${questionnaire.userEmail}"
        tvRiskLevel.text = "Risk Level: ${questionnaire.riskLevel}"

        val responsesText = StringBuilder()
        questionnaire.responses.forEach { (question, answer) ->
            responsesText.append("Q: $question\nA: $answer\n\n")
        }
        tvResponses.text = responsesText.toString()

        dialog.setView(dialogView)
            .setPositiveButton("Close", null)
            .show()
    }

    inner class QuestionnairesAdapter(
        private val questionnaires: List<QuestionnaireResponse>,
        private val onClick: (QuestionnaireResponse) -> Unit
    ) : RecyclerView.Adapter<QuestionnairesAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvUserName: TextView = view.findViewById(R.id.tvUserName)
            val tvUserEmail: TextView = view.findViewById(R.id.tvUserEmail)
            val tvDate: TextView = view.findViewById(R.id.tvSubmittedDate)
            val tvRiskLevel: TextView = view.findViewById(R.id.tvRiskLevel)
            val card: CardView = view.findViewById(R.id.cardQuestionnaire)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_questionnaire, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val questionnaire = questionnaires[position]
            holder.tvUserName.text = questionnaire.userName
            holder.tvUserEmail.text = questionnaire.userEmail
            holder.tvDate.text = "Submitted: ${java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(questionnaire.submittedDate))}"
            holder.tvRiskLevel.text = "Risk: ${questionnaire.riskLevel}"

            // Set color based on risk level
            val riskColor = when (questionnaire.riskLevel) {
                "High" -> android.graphics.Color.parseColor("#F44336")
                "Medium" -> android.graphics.Color.parseColor("#FF9800")
                else -> android.graphics.Color.parseColor("#4CAF50")
            }
            holder.tvRiskLevel.setTextColor(riskColor)

            holder.card.setOnClickListener {
                onClick(questionnaire)
            }
        }

        override fun getItemCount() = questionnaires.size
    }
}

