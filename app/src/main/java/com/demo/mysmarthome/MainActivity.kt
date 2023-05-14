package com.demo.mysmarthome

import    androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //what does this do

        val favouriteFragment = Favourites()
         val thingsFragment = Things()
         val routinesFragment = Routines()
         val ideasFragment = Ideas()
         val settingsFragment = Settings()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView2)

        val selectedFragment = intent.getStringExtra("SELECTED_FRAGMENT")
        if (selectedFragment == "routines"){
            replaceFragment(routinesFragment)
            bottomNavigationView.selectedItemId= R.id.routines

        }else{
        replaceFragment(Favourites())}

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favourites -> replaceFragment(favouriteFragment)
                R.id.things -> replaceFragment(thingsFragment)
                R.id.routines -> replaceFragment(routinesFragment)
                R.id.ideas -> replaceFragment(ideasFragment)
                R.id.settings -> replaceFragment(settingsFragment)
            }
           true

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame, fragment)
            transaction.commit()
        }

    }
}

