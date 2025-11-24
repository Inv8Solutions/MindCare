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

        // Breaktime Logic
        switchMorning.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Breaks every hour is enabled", Toast.LENGTH_SHORT).show()
                // TODO: Add your breaktime logic here
            } else {
                Toast.makeText(this, "Breaks every hour is disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Hydration Logic
        switchAfternoon.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Hydration notification every hour is enabled", Toast.LENGTH_SHORT).show()
                // TODO: Add your afternoon notification logic here
            } else {
                Toast.makeText(this, "Hydration notification every hour is disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Sleep Schedule Logic
        switchEvening.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Evening Sleep Schedule reminders enabled", Toast.LENGTH_SHORT).show()
                // TODO: Add your evening notification logic here
            } else {
                Toast.makeText(this, "Evening Sleep Schedule reminders disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Task Submission Logic
        switchNight.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Daily Check-in Enabled", Toast.LENGTH_SHORT).show()
                // TODO: Add your night notification logic here
            } else {
                Toast.makeText(this, "Daily Check-in disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
