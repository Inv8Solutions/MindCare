package com.inv8solutions.mindcare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.inv8solutions.mindcare.models.User

class ListLSNUsers : ComponentActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: TextView
    private val lsnUsers = mutableListOf<User>()
    private lateinit var adapter: LSNUsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_lsn_users)

        firestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.recyclerViewLSNUsers)
        progressBar = findViewById(R.id.progressBar)
        emptyView = findViewById(R.id.tvEmptyView)

        adapter = LSNUsersAdapter(lsnUsers) { user ->
            toggleLSNStatus(user)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadLSNUsers()
    }

    private fun loadLSNUsers() {
        progressBar.visibility = View.VISIBLE
        emptyView.visibility = View.GONE

        // Load all approved users to show LSN status
        firestore.collection("users")
            .whereEqualTo("isApproved", true)
            .get()
            .addOnSuccessListener { documents ->
                lsnUsers.clear()
                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    lsnUsers.add(user)
                }
                // Sort to show LSN users first
                lsnUsers.sortByDescending { it.isLSN }
                adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE

                if (lsnUsers.isEmpty()) {
                    emptyView.visibility = View.VISIBLE
                    emptyView.text = "No approved users found"
                }
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Error loading users: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun toggleLSNStatus(user: User) {
        val newStatus = !user.isLSN
        firestore.collection("users")
            .document(user.uid)
            .update("isLSN", newStatus)
            .addOnSuccessListener {
                val message = if (newStatus) "User promoted to LSN" else "LSN status removed"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                loadLSNUsers() // Refresh list
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    inner class LSNUsersAdapter(
        private val users: List<User>,
        private val onToggleLSN: (User) -> Unit
    ) : RecyclerView.Adapter<LSNUsersAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvUserName)
            val tvEmail: TextView = view.findViewById(R.id.tvUserEmail)
            val tvLSNStatus: TextView = view.findViewById(R.id.tvLSNStatus)
            val btnToggleLSN: Button = view.findViewById(R.id.btnToggleLSN)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_lsn_user, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val user = users[position]
            holder.tvName.text = user.fullName
            holder.tvEmail.text = user.email

            if (user.isLSN) {
                holder.tvLSNStatus.text = "✓ LSN"
                holder.tvLSNStatus.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
                holder.btnToggleLSN.text = "Remove LSN"
                holder.btnToggleLSN.setBackgroundColor(android.graphics.Color.parseColor("#F44336"))
            } else {
                holder.tvLSNStatus.text = "Regular User"
                holder.tvLSNStatus.setTextColor(android.graphics.Color.parseColor("#757575"))
                holder.btnToggleLSN.text = "Make LSN"
                holder.btnToggleLSN.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"))
            }

            holder.btnToggleLSN.setOnClickListener {
                onToggleLSN(user)
            }
        }

        override fun getItemCount() = users.size
    }
}

