package com.example.dndcharacterapp

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SkillsFragment(private val skillsFragmentListener: SkillsFragmentListener? = null, private val charID : Int) : Fragment() {

    interface SkillsFragmentListener{
        fun nextFragmentAfterSkills()
    }

    private lateinit var nextToolbar : Toolbar
    private lateinit var skillsNameTextView: TextView
    private lateinit var checkBoxProficient: CheckBox
    private lateinit var bonusText : EditText
    private lateinit var nextButton : Button

    private lateinit var dbHelper: DatabaseHelper

    private var currentSkill = 0

    var skillId = 0

    companion object {
        val skills = listOf(
            "Acrobatics",
            "Animal Handling",
            "Arcana",
            "Athletics",
            "Deception",
            "History",
            "Insight",
            "Intimidation",
            "Investigation",
            "Medicine",
            "Nature",
            "Perception",
            "Performance",
            "Persuasion",
            "Religion",
            "Sleight of Hand",
            "Stealth",
            "Survival"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_skills_fagment, container, false)

        skillsNameTextView = view.findViewById(R.id.textViewSkill)
        checkBoxProficient = view.findViewById(R.id.checkBoxProficient)
        bonusText = view.findViewById(R.id.editTextNumberBonus)
        nextButton = view.findViewById(R.id.buttonNext)

        nextToolbar = view.findViewById(R.id.toolbarNextSkills)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        if(skillsFragmentListener != null){
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
            when(item.itemId){
                R.id.update ->{
                    lifecycleScope.launch(Dispatchers.IO){
                        dbHelper.updateSkill(skillId, checkBoxProficient.isChecked, bonusText.text.toString().toIntOrNull() ?: -1)
                    }
                    true
                }
                R.id.add -> {
                    Toast.makeText(context, "All Skills already added", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.clear -> {
                    Toast.makeText(context, "All Skills already added", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        val cursor = dbHelper.getSkill(charID)
            if (cursor.moveToFirst()) {
                val proficientIndex = cursor.getColumnIndex("proficient")
                val bonusIndex = cursor.getColumnIndex("bonus")

                val proficient = cursor.getInt(proficientIndex)

                skillsNameTextView.text = skills[0]
                checkBoxProficient.isChecked = proficient == 1
                bonusText.setText(cursor.getInt(bonusIndex).toString())

                cursor.moveToNext()

                nextButton.setOnClickListener{nextSkillUpdate(cursor)}
            }

    }

    private fun addCharacterLogic() {

        nextButton.setOnClickListener{nextSkillAdd()}

        nextToolbar.inflateMenu(R.menu.next_menu)

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    if(currentSkill == skills.lastIndex) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            dbHelper.insertSkill(
                                charID,
                                skillsNameTextView.text.toString(),
                                checkBoxProficient.isChecked,
                                bonusText.text.toString().toIntOrNull() ?: -1
                            )
                        }
                        skillsFragmentListener?.nextFragmentAfterSkills()
                    }
                    else{
                        Toast.makeText(context, "Please add all Skills", Toast.LENGTH_SHORT).show()
                    }
                    true
                }

                else -> false
            }
        }

        skillsNameTextView.text = skills[currentSkill]
    }

    private fun nextSkillAdd()
    {
        if(currentSkill != skills.size-1)
        {
            currentSkill++
                lifecycleScope.launch(Dispatchers.IO) {
                    dbHelper.insertSkill(
                        charID,
                        skillsNameTextView.text.toString(),
                        checkBoxProficient.isChecked, bonusText.text.toString().toIntOrNull() ?: -1
                    )
                }
                skillsNameTextView.text = skills[currentSkill]
                checkBoxProficient.isChecked = false
                bonusText.text.clear()
        }
        else{
            skillsNameTextView.text = "No more skills"
        }
    }

    private fun nextSkillUpdate(cursor: Cursor)
    {
        if(!cursor.isAfterLast) {
            val proficientIndex = cursor.getColumnIndex("proficient")
            val bonusIndex = cursor.getColumnIndex("bonus")
            val nameIndex = cursor.getColumnIndex("skills_name")

            val proficient = cursor.getInt(proficientIndex)

            val name = cursor.getString(nameIndex)
            skillsNameTextView.text = cursor.getString(nameIndex)
            checkBoxProficient.isChecked = proficient == 1
            bonusText.setText(cursor.getInt(bonusIndex).toString())

            cursor.moveToNext()
        }
        else{
            cursor.close()
        }
    }
}