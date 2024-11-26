package com.example.dndcharacterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BackgroundFragment(private val backgroundFragmentListener: BackgroundFragmentListener? = null, private val charID : Int) : Fragment() {

    interface BackgroundFragmentListener{
        fun nextFragmentAfterBackground()
    }

    private lateinit var nextToolbar: Toolbar
    private lateinit var backgroundNameText :EditText
    private lateinit var personText: EditText
    private lateinit var idealsText: EditText
    private lateinit var bondsText: EditText
    private lateinit var flawsText: EditText
    private lateinit var descText: EditText

    private lateinit var dbHelper: DatabaseHelper

    private var backgroundID = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_background, container, false)

        backgroundNameText = view.findViewById(R.id.editTextTextBackgroundName)
        personText = view.findViewById(R.id.editTextTextPerson)
        idealsText = view.findViewById(R.id.editTextTextIdeals)
        bondsText = view.findViewById(R.id.editTextTextBonds)
        flawsText = view.findViewById(R.id.editTextTextFlaws)
        descText = view.findViewById(R.id.editTextTextBackgroundDesc)

        nextToolbar = view.findViewById(R.id.toolbarBackground)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        if(backgroundFragmentListener != null){
            addCharacterLogic()
        }

        else{
            updateCharacterLogic()
        }

        return view
    }

    private fun updateCharacterLogic() {
        nextToolbar.inflateMenu(R.menu.update_menu)

        nextToolbar.setOnMenuItemClickListener {
                item ->
            when (item.itemId) {
                R.id.update -> {
                    lifecycleScope.launch(Dispatchers.IO){
                        dbHelper.updateBackground(backgroundID,
                            backgroundNameText.text.toString(),
                            personText.text.toString(),
                            idealsText.text.toString(),
                            bondsText.text.toString(),
                            flawsText.text.toString(),
                            descText.text.toString())
                    }
                    true
                }
                R.id.add ->
                {
                    Toast.makeText(context, "Cannot add multiple backgrounds",Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.clear ->{
                    Toast.makeText(context, "Cannot add multiple backgrounds",Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        val cursor = dbHelper.getBackground(charID)

        if (cursor.moveToFirst())
        {
            val index = cursor.getColumnIndex("background_id")
            backgroundID = cursor.getInt(index)

            val nameIndex = cursor.getColumnIndex("name")
            val personIndex = cursor.getColumnIndex("personality_traits")
            val idealsIndex = cursor.getColumnIndex("ideals")
            val bondsIndex = cursor.getColumnIndex("bonds")
            val flawsIndex = cursor.getColumnIndex("flaws")
            val descIndex = cursor.getColumnIndex("description")

            backgroundNameText.setText(cursor.getString(nameIndex))
            personText.setText(cursor.getString(personIndex))
            idealsText.setText(cursor.getString(idealsIndex))
            bondsText.setText(cursor.getString(bondsIndex))
            flawsText.setText(cursor.getString(flawsIndex))
            descText.setText(cursor.getString(descIndex))
        }
    }

    private fun addCharacterLogic() {
        nextToolbar.inflateMenu(R.menu.next_menu)


        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    dbHelper.insertBackground(charID,
                        backgroundNameText.text.toString(),
                        personText.text.toString(),
                        idealsText.text.toString(),
                        bondsText.text.toString(),
                        flawsText.text.toString(),
                        descText.text.toString())
                    backgroundFragmentListener?.nextFragmentAfterBackground()
                    true
                }

                else -> false
            }
        }
    }
}