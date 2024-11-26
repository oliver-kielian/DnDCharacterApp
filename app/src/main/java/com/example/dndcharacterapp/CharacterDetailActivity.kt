package com.example.dndcharacterapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var characterImage: WebView
    private lateinit var tables : Spinner
    private lateinit var goButton : Button

    private lateinit var dbHelper: DatabaseHelper
    private var charID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_character_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(applicationContext)

        characterImage = findViewById(R.id.characterImageView)
        tables = findViewById(R.id.spinnerTables)
        goButton = findViewById(R.id.buttonGo)

        val intent = intent
        charID = intent.getIntExtra("ID", 0)
        Log.d("CharacterID", "Query: $charID")

        setCharacterImage()
    }

    private fun setCharacterImage() {
        val cursor = dbHelper.getCharacterByID(charID)

        cursor.moveToFirst()
        val imageIndex = cursor.getColumnIndex("image")
        if(cursor.getString(imageIndex) != ""){
            characterImage.loadUrl(cursor.getString(imageIndex))
        }
    }

    fun go(view: View) {
        when (tables.selectedItem.toString()){
                "Character"->{
                    val characterFragment = CharacterFragment()
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.tables, characterFragment)
                    ft.commit()
                }
                "Skills"->{
                    val skillsFragment = SkillsFragment(null, charID)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.tables, skillsFragment)
                    ft.commit()
                }
                "Background"->{
                    val backgroundFragment = BackgroundFragment(null, charID)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.tables, backgroundFragment)
                    ft.commit()
                }
                "Inventory"->{
                    val inventoryFragment = InventoryFragment(null, charID)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.tables, inventoryFragment)
                    ft.commit()
                }
                "Notes"->{
                    val notesFragment = NotesFragment(null, charID)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.tables, notesFragment)
                    ft.commit()
                }
                "Spells"->{
                    val spellsFragment = SpellsFragment(null, charID)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.tables, spellsFragment)
                    ft.commit()
                }
                "Feats"->{
                    val featsFragment = FeatsFragment(null, charID)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.tables, featsFragment)
                    ft.commit()
                }
                "Stats"->{
                    val statsFragment = StatsFragment(null, charID)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.tables, statsFragment)
                    ft.commit()
                }
                "Abilities"->{
                    val abilityFragment = AbilityFragment(null, charID)
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(R.id.tables, abilityFragment)
                    ft.commit()
                }
        }
    }
}