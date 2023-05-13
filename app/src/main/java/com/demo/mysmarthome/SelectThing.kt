package com.demo.mysmarthome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class SelectThing : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_thing)
        tabLayout=findViewById(R.id.tabLayout)
        viewPager=findViewById(R.id.viewPager)

        tabLayout.addTab(tabLayout.newTab().setText("THINGS"))
        tabLayout.addTab(tabLayout.newTab().setText("SCENES"))
        tabLayout.addTab(tabLayout.newTab().setText("ROUTINES"))
        tabLayout.tabGravity= TabLayout.GRAVITY_CENTER

        val adapter =MyAdapter(this,supportFragmentManager,tabLayout.tabCount)
        viewPager.adapter =adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


    }
}