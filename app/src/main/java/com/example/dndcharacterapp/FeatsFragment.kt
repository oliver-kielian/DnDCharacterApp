package com.example.dndcharacterapp

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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeatsFragment(private val featsFragmentListener: FeatsFragmentListener? = null, private val charId: Int) : Fragment() {

    interface FeatsFragmentListener{
        fun nextFragmentAfterFeats()
    }

    private lateinit var nextToolbar: Toolbar
    private lateinit var nameText: EditText
    private lateinit var descText: EditText
    private lateinit var prereqText: EditText
    private lateinit var addButton: Button

    private lateinit var dbHelper: DatabaseHelper

    private var featID = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feats, container, false)

        nextToolbar = view.findViewById(R.id.toolbarFeats)

        nameText = view.findViewById(R.id.editTextTextNameFeats)
        descText = view.findViewById(R.id.editTextTextDescFeats)
        prereqText = view.findViewById(R.id.editTextTextPrereq)
        addButton = view.findViewById(R.id.buttonAddFeats)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        if(featsFragmentListener != null)
        {
            addCharacterLogic()
        }
        else{
            updateCharacterLogic()
        }


        return view
    }

    private fun updateCharacterLogic() {
        addButton.setText("Next Feat")
        nextToolbar.inflateMenu(R.menu.update_menu)

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.update -> {
                    lifecycleScope.launch(Dispatchers.IO){

                        dbHelper.updateFeat(featID,
                            nameText.text.toString(),
                            descText.text.toString(),
                            prereqText.text.toString())
                    }
                    true
                }
                R.id.add ->
                {
                    lifecycleScope.launch(Dispatchers.IO) {
                        dbHelper.insertFeats(charId, nameText.text.toString(), descText.text.toString(), prereqText.text.toString())
                    }
                    true
                }
                R.id.clear ->{
                    nameText.text.clear()
                    descText.text.clear()
                    prereqText.text.clear()
                    true
                }
                else -> false
            }
        }
        val cursor = dbHelper.getFeat(charId)

        if(cursor.moveToFirst()) {

            val index = cursor.getColumnIndex("feat_id")
            Log.d("FEATTEST", "$index")
            featID = cursor.getInt(index)

            val nameIndex = cursor.getColumnIndex("name")
            val descIndex = cursor.getColumnIndex("description")
            val prereqIndex = cursor.getColumnIndex("prerequisite")

            nameText.setText(cursor.getString(nameIndex))
            descText.setText(cursor.getString(descIndex))
            prereqText.setText(cursor.getString(prereqIndex))

            cursor.moveToNext()

            addButton.setOnClickListener { nextFeat(cursor) }

        }
    }

    private fun nextFeat(cursor: Cursor) {
        if(!cursor.isAfterLast){
            val nameIndex = cursor.getColumnIndex("name")
            val descIndex = cursor.getColumnIndex("description")
            val prereqIndex = cursor.getColumnIndex("prerequisite")

            nameText.setText(cursor.getString(nameIndex))
            descText.setText(cursor.getString(descIndex))
            prereqText.setText(cursor.getInt(prereqIndex).toString())

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
                    dbHelper.insertFeats(charId, nameText.text.toString(), descText.text.toString(), prereqText.text.toString())
                    featsFragmentListener?.nextFragmentAfterFeats()
                    true
                }

                else -> false
            }
        }

        addButton.setOnClickListener{addSpell()}
    }

    private fun addSpell(){
        dbHelper.insertFeats(charId, nameText.text.toString(), descText.text.toString(), prereqText.text.toString())
        nameText.text.clear()
        descText.text.clear()
        prereqText.text.clear()
    }

}