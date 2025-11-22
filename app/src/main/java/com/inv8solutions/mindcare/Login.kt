package com.inv8solutions.mindcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.inv8solutions.mindcare.R

class Login : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load your login.xml layout
        setContentView(R.layout.login)
    }
}
