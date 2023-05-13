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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [THINGS.newInstance] factory method to
 * create an instance of this fragment.
 */
class THINGS : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
                intent.putExtra("notificationCreated", true)
                startActivity(intent)
            }
        }

        return view





}}