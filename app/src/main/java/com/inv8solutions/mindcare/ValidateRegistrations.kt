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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.inv8solutions.mindcare.models.User

class ValidateRegistrations : ComponentActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyView: TextView
    private val pendingUsers = mutableListOf<User>()
    private lateinit var adapter: PendingUsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.validate_registrations)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerViewPendingUsers)
        progressBar = findViewById(R.id.progressBar)
        emptyView = findViewById(R.id.tvEmptyView)

        adapter = PendingUsersAdapter(pendingUsers) { user, approved ->
            handleUserValidation(user, approved)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadPendingUsers()
    }

    private fun loadPendingUsers() {
        progressBar.visibility = View.VISIBLE
        emptyView.visibility = View.GONE

        firestore.collection("users")
            .whereEqualTo("isApproved", false)
            .get()
            .addOnSuccessListener { documents ->
                pendingUsers.clear()
                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    pendingUsers.add(user)
                }
                adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE

                if (pendingUsers.isEmpty()) {
                    emptyView.visibility = View.VISIBLE
                    emptyView.text = "No pending registrations"
                }
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Error loading users: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun handleUserValidation(user: User, approved: Boolean) {
        val currentAdmin = auth.currentUser ?: return

        val updates = hashMapOf<String, Any>(
            "isApproved" to approved,
            "approvedBy" to currentAdmin.uid,
            "approvedDate" to System.currentTimeMillis()
        )

        firestore.collection("users")
            .document(user.uid)
            .update(updates)
            .addOnSuccessListener {
                val message = if (approved) "User approved" else "User rejected"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                loadPendingUsers() // Refresh list
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    inner class PendingUsersAdapter(
        private val users: List<User>,
        private val onAction: (User, Boolean) -> Unit
    ) : RecyclerView.Adapter<PendingUsersAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvUserName)
            val tvEmail: TextView = view.findViewById(R.id.tvUserEmail)
            val tvDate: TextView = view.findViewById(R.id.tvRegistrationDate)
            val btnApprove: Button = view.findViewById(R.id.btnApprove)
            val btnReject: Button = view.findViewById(R.id.btnReject)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pending_user, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val user = users[position]
            holder.tvName.text = user.fullName
            holder.tvEmail.text = user.email
            holder.tvDate.text = "Registered: ${java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date(user.registrationDate))}"

            holder.btnApprove.setOnClickListener {
                onAction(user, true)
            }

            holder.btnReject.setOnClickListener {
                onAction(user, false)
            }
        }

        override fun getItemCount() = users.size
    }
}

