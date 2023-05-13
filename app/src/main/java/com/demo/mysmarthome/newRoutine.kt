package com.demo.mysmarthome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class newRoutine : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_routine)
        // Find the tap in the activity layout
        val tap = findViewById<FloatingActionButton>(R.id.tap)
// Set a click listener on the tap
        tap.setOnClickListener {
            // Create an Intent to navigate to the next activity
            val intent = Intent(this, WriteRoutine::class.java)
            startActivity(intent)
        }
    }
}