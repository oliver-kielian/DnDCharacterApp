package com.example.dndcharacterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

class CharacterFragment(private val characterFragmentListener: CharacterFragmentListener? = null) : Fragment() {

    interface CharacterFragmentListener {
        fun nextFragmentFromCharacter(charID: Int)
    }

    private lateinit var nextToolbar: Toolbar
    private lateinit var nameText : EditText
    private lateinit var raceText : EditText
    private lateinit var classText : EditText
    private lateinit var levelText : EditText
    private lateinit var alignmentText : EditText
    private lateinit var hitPointsText : EditText
    private lateinit var maxHitPointsText : EditText
    private lateinit var armorClassText : EditText
    private lateinit var proficiencyText : EditText
    private lateinit var speedText : EditText
    private lateinit var birthdayButton : Button
    private lateinit var imageText : EditText

    private lateinit var dbHelper: DatabaseHelper

    var bday = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_character, container, false)

        nameText = view.findViewById(R.id.editTextTextName)
        raceText = view.findViewById(R.id.editTextTextRace)
        classText = view.findViewById(R.id.editTextTextClass)
        levelText = view.findViewById(R.id.editTextNumberLevel)
        alignmentText = view.findViewById(R.id.editTextTextAlignment)
        hitPointsText = view.findViewById(R.id.editTextNumberHitPoints)
        maxHitPointsText = view.findViewById(R.id.editTextNumberMaxHitPoints)
        armorClassText = view.findViewById(R.id.editTextNumberArmorClass)
        proficiencyText = view.findViewById(R.id.editTextNumberProficiency)
        speedText = view.findViewById(R.id.editTextNumberSpeed)
        birthdayButton = view.findViewById(R.id.buttonBday)
        imageText = view.findViewById(R.id.editTextTextImage)

        birthdayButton.setOnClickListener{pickBday()}


        nextToolbar = view.findViewById(R.id.toolbarNext)
        nextToolbar.inflateMenu(R.menu.next_menu)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    dbHelper.insertCharacter(nameText.text.toString(),
                        raceText.text.toString(),
                        classText.text.toString(),
                        levelText.text.toString().toIntOrNull() ?: -1,
                        alignmentText.text.toString(),
                        hitPointsText.text.toString().toIntOrNull() ?: -1,
                        maxHitPointsText.text.toString().toIntOrNull() ?: -1,
                        armorClassText.text.toString().toIntOrNull() ?: -1,
                        proficiencyText.text.toString().toIntOrNull() ?: -1,
                        speedText.text.toString().toIntOrNull() ?: -1,
                        bday,
                        imageText.text.toString())

                    val cursor = dbHelper.getCharacter(null, nameText.text.toString())
                    cursor.moveToFirst()
                    val index = cursor.getColumnIndex("character_id")
                    val charID = cursor.getInt(index)
                    characterFragmentListener?.nextFragmentFromCharacter(charID)
                    true
                }

                else -> false
            }
        }
        return view
    }

    private fun pickBday() {
        TODO("Not yet implemented")
    }
}