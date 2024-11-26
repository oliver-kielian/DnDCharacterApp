package com.example.dndcharacterapp

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), ItemAdapter.ItemAdapterListener {
    private lateinit var mainToolbar : Toolbar
    private lateinit var characterList : RecyclerView
    private lateinit var errorText: TextView

    private lateinit var adapter: ItemAdapter

    private lateinit var dbHelper: DatabaseHelper

    companion object{
        const val ADDED = 1
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult())
    {
            result ->
        if(result.resultCode != ADDED){
            errorText.text = "Failed to add character, Please try again"
        }
        else{
            adapter.updateCursor(dbHelper.getAllCharacters())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainToolbar = findViewById(R.id.mainToolbar)
        characterList = findViewById(R.id.characterRecyclerView)
        errorText = findViewById(R.id.textViewError)

        dbHelper = DatabaseHelper(applicationContext)

        adapter = ItemAdapter(dbHelper.getAllCharacters(), this)
        characterList.adapter = adapter
        characterList.layoutManager = LinearLayoutManager(this)

        setSupportActionBar(mainToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.add ->{
                val intent = Intent(applicationContext, CharacterActivity::class.java)
                startForResult.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun click(id: Int) {

    }
}