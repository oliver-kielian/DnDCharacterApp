package com.example.dndcharacterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class SkillsFragment(private val skillsFragmentListener: SkillsFragmentListener, private val charID : Int) : Fragment() {

    interface SkillsFragmentListener{
        fun nextFragmentAfterSkills()
    }

    private lateinit var nextToolbar : Toolbar
    private lateinit var skillsNameTextView: TextView
    private lateinit var checkBoxProficient: CheckBox
    private lateinit var bonusText : EditText
    private lateinit var nextButton : Button

    private lateinit var dbHelper: DatabaseHelper

    var currentSkill = 0

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
        nextToolbar.inflateMenu(R.menu.next_menu)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    dbHelper.insertSkill(charID,
                        skillsNameTextView.text.toString(),
                        checkBoxProficient.isChecked, bonusText.text.toString().toIntOrNull() ?: -1)
                    skillsFragmentListener.nextFragmentAfterSkills()
                    true
                }

                else -> false
            }
        }

        skillsNameTextView.text = skills[currentSkill]

        nextButton.setOnClickListener{nextSkill()}

        return view
    }

    private fun nextSkill()
    {
        if(currentSkill != skills.size-1)
        {
            currentSkill++
            dbHelper.insertSkill(charID,
                skillsNameTextView.text.toString(),
                checkBoxProficient.isChecked, bonusText.text.toString().toIntOrNull() ?: -1)
            skillsNameTextView.text = skills[currentSkill]
            checkBoxProficient.isChecked = false
            bonusText.text.clear()

        }
        else{
            skillsNameTextView.text = "All skills filled"
        }
    }
}