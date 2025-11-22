package com.inv8solutions.mindcare

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView

class Dashboard : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load your dashboard.xml layout
        setContentView(R.layout.dashboard)

        var dailyReminders = findViewById<CardView>(R.id.daily_reminders)
        dailyReminders.setOnClickListener {
            val intent = Intent(this, DailyReminders::class.java)
        }
        var dailyJournal = findViewById<CardView>(R.id.daily_journal)
        dailyJournal.setOnClickListener {
            val intent = Intent(this, DailyJournal::class.java)
        }

        var selfAssessment = findViewById<CardView>(R.id.self_assessment)
        selfAssessment.setOnClickListener {
            val intent = Intent(this, SelfAssessment::class.java)
        }
        var hotlineAccess = findViewById<CardView>(R.id.hotline_access)
        hotlineAccess.setOnClickListener {
            val intent = Intent(this, HotlineAccess::class.java)
        }

    }
}