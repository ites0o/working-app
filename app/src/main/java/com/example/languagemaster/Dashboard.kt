package com.example.languagemaster


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val intent = Intent(this, TranslateActivity::class.java)
        startActivity(intent)

    }
}