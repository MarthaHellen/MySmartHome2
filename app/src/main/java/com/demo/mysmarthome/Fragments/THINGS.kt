package com.demo.mysmarthome.Fragments

import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.demo.mysmarthome.R
import com.demo.mysmarthome.WriteRoutine



class THINGS : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_t_h_i_n_g_s, container, false)
        val firstLinearLayout = view.findViewById<LinearLayout>(R.id.add_notification_LinearLayout)
        val secondLinearLayout = view.findViewById<LinearLayout>(R.id.display)
        val textView = view.findViewById<TextView>(R.id.text)

        firstLinearLayout.setOnClickListener {
            firstLinearLayout.visibility = View.GONE
            secondLinearLayout.visibility = View.VISIBLE
            textView.requestFocus()

            secondLinearLayout.setOnClickListener {
                val intent = Intent(requireContext(), WriteRoutine::class.java)
                intent.putExtra("notificationCreated", true) //as your loading, load with this data
                startActivity(intent)
            }
        }

        return view





}}