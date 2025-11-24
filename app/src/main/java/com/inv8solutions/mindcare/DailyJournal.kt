package com.inv8solutions.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DailyJournal : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JournalAdapter

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_journals)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnAddJournal = findViewById<Button>(R.id.btnAddJournal)
        recyclerView = findViewById(R.id.recyclerViewJournals)

        adapter = JournalAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnBack.setOnClickListener { finish() }

        btnAddJournal.setOnClickListener {
            startActivity(Intent(this, NewJournalEntryActivity::class.java))
        }

        fetchJournalEntries()
    }

    private fun fetchJournalEntries() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("users")
            .document(user.uid)
            .collection("journals")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                val journalList = result.documents.map { doc ->
                    doc.toObject(JournalEntry::class.java)!!
                }
                adapter.updateData(journalList)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load journals", Toast.LENGTH_SHORT).show()
            }
    }
}
