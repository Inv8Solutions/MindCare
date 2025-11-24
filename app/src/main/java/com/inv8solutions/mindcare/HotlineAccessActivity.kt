package com.inv8solutions.mindcare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity

class HotlineAccessActivity : ComponentActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnCall911: Button
    private lateinit var btnCrisis: Button
    private lateinit var btnStudent: Button
    private lateinit var btnTextLine: Button
    private lateinit var btnAnxiety: Button
    private lateinit var btnSpecialNeeds: Button
    private lateinit var btnCampus: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hotline_access)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        btnCall911 = findViewById(R.id.btnCall911)
        btnCrisis = findViewById(R.id.btnCrisis)
        btnStudent = findViewById(R.id.btnStudent)
        btnTextLine = findViewById(R.id.btnTextLine)
        btnAnxiety = findViewById(R.id.btnAnxiety)
        btnSpecialNeeds = findViewById(R.id.btnSpecialNeeds)
        btnCampus = findViewById(R.id.btnCampus)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnCall911.setOnClickListener { makeCall("911") }
        btnCrisis.setOnClickListener { makeCall("988") }
        btnStudent.setOnClickListener { makeCall("18002738255") }
        btnTextLine.setOnClickListener { sendSMS("741741", "HOME") }
        btnAnxiety.setOnClickListener { makeCall("18009506264") }
        btnSpecialNeeds.setOnClickListener { makeCall("18002345678") }
        btnCampus.setOnClickListener { makeCall("18005550123") }
    }

    private fun makeCall(phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to make call", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("sms:$phoneNumber")
            intent.putExtra("sms_body", message)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to send SMS", Toast.LENGTH_SHORT).show()
        }
    }
}
