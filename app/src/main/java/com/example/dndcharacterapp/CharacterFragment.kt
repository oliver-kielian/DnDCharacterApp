package com.example.dndcharacterapp

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CharacterFragment(private val characterFragmentListener: CharacterFragmentListener? = null, private val charID: Int? = null) : Fragment(), DatePickerDialog.OnDateSetListener{

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

    private var bday = "0"

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
        nextToolbar = view.findViewById(R.id.toolbarNext)

        birthdayButton.setOnClickListener{pickBday()}

        dbHelper = context?.let { DatabaseHelper(it) }!!

        if(characterFragmentListener != null){
            addCharacterLogic()
        }
        else{
            updateCharacterLogic()
        }


        return view
    }

    private fun updateCharacterLogic() {
        nextToolbar.inflateMenu(R.menu.update_menu)

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.update -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        if (charID != null) {
                            dbHelper.updateCharacter(
                                charID,
                                nameText.text.toString(),
                                raceText.text.toString(),
                                classText.text.toString(),
                                levelText.text.toString().toIntOrNull() ?: -1,
                                alignmentText.text.toString(),
                                hitPointsText.text.toString().toIntOrNull() ?: -1,
                                maxHitPointsText.text.toString().toIntOrNull() ?: -1,
                                armorClassText.text.toString().toIntOrNull() ?: -1,
                                speedText.text.toString().toIntOrNull() ?: -1,
                                bday,
                                imageText.text.toString()
                            )
                        }
                    }
                    true
                }

                R.id.add -> {
                    Toast.makeText(context, "Please go to home page to add a character", Toast.LENGTH_SHORT)
                        .show()
                    true
                }

                R.id.clear -> {
                    Toast.makeText(context, "Please go to home page to add a character", Toast.LENGTH_SHORT)
                        .show()
                    true
                }

                else -> false
            }
        }

        if(charID != null) {
            val cursor = dbHelper.getCharacterByID(charID)

            if(cursor.moveToFirst())
            {
                val nameIndex = cursor.getColumnIndex("name")
                val raceIndex = cursor.getColumnIndex("race")
                val classIndex = cursor.getColumnIndex("char_class")
                val levelIndex = cursor.getColumnIndex("level")
                val alignmentIndex = cursor.getColumnIndex("alignment")
                val hitPointsIndex = cursor.getColumnIndex("hit_points")
                val maxHitPointsIndex = cursor.getColumnIndex("max_hit_points")
                val armorIndex = cursor.getColumnIndex("armor_class")
                val speedIndex = cursor.getColumnIndex("speed")
                val birthdayIndex = cursor.getColumnIndex("birthday")
                val imageIndex = cursor.getColumnIndex("image")

                nameText.setText(cursor.getString(nameIndex))
                raceText.setText(cursor.getString(raceIndex))
                classText.setText(cursor.getString(classIndex))
                levelText.setText(cursor.getInt(levelIndex).toString())
                alignmentText.setText(cursor.getString(alignmentIndex))
                hitPointsText.setText(cursor.getInt(hitPointsIndex).toString())
                maxHitPointsText.setText(cursor.getInt(maxHitPointsIndex).toString())
                armorClassText.setText(cursor.getInt(armorIndex).toString())
                speedText.setText(cursor.getInt(speedIndex).toString())
                birthdayButton.text = cursor.getString(birthdayIndex)
                imageText.setText(cursor.getString(imageIndex))

            }
        }


    }

    private fun addCharacterLogic() {
        nextToolbar.inflateMenu(R.menu.next_menu)

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    lifecycleScope.launch(Dispatchers.IO) {
                        dbHelper.insertCharacter(
                            nameText.text.toString(),
                            raceText.text.toString(),
                            classText.text.toString(),
                            levelText.text.toString().toIntOrNull() ?: -1,
                            alignmentText.text.toString(),
                            hitPointsText.text.toString().toIntOrNull() ?: -1,
                            maxHitPointsText.text.toString().toIntOrNull() ?: -1,
                            armorClassText.text.toString().toIntOrNull() ?: -1,
                            speedText.text.toString().toIntOrNull() ?: -1,
                            bday,
                            imageText.text.toString()
                        )
                    }
                    val cursor = dbHelper.getCharacterByName(nameText.text.toString())
                    cursor.moveToFirst()
                    val index = cursor.getColumnIndex("character_id")
                    val charID = cursor.getInt(index)
                    characterFragmentListener?.nextFragmentFromCharacter(charID)
                    true
                }

                else -> false
            }
        }
    }

    private fun pickBday() {
        val dateFragment = DateFragment(this)
        dateFragment.show(parentFragmentManager, null)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        bday = String.format("%d/%d/%d", month, dayOfMonth, year)
    }
}