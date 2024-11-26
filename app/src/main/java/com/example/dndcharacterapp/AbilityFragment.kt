package com.example.dndcharacterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar


class AbilityFragment(private val abilityFragmentListener: AbilityFragmentListener, private val charID: Int) : Fragment() {

    interface AbilityFragmentListener{
        fun nextFragmentAfterAbility()
    }

    private lateinit var nextToolbar: Toolbar

    private lateinit var nameText : EditText
    private lateinit var descText: EditText
    private lateinit var levelText : EditText
    private lateinit var addButton : Button

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ability, container, false)

        nameText = view.findViewById(R.id.editTextTextAbilityName)
        descText = view.findViewById(R.id.editTextTextAbilityDesc)
        levelText = view.findViewById(R.id.editTextNumberAbilityLevel)
        addButton = view.findViewById(R.id.buttonAddAbility)

        nextToolbar = view.findViewById(R.id.toolbarAbility)
        nextToolbar.inflateMenu(R.menu.next_menu)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    dbHelper.insertAbility(charID, nameText.text.toString(), descText.text.toString(), levelText.text.toString().toIntOrNull() ?: -1)
                    abilityFragmentListener.nextFragmentAfterAbility()
                    true
                }

                else -> false
            }
        }

        addButton.setOnClickListener{addAbility()}
        return view
    }

    private fun addAbility(){
        dbHelper.insertAbility(charID, nameText.text.toString(), descText.text.toString(), levelText.text.toString().toIntOrNull() ?: -1)
        nameText.text.clear()
        descText.text.clear()
        levelText.text.clear()
    }

}