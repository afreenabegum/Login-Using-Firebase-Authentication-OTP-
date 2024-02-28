package com.example.loginpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.system.exitProcess

class SignIn_Page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_page)

        val phoneNumberReceived = intent.getStringExtra(MainActivity.KEY)
        val showPhoneNum = findViewById<TextView>(R.id.tvPhoneNumber)
        showPhoneNum.text = "Hello $phoneNumberReceived"

        val logout = findViewById<Button>(R.id.btnLogout)

        logout.setOnClickListener {
           finish() // exit from the current Page
        }
    }
}