package com.inv8solutions.mindcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JournalAdapter(private var journalList: List<JournalEntry>)
    : RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvScenarioTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvScenarioDescription)
        val tvCategory: TextView = itemView.findViewById(R.id.tvScenarioDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_journal_card, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val entry = journalList[position]
        holder.tvTitle.text = entry.title
        holder.tvDescription.text = entry.thoughts
        holder.tvCategory.text = entry.category
    }

    override fun getItemCount(): Int = journalList.size

    fun updateData(newList: List<JournalEntry>) {
        journalList = newList
        notifyDataSetChanged()
    }
}
