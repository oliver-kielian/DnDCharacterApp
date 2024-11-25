package com.example.dndcharacterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

class StatsFragment(private val statsFragmentListener: StatsFragmentListener) : Fragment() {

    interface StatsFragmentListener{
        fun nextFragmentAfterStats()
    }

    private lateinit var nextToolbar: Toolbar
    private lateinit var rollButton: Button
    private lateinit var strengthText : EditText
    private lateinit var strengthTextMod : EditText
    private lateinit var dexText : EditText
    private lateinit var dexTextMod : EditText
    private lateinit var constitutionText : EditText
    private lateinit var constitutionTextMod : EditText
    private lateinit var intText : EditText
    private lateinit var intTextMod : EditText
    private lateinit var wisText : EditText
    private lateinit var wisTextMod : EditText
    private lateinit var charismaText : EditText
    private lateinit var charismaTextMod : EditText

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        rollButton = view.findViewById(R.id.buttonRoll)
        strengthText = view.findViewById(R.id.editTextNumberStrength)
        strengthTextMod = view.findViewById(R.id.editTextNumberStrengthMod)
        dexText = view.findViewById(R.id.editTextNumberDexterity)
        dexTextMod = view.findViewById(R.id.editTextNumberDexterityMod)
        constitutionText = view.findViewById(R.id.editTextNumberConstitution)
        constitutionTextMod = view.findViewById(R.id.editTextNumberConstitutionMod)
        intText = view.findViewById(R.id.editTextNumberIntelligence)
        intTextMod = view.findViewById(R.id.editTextNumberIntelligenceMod)
        wisText = view.findViewById(R.id.editTextNumberWisdom)
        wisTextMod = view.findViewById(R.id.editTextNumberWisdomMod)
        charismaText = view.findViewById(R.id.editTextNumberCharisma)
        charismaTextMod = view.findViewById(R.id.editTextNumberCharismaMod)

        nextToolbar = view.findViewById(R.id.toolbarNext)
        nextToolbar.inflateMenu(R.menu.next_menu)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {

                    statsFragmentListener.nextFragmentAfterStats()
                    true
                }

                else -> false
            }
        }

        return view
    }
}