package com.example.dndcharacterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import kotlin.time.Duration

class SpellsFragment(private val spellsFragmentListener: SpellsFragmentListener) : Fragment() {

    interface SpellsFragmentListener{
        fun nextFragmentAfterSpells()
    }

    private lateinit var nextToolbar: Toolbar
    private lateinit var spellNameText: EditText
    private lateinit var levelText: EditText
    private lateinit var schoolText: EditText
    private lateinit var castingTimeText: EditText
    private lateinit var rangeText: EditText
    private lateinit var componentsText: EditText
    private lateinit var durationText: EditText
    private lateinit var descText: EditText
    private lateinit var addButton: Button

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_spells, container, false)

        nextToolbar = view.findViewById(R.id.toolbarSpells)
        spellNameText = view.findViewById(R.id.editTextTextNameSpells)
        levelText = view.findViewById(R.id.editTextNumberLevelSpells)
        schoolText = view.findViewById(R.id.editTextTextSchool)
        castingTimeText = view.findViewById(R.id.editTextTextCastingTime)
        rangeText = view.findViewById(R.id.editTextTextRange)
        componentsText = view.findViewById(R.id.editTextTextComponents)
        durationText = view.findViewById(R.id.editTextTextDuration)
        descText = view.findViewById(R.id.editTextTextDescSpells)
        addButton = view.findViewById(R.id.buttonAddSpells)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    //TODO: INSERT INTO DATABASE
                    spellsFragmentListener.nextFragmentAfterSpells()
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
        spellNameText.text.clear()
        levelText.text.clear()
        schoolText.text.clear()
        castingTimeText.text.clear()
        rangeText.text.clear()
        componentsText.text.clear()
        durationText.text.clear()
        descText.text.clear()

    }

}