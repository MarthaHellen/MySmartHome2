package com.demo.mysmarthome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SelectEvent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_event)
        // Find the TextView in the layout
        val time = findViewById<TextView>(R.id.selectEventTime)
        // Set an onClickListener for the TextView
        time.setOnClickListener {
            // Create an Intent to start the writeRoutine activity
            val intent = Intent(this,WriteRoutine::class.java)
            intent.putExtra("showTimePicker", true)
            startActivity(intent)
        }
    }
}