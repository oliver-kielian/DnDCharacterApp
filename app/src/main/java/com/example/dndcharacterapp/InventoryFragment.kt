package com.example.dndcharacterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

class InventoryFragment(private val inventoryFragmentListener: InventoryFragmentListener? = null, private val charID : Int) : Fragment() {

    interface InventoryFragmentListener{
        fun nextFragmentAfterInventory()
    }

    private lateinit var nextToolbar: Toolbar

    private lateinit var nameText: EditText
    private lateinit var quantityText: EditText
    private lateinit var weightText: EditText
    private lateinit var descText: EditText

    private lateinit var addButton: Button

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)

        nextToolbar = view.findViewById(R.id.toolbarInventory)

        nameText = view.findViewById(R.id.editTextTextNameInventory)
        quantityText = view.findViewById(R.id.editTextNumberQuantity)
        weightText = view.findViewById(R.id.editTextNumberWeight)
        descText = view.findViewById(R.id.editTextTextDescInventory)
        addButton = view.findViewById(R.id.buttonAddItem)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        nextToolbar.inflateMenu(R.menu.next_menu)

        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    dbHelper.insertInventory(charID, nameText.text.toString(),
                        quantityText.text.toString().toIntOrNull() ?: -1,
                        weightText.text.toString().toFloatOrNull() ?: -1.0f,
                        descText.text.toString())
                    inventoryFragmentListener?.nextFragmentAfterInventory()
                    true
                }

                else -> false
            }
        }

        addButton.setOnClickListener{addItem()}
        return view
    }

    private fun addItem(){
        dbHelper.insertInventory(charID, nameText.text.toString(),
            quantityText.text.toString().toIntOrNull() ?: -1,
            weightText.text.toString().toFloatOrNull() ?: -1.0f,
            descText.text.toString())
        nameText.text.clear()
        quantityText.text.clear()
        weightText.text.clear()
        descText.text.clear()
    }

}