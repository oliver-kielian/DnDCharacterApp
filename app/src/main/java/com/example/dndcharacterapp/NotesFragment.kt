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

class NotesFragment(private val notesFragmentListener: NotesFragmentListener? = null, private val charID : Int) : Fragment() {

    interface NotesFragmentListener{
        fun finishAdding()
    }

    private lateinit var nextToolbar: Toolbar

    private lateinit var noteText: EditText

    private lateinit var addButton: Button

    private lateinit var dbHelper: DatabaseHelper

    private var noteId = 0

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



        if(notesFragmentListener != null)
        {
            addCharacterLogic()
        }

        else{
            editCharacterLogic()
        }

        return view
    }

    private fun editCharacterLogic() {
        addButton.setText("Next Note")

        nextToolbar.inflateMenu(R.menu.update_menu)

        nextToolbar.setOnMenuItemClickListener {
                item ->
            when (item.itemId) {
                R.id.update -> {
                    lifecycleScope.launch(Dispatchers.IO){
                        dbHelper.updateNote(noteId, noteText.text.toString())
                    }
                    true
                }
                R.id.add ->
                {
                    lifecycleScope.launch(Dispatchers.IO) {
                        dbHelper.insertNotes(charID, noteText.text.toString())
                    }
                    true
                }
                R.id.clear ->{
                    noteText.text.clear()
                    true
                }
                else -> false
            }
        }

        val cursor = dbHelper.getNote(charID)

        if(cursor.moveToFirst()){
            val index = cursor.getColumnIndex("notes_id")
            noteId = cursor.getInt(index)

            val textIndex = cursor.getColumnIndex("note_text")

            noteText.setText(cursor.getString(textIndex))

            cursor.moveToNext()

            addButton.setOnClickListener(){nextNote(cursor)}
        }
    }

    private fun nextNote(cursor: Cursor) {
        if(!cursor.isAfterLast){
            val textIndex = cursor.getColumnIndex("note_text")

            noteText.setText(cursor.getString(textIndex))

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
                    lifecycleScope.launch(Dispatchers.IO) {
                        dbHelper.insertNotes(charID, noteText.text.toString())
                    }
                    notesFragmentListener?.finishAdding()
                    true
                }

                else -> false
            }
        }

        addButton.setOnClickListener{addNote()}
    }

    private fun addNote(){
        dbHelper.insertNotes(charID, noteText.text.toString())
        noteText.text.clear()
    }

}