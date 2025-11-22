package com.inv8solutions.mindcare

import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.activity.ComponentActivity

class DailyReminders : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_reminders)

        // Switch references
        val switchMorning = findViewById<Switch>(R.id.switchBreakTime)
        val switchAfternoon = findViewById<Switch>(R.id.switchHydration)
        val switchEvening = findViewById<Switch>(R.id.switchSleep)
        val switchNight = findViewById<Switch>(R.id.switchTask)

        // Morning Switch Logic
        switchMorning.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Morning reminders enabled", Toast.LENGTH_SHORT).show()
                // TODO: Add your morning notification logic here
            } else {
                Toast.makeText(this, "Morning reminders disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Afternoon Switch Logic
        switchAfternoon.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Afternoon reminders enabled", Toast.LENGTH_SHORT).show()
                // TODO: Add your afternoon notification logic here
            } else {
                Toast.makeText(this, "Afternoon reminders disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Evening Switch Logic
        switchEvening.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Evening reminders enabled", Toast.LENGTH_SHORT).show()
                // TODO: Add your evening notification logic here
            } else {
                Toast.makeText(this, "Evening reminders disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Night Switch Logic (NEW)
        switchNight.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Night reminders enabled", Toast.LENGTH_SHORT).show()
                // TODO: Add your night notification logic here
            } else {
                Toast.makeText(this, "Night reminders disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
