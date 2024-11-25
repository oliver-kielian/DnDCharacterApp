package com.example.dndcharacterapp

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), ItemAdapter.ItemAdapterListener {
    private lateinit var mainToolbar : Toolbar
    private lateinit var characterList : RecyclerView

    private lateinit var adapter: ItemAdapter

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var cursor: Cursor

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

        dbHelper = DatabaseHelper(applicationContext)
        cursor = dbHelper.getAllCharacters()

        adapter = ItemAdapter(cursor, this)
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
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun click(position: Int) {

    }
}