package com.inv8solutions.mindcare

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.inv8solutions.mindcare.R


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load your XML layout
        setContentView(R.layout.activity_main)

        val loginBtn = findViewById<Button>(R.id.btnLogin)


        // Add click listener
        loginBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        val registerBtn = findViewById<Button>(R.id.btnRegister)

        // Add click listener
        registerBtn.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
