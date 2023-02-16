package com.example.linkedin_clone.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.linkedin_clone.R
import com.example.linkedin_clone.searchActivity

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        view.findViewById<TextView>(R.id.item_search_input)?.setOnClickListener {
            val intent = Intent (activity, searchActivity::class.java)
            activity?.startActivity(intent)
        }
        return view

        }
}