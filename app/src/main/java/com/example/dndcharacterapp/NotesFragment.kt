package com.example.dndcharacterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

class NotesFragment(private val notesFragmentListener: NotesFragmentListener, private val charID : Int) : Fragment() {

    interface NotesFragmentListener{
        fun finishAdding()
    }

    private lateinit var nextToolbar: Toolbar

    private lateinit var noteText: EditText

    private lateinit var addButton: Button

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        nextToolbar = view.findViewById(R.id.toolbarNotes)

        noteText = view.findViewById(R.id.editTextTextNote)

        addButton = view.findViewById(R.id.buttonAddNote)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        nextToolbar.inflateMenu(R.menu.next_menu)

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    dbHelper.insertNotes(charID, noteText.text.toString())
                    notesFragmentListener.finishAdding()
                    true
                }

                else -> false
            }
        }

        addButton.setOnClickListener{addNote()}
        return view
    }

    private fun addNote(){
        dbHelper.insertNotes(charID, noteText.text.toString())
        noteText.text.clear()
    }

}