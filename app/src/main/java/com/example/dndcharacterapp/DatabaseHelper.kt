package com.example.dndcharacterapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "characters.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val character = """
            CREATE TABLE character(
                character_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name STRING,
                race STRING,
                char_class STRING,
                level INTEGER,
                alignment STRING,
                hit_points INTEGER,
                max_hit_points INTEGER,
                armor_class INTEGER,
                speed INTEGER,
                birthday STRING,
                image STRING
            )
            """
        val skills = """
            CREATE TABLE skills(
                skills_id INTEGER PRIMARY KEY AUTOINCREMENT,
                character_id INTEGER,
                skills_name STRING,
                proficient INTEGER,
                bonus INTEGER,
                FOREIGN KEY(character_id) REFERENCES character(character_id)
            )
        """.trimIndent()
        val background = """
            CREATE TABLE background(
                background_id INTEGER PRIMARY KEY AUTOINCREMENT,
                character_id INTEGER,
                name STRING,
                personality_traits STRING,
                ideals STRING,
                bonds STRING,
                flaws STRING,
                description STRING,
                FOREIGN KEY(character_id) REFERENCES character(character_id)
            )
        """.trimIndent()
        val inventory = """
            CREATE TABLE inventory(
                item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                character_id INTEGER,
                item_name STRING,
                quantity INTEGER,
                weight FLOAT,
                description STRING,
                FOREIGN KEY(character_id) REFERENCES character(character_id)
            )
        """.trimIndent()
        val notes = """
            CREATE TABLE notes(
                notes_id INTEGER PRIMARY KEY AUTOINCREMENT,
                character_id INTEGER,
                note_text STRING,
                FOREIGN KEY(character_id) REFERENCES character(character_id)
            )
        """.trimIndent()
        val spells = """
            CREATE TABLE spells(
                spell_id INTEGER PRIMARY KEY AUTOINCREMENT,
                character_id INTEGER,
                spell_name STRING,
                level INTEGER,
                school STRING,
                casting_time STRING,
                range STRING,
                components STRING,
                duration STRING,
                description STRING,
                FOREIGN KEY(character_id) REFERENCES character(character_id)
            )
        """.trimIndent()
        val feats = """
            CREATE TABLE feats(
                feat_id INTEGER PRIMARY KEY AUTOINCREMENT,
                character_id INTEGER,
                name STRING,
                description STRING,
                prerequisite STRING,
                FOREIGN KEY(character_id) REFERENCES character(character_id)
            )
        """.trimIndent()
        val stats = """
            CREATE TABLE stats(
                stat_id INTEGER PRIMARY KEY AUTOINCREMENT,
                character_id INTEGER,
                strength INTEGER,
                strength_modifier INTEGER,
                dexterity INTEGER,
                dexterity_modifier INTEGER,
                constitution INTEGER,
                constitution_modifier INTEGER,
                intelligence INTEGER,
                intelligence_modifier INTEGER,
                wisdom INTEGER,
                wisdom_modifier INTEGER,
                charisma INTEGER,
                charisma_modifier INTEGER,
                FOREIGN KEY(character_id) REFERENCES character(character_id)
            )
        """.trimIndent()
        val abilities = """
            CREATE TABLE abilities (
                ability_id INTEGER PRIMARY KEY AUTOINCREMENT,
                  character_id INTEGER,
                  name STRING,
                  description STRING,
                  level_requirement INTEGER,
                  FOREIGN KEY(character_id) REFERENCES character(character_id)
            )
        """.trimIndent()

        p0?.execSQL(character)
        p0?.execSQL(skills)
        p0?.execSQL(background)
        p0?.execSQL(inventory)
        p0?.execSQL(notes)
        p0?.execSQL(spells)
        p0?.execSQL(feats)
        p0?.execSQL(stats)
        p0?.execSQL(abilities)

        p0?.execSQL("PRAGMA foreign_keys = ON;")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

   fun getAllCharacters(): Cursor {
        val db = this.readableDatabase

       val cursor = db.query(
           "character",
           arrayOf("character_id", "name"),
           null,
           null,
           null,
           null,
           null,
           null
       )

       return cursor
    }

    fun insertCharacter(name:String, race:String, char_class:String, level:Int, alignment:String, hit_points:Int, max_hit_points:Int, armor_class:Int, speed:Int, bday:String, image:String){
        val db = this.readableDatabase

        val characterValues = ContentValues().apply {
            put("name", name)
            put("race", race)
            put("char_class", char_class)
            put("level", level)
            put("alignment", alignment)
            put("hit_points", hit_points)
            put("max_hit_points", max_hit_points)
            put("armor_class", armor_class)
            put("speed", speed)
            put("birthday", bday)
            put("image", image)
        }

        db.insert("character", null, characterValues)
        db.close()
    }

    fun insertAbility(charId: Int, name:String, desc:String, level:Int)
    {
        val db = this.readableDatabase
        val abilityValues = ContentValues().apply {
            put("character_id", charId)
            put("name", name)
            put("description", desc)
            put("level_requirement", level)
        }

        db.insert("abilities", null, abilityValues)
        db.close()
    }

    fun insertFeats(charId: Int, name:String, desc:String, prereq:String){
        val db = this.readableDatabase

        val featValues = ContentValues().apply {
            put("character_id", charId)
            put("name", name)
            put("description", desc)
            put("prerequisite", prereq)
        }

        db.insert("feats", null, featValues)
        db.close()
    }

    fun insertInventory(charId: Int, name: String, quantity: Int, weight : Float, desc: String){
        val db = this.readableDatabase

        val itemValues = ContentValues().apply {
            put("character_id", charId)
            put("item_name", name)
            put("quantity", quantity)
            put("weight", weight)
            put("description", desc)
        }

        db.insert("inventory", null, itemValues)
        db.close()
    }

    fun insertSkill(charId: Int, name: String, proficient: Boolean, bonus: Int){
        val db = this.readableDatabase
        var proficientInt = 0
        if(proficient)
        {
            proficientInt = 1
        }

        val skillValues = ContentValues().apply {
            put("character_id", charId)
            put("skills_name", name)
            put("proficient", proficientInt)
            put("bonus", bonus)
        }

        db.insert("skills", null, skillValues)
        db.close()
    }

    fun insertNotes(charId: Int, text : String){
        val db = this.readableDatabase

        val noteValues = ContentValues().apply{
            put("character_id", charId)
            put("note_text", text)
        }

        db.insert("notes", null, noteValues)
        db.close()
    }

    fun insertSpells(charId: Int, name: String, level: Int, school : String, castingTime : String, range: String, components : String, duration : String, desc: String){
        val db = this.readableDatabase

        val spellValues = ContentValues().apply{
            put("character_id", charId)
            put("spell_name", name)
            put("level", level)
            put("school", school)
            put("casting_time", castingTime)
            put("range", range)
            put("components", components)
            put("duration", duration)
            put("description", desc)
        }

        db.insert("spells", null, spellValues)
        db.close()
    }

    fun insertStats(charId: Int, strength : Int, strMod : Int, dex : Int, dexMod : Int, cons : Int, consMod : Int, int : Int, intMod : Int, wis : Int, wisMod : Int, char : Int, charMod : Int){
        val db = this.readableDatabase

        val statValues = ContentValues().apply{
            put("character_id", charId)
            put("strength", strength)
            put("strength_modifier", strMod)
            put("dexterity", dex)
            put("dexterity_modifier", dexMod)
            put("constitution", cons)
            put("constitution_modifier", consMod)
            put("intelligence", int)
            put("intelligence_modifier", intMod)
            put("wisdom", wis)
            put("wisdom_modifier", wisMod)
            put("charisma", char)
            put("charisma_modifier", charMod)
        }

        db.insert("stats", null, statValues)
        db.close()
    }

    fun insertBackground(charId: Int, name: String, personality_traits : String, ideals : String, bonds : String, flaws : String, desc: String){

        val db = this.readableDatabase

        val backgroundValues = ContentValues().apply{
            put("character_id", charId)
            put("name", name)
            put("personality_traits", personality_traits)
            put("ideals", ideals)
            put("bonds", bonds)
            put("flaws", flaws)
            put("description", desc)
        }

        db.insert("background", null, backgroundValues)
        db.close()
    }

    fun getCharacter(id: Int? = -1, name: String = ""): Cursor {
        val db = this.readableDatabase
        var cursor = db.query(
            "character",
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        if (id == -1) {
            cursor = db.query(
                "character",
                arrayOf("character_id", "name"),
                "name = ?",
                arrayOf(name),
                null,
                null,
                null,
                null
            )
        } else if (name == "") {
            cursor = db.query(
                "character",
                arrayOf(
                    "character_id",
                    "name",
                    "race",
                    "char_class",
                    "level",
                    "alignment",
                    "hit_points",
                    "max_hit_points",
                    "armor_class",
                    "speed",
                    "birthday",
                    "image"
                ),
                "character_id = ?",
                arrayOf(id.toString()),
                null,
                null,
                null,
                null
            )
        }
        return cursor
    }

    fun getAbility(charId: Int) : Cursor
    {
        val db = this.readableDatabase

        val cursor = db.query(

        )
    }
}
