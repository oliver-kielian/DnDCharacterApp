package com.example.dndcharacterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

class BackgroundFragment(private val backgroundFragmentListener: BackgroundFragmentListener) : Fragment() {

    interface BackgroundFragmentListener{
        fun nextFragmentAfterBackground()
    }

    private lateinit var nextToolbar: Toolbar
    private lateinit var backgroundNameText :EditText
    private lateinit var personText: EditText
    private lateinit var idealsText: EditText
    private lateinit var bondsText: EditText
    private lateinit var flawsText: EditText

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_background, container, false)

        backgroundNameText = view.findViewById(R.id.editTextTextBackgroundName)
        personText = view.findViewById(R.id.editTextTextPerson)
        idealsText = view.findViewById(R.id.editTextTextIdeals)
        bondsText = view.findViewById(R.id.editTextTextBonds)
        flawsText = view.findViewById(R.id.editTextTextFlaws)

        nextToolbar = view.findViewById(R.id.toolbarBackground)
        nextToolbar.inflateMenu(R.menu.next_menu)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    //TODO: INSERT INTO DATABASE
                    backgroundFragmentListener.nextFragmentAfterBackground()
                    true
                }

                else -> false
            }
        }

        return view
    }
}