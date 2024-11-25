package com.example.dndcharacterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

class FeatsFragment(private val featsFragmentListener: FeatsFragmentListener) : Fragment() {

    interface FeatsFragmentListener{
        fun nextFragmentAfterFeats()
    }

    private lateinit var nextToolbar: Toolbar
    private lateinit var nameText: EditText
    private lateinit var descText: EditText
    private lateinit var prereqText: EditText
    private lateinit var addButton: Button

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feats, container, false)

        nextToolbar = view.findViewById(R.id.toolbarFeats)

        nameText = view.findViewById(R.id.editTextTextNameFeats)
        descText = view.findViewById(R.id.editTextTextDescFeats)
        prereqText = view.findViewById(R.id.editTextTextPrereq)
        addButton = view.findViewById(R.id.buttonAddFeats)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        nextToolbar.inflateMenu(R.menu.next_menu)

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    //TODO: INSERT INTO DATABASE
                    featsFragmentListener.nextFragmentAfterFeats()
                    true
                }

                else -> false
            }
        }

        addButton.setOnClickListener{addSpell()}
        return view
    }

    private fun addSpell(){
        //TODO: INSERT INTO DATABASE
        nameText.text.clear()
        descText.text.clear()
        prereqText.text.clear()
    }

}