package com.example.dndcharacterapp

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Duration

class SpellsFragment(private val spellsFragmentListener: SpellsFragmentListener? = null, private val charID : Int) : Fragment() {

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

    private var spellId = 0

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

        if(spellsFragmentListener != null){
            addCharacterLogic()
        }
        else{
            updateCharacterLogic()
        }


        return view
    }

    private fun updateCharacterLogic() {
        addButton.setText("Next Spell")
        nextToolbar.inflateMenu(R.menu.update_menu)

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.update -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        dbHelper.updateSpell(spellId,
                            spellNameText.text.toString(),
                            levelText.text.toString().toIntOrNull() ?: -1,
                            schoolText.text.toString(),
                            castingTimeText.text.toString(),
                            rangeText.text.toString(),
                            componentsText.text.toString(),
                            durationText.text.toString(),
                            descText.text.toString())
                    }
                    true
                }

                R.id.add -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        dbHelper.insertSpells(charID,
                            spellNameText.text.toString(),
                            levelText.text.toString().toIntOrNull() ?: -1,
                            schoolText.text.toString(),
                            castingTimeText.text.toString(),
                            rangeText.text.toString(),
                            componentsText.text.toString(),
                            durationText.text.toString(),
                            descText.text.toString())
                    }
                    true
                }

                R.id.clear -> {
                    spellNameText.text.clear()
                    levelText.text.clear()
                    schoolText.text.clear()
                    castingTimeText.text.clear()
                    rangeText.text.clear()
                    componentsText.text.clear()
                    durationText.text.clear()
                    descText.text.clear()

                    true
                }

                else -> false
            }
        }
        val cursor = dbHelper.getSpell(charID)

        if (cursor.moveToFirst()) {

            val index = cursor.getColumnIndex("spell_id")
            spellId = cursor.getInt(index)

            val nameIndex = cursor.getColumnIndex("spell_name")
            val levelIndex = cursor.getColumnIndex("level")
            val schoolIndex = cursor.getColumnIndex("school")
            val castingTimeIndex = cursor.getColumnIndex("casting_time")
            val rangeIndex = cursor.getColumnIndex("range")
            val componentsIndex = cursor.getColumnIndex("components")
            val durationIndex = cursor.getColumnIndex("duration")
            val descIndex = cursor.getColumnIndex("description")

            spellNameText.setText(cursor.getString(nameIndex))
            levelText.setText(cursor.getInt(levelIndex).toString())
            schoolText.setText(cursor.getString(schoolIndex))
            castingTimeText.setText(cursor.getString(castingTimeIndex))
            rangeText.setText(cursor.getString(rangeIndex))
            componentsText.setText(cursor.getString(componentsIndex))
            durationText.setText(cursor.getString(durationIndex))
            descText.setText(cursor.getString(descIndex))

            cursor.moveToNext()

            addButton.setOnClickListener { nextSpell(cursor) }

        }
    }

    private fun nextSpell(cursor: Cursor) {
        if(!cursor.isAfterLast){
            val nameIndex = cursor.getColumnIndex("spell_name")
            val levelIndex = cursor.getColumnIndex("level")
            val schoolIndex = cursor.getColumnIndex("school")
            val castingTimeIndex = cursor.getColumnIndex("casting_time")
            val rangeIndex = cursor.getColumnIndex("range")
            val componentsIndex = cursor.getColumnIndex("components")
            val durationIndex = cursor.getColumnIndex("duration")
            val descIndex = cursor.getColumnIndex("description")

            spellNameText.setText(cursor.getString(nameIndex))
            levelText.setText(cursor.getInt(levelIndex).toString())
            schoolText.setText(cursor.getString(schoolIndex))
            castingTimeText.setText(cursor.getString(castingTimeIndex))
            rangeText.setText(cursor.getString(rangeIndex))
            componentsText.setText(cursor.getString(componentsIndex))
            durationText.setText(cursor.getString(durationIndex))
            descText.setText(cursor.getString(descIndex))

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
                    dbHelper.insertSpells(charID,
                        spellNameText.text.toString(),
                        levelText.text.toString().toIntOrNull() ?: -1,
                        schoolText.text.toString(),
                        castingTimeText.text.toString(),
                        rangeText.text.toString(),
                        componentsText.text.toString(),
                        durationText.text.toString(),
                        descText.text.toString())
                    spellsFragmentListener?.nextFragmentAfterSpells()
                    true
                }

                else -> false
            }
        }

        addButton.setOnClickListener{addSpell()}
    }

    private fun addSpell(){
        dbHelper.insertSpells(charID,
            spellNameText.text.toString(),
            levelText.text.toString().toIntOrNull() ?: -1,
            schoolText.text.toString(),
            castingTimeText.text.toString(),
            rangeText.text.toString(),
            componentsText.text.toString(),
            durationText.text.toString(),
            descText.text.toString())

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