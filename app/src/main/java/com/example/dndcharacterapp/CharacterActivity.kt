package com.example.dndcharacterapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CharacterActivity : AppCompatActivity(),
    CharacterFragment.CharacterFragmentListener,
    StatsFragment.StatsFragmentListener,
    SkillsFragment.SkillsFragmentListener,
    BackgroundFragment.BackgroundFragmentListener,
    AbilityFragment.AbilityFragmentListener,
    SpellsFragment.SpellsFragmentListener,
    FeatsFragment.FeatsFragmentListener,
    InventoryFragment.InventoryFragmentListener,
    NotesFragment.NotesFragmentListener{

        private lateinit var resultIntent : Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_character)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val characterFragment = CharacterFragment(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frameLayoutFields, characterFragment)
        ft.commit()

        resultIntent = intent
    }

    override fun nextFragmentFromCharacter() {
        val statsFragment = StatsFragment(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frameLayoutFields, statsFragment)
        ft.commit()
    }

    override fun nextFragmentAfterStats() {
        val skillsFragment = SkillsFragment(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frameLayoutFields, skillsFragment)
        ft.commit()
    }

    override fun nextFragmentAfterSkills() {
        val backgroundFragment = BackgroundFragment(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frameLayoutFields, backgroundFragment)
        ft.commit()
    }

    override fun nextFragmentAfterBackground() {
        val abilityFragment = AbilityFragment(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frameLayoutFields, abilityFragment)
        ft.commit()
    }

    override fun nextFragmentAfterAbility() {
        val spellsFragment = SpellsFragment(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frameLayoutFields, spellsFragment)
        ft.commit()
    }

    override fun nextFragmentAfterSpells() {
        val featsFragment = FeatsFragment(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frameLayoutFields, featsFragment)
        ft.commit()
    }

    override fun nextFragmentAfterFeats() {
        val inventoryFragment = InventoryFragment(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frameLayoutFields, inventoryFragment)
        ft.commit()
    }

    override fun nextFragmentAfterInventory() {
        val notesFragment = NotesFragment(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frameLayoutFields, notesFragment)
        ft.commit()
    }

    override fun finishAdding() {
        setResult(MainActivity.ADDED, resultIntent)
        finish()
    }

}