package com.example.dndcharacterapp

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar


class AbilityFragment(private val abilityFragmentListener: AbilityFragmentListener? = null, private val charID: Int) : Fragment() {

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

        dbHelper = context?.let { DatabaseHelper(it) }!!

        nextToolbar = view.findViewById(R.id.toolbarAbility)

        if(abilityFragmentListener != null){

            addCharacterLogic()
        }
        else{
            editCharater()
        }

        return view
    }

    @SuppressLint("Range")
    private fun editCharater() {
        addButton.setText("Next Ability")
        nextToolbar.inflateMenu(R.menu.update_menu)

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.update -> {
                    //TODO: update function
                    true
                }
                R.id.add ->
                {
                    true
                }
                else -> false
            }
        }
        val cursor = dbHelper.getAbility(charID)
        if(cursor.moveToFirst()) {
            addButton.setOnClickListener { nextAbility(cursor) }
        }
    }

    private fun nextAbility(cursor: Cursor) {
        if(!cursor.isAfterLast){
            val nameIndex = cursor.getColumnIndex("name")
            val descIndex = cursor.getColumnIndex("description")
            val levelReqIndex = cursor.getColumnIndex("level_requirement")

            nameText.setText(cursor.getString(nameIndex))
            descText.setText(cursor.getString(descIndex))
            levelText.setText(cursor.getInt(levelReqIndex).toString())

            cursor.moveToNext()
        }
        else{
            cursor.close()
        }
    }

    private fun addCharacterLogic() {
        nextToolbar.inflateMenu(R.menu.next_menu)

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {

                    dbHelper.insertAbility(charID, nameText.text.toString(), descText.text.toString(), levelText.text.toString().toIntOrNull() ?: -1)
                    abilityFragmentListener?.nextFragmentAfterAbility()
                    true
                }

                else -> false
            }
        }

        addButton.setOnClickListener{addAbility()}
    }

        private fun addAbility(){
        dbHelper.insertAbility(charID, nameText.text.toString(), descText.text.toString(), levelText.text.toString().toIntOrNull() ?: -1)
        nameText.text.clear()
        descText.text.clear()
        levelText.text.clear()
    }

}