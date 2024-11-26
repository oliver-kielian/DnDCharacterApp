package com.example.dndcharacterapp

import android.animation.Animator
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class StatsFragment(private val statsFragmentListener: StatsFragmentListener? = null, private val charID : Int) : Fragment(), Animator.AnimatorListener {

    interface StatsFragmentListener{
        fun nextFragmentAfterStats()
    }

    private lateinit var nextToolbar: Toolbar
    private lateinit var rollButton: Button
    private lateinit var strengthText : EditText
    private lateinit var strengthTextMod : EditText
    private lateinit var dexText : EditText
    private lateinit var dexTextMod : EditText
    private lateinit var constitutionText : EditText
    private lateinit var constitutionTextMod : EditText
    private lateinit var intText : EditText
    private lateinit var intTextMod : EditText
    private lateinit var wisText : EditText
    private lateinit var wisTextMod : EditText
    private lateinit var charismaText : EditText
    private lateinit var charismaTextMod : EditText

    private lateinit var dbHelper: DatabaseHelper

    private var statId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        rollButton = view.findViewById(R.id.buttonRoll)
        strengthText = view.findViewById(R.id.editTextNumberStrength)
        strengthTextMod = view.findViewById(R.id.editTextNumberStrengthMod)
        dexText = view.findViewById(R.id.editTextNumberDexterity)
        dexTextMod = view.findViewById(R.id.editTextNumberDexterityMod)
        constitutionText = view.findViewById(R.id.editTextNumberConstitution)
        constitutionTextMod = view.findViewById(R.id.editTextNumberConstitutionMod)
        intText = view.findViewById(R.id.editTextNumberIntelligence)
        intTextMod = view.findViewById(R.id.editTextNumberIntelligenceMod)
        wisText = view.findViewById(R.id.editTextNumberWisdom)
        wisTextMod = view.findViewById(R.id.editTextNumberWisdomMod)
        charismaText = view.findViewById(R.id.editTextNumberCharisma)
        charismaTextMod = view.findViewById(R.id.editTextNumberCharismaMod)

        dbHelper = context?.let { DatabaseHelper(it) }!!

        nextToolbar = view.findViewById(R.id.toolbarNextStats)

        if(statsFragmentListener != null){
            addCharacterLogic()
        }
        else{
            updateCharacterLogic()
        }

        return view
    }

    private fun updateCharacterLogic() {
        rollButton.isEnabled = false
        nextToolbar.inflateMenu(R.menu.update_menu)

        nextToolbar.setOnMenuItemClickListener {
                item ->
            when (item.itemId) {
                R.id.update -> {
                    lifecycleScope.launch(Dispatchers.IO){
                        dbHelper.updateStats(statId,
                            strengthText.text.toString().toIntOrNull() ?: -1,
                            strengthTextMod.text.toString().toIntOrNull() ?: -1,
                            dexText.text.toString().toIntOrNull() ?: -1,
                            dexTextMod.text.toString().toIntOrNull() ?: -1,
                            constitutionText.text.toString().toIntOrNull() ?: -1,
                            constitutionTextMod.text.toString().toIntOrNull() ?: -1,
                            intText.text.toString().toIntOrNull() ?: -1,
                            intTextMod.text.toString().toIntOrNull() ?: -1,
                            wisText.text.toString().toIntOrNull() ?: -1,
                            wisTextMod.text.toString().toIntOrNull() ?: -1,
                            charismaText.text.toString().toIntOrNull() ?: -1,
                            charismaTextMod.text.toString().toIntOrNull() ?: -1)
                    }
                    true
                }
                R.id.add ->
                {
                    Toast.makeText(context, "All stats on page", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.clear ->{
                    Toast.makeText(context, "All stats on page", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        val cursor = dbHelper.getStats(charID)

        if (cursor.moveToFirst())
        {
            val index = cursor.getColumnIndex("stat_id")
            statId = cursor.getInt(index)

            val strIndex = cursor.getColumnIndex("strength")
            val strModIndex = cursor.getColumnIndex("strength_modifier")
            val dexIndex = cursor.getColumnIndex("dexterity")
            val dexModIndex = cursor.getColumnIndex("dexterity_modifier")
            val consIndex = cursor.getColumnIndex("constitution")
            val consModIndex = cursor.getColumnIndex("constitution_modifier")
            val intIndex = cursor.getColumnIndex("intelligence")
            val intModIndex = cursor.getColumnIndex("intelligence_modifier")
            val wisIndex = cursor.getColumnIndex("wisdom")
            val wisModIndex = cursor.getColumnIndex("wisdom_modifier")
            val charIndex = cursor.getColumnIndex("charisma")
            val charModIndex = cursor.getColumnIndex("charisma_modifier")

            strengthText.setText(cursor.getInt(strIndex).toString())
            strengthTextMod.setText(cursor.getInt(strModIndex).toString())

            dexText.setText(cursor.getInt(dexIndex).toString())
            dexTextMod.setText(cursor.getInt(dexModIndex).toString())

            constitutionText.setText(cursor.getInt(consIndex).toString())
            constitutionTextMod.setText(cursor.getInt(consModIndex).toString())

            intText.setText(cursor.getInt(intIndex).toString())
            intTextMod.setText(cursor.getInt(intModIndex).toString())

            wisText.setText(cursor.getInt(wisIndex).toString())
            wisTextMod.setText(cursor.getInt(wisModIndex).toString())

            charismaText.setText(cursor.getInt(charIndex).toString())
            charismaTextMod.setText(cursor.getInt(charModIndex).toString())

        }
    }

    private fun addCharacterLogic() {
        nextToolbar.inflateMenu(R.menu.next_menu)
        nextToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.next ->
                {
                    lifecycleScope.launch(Dispatchers.IO) {
                        dbHelper.insertStats(
                            charID,
                            strengthText.text.toString().toIntOrNull() ?: -1,
                            strengthTextMod.text.toString().toIntOrNull() ?: -1,
                            dexText.text.toString().toIntOrNull() ?: -1,
                            dexTextMod.text.toString().toIntOrNull() ?: -1,
                            constitutionText.text.toString().toIntOrNull() ?: -1,
                            constitutionTextMod.text.toString().toIntOrNull() ?: -1,
                            intText.text.toString().toIntOrNull() ?: -1,
                            intTextMod.text.toString().toIntOrNull() ?: -1,
                            wisText.text.toString().toIntOrNull() ?: -1,
                            wisTextMod.text.toString().toIntOrNull() ?: -1,
                            charismaText.text.toString().toIntOrNull() ?: -1,
                            charismaTextMod.text.toString().toIntOrNull() ?: -1
                        )
                    }
                    statsFragmentListener?.nextFragmentAfterStats()
                    true
                }

                else -> false
            }
        }

        rollButton.setOnClickListener{roll()}
    }

    private fun roll() {
        showDiceRollAlert()

        val scores = mutableListOf<Int>()
        for (i in 1..6)
        {
            val diceList = mutableListOf<Int>()
            diceList.add(Random.nextInt(1,7))
            diceList.add(Random.nextInt(1,7))
            diceList.add(Random.nextInt(1,7))
            diceList.add(Random.nextInt(1,7))

            diceList.sortDescending()

            scores.add(diceList[0]+diceList[1]+diceList[2])
        }

        strengthText.setText(scores[0].toString())
        dexText.setText(scores[1].toString())
        constitutionText.setText(scores[2].toString())
        intText.setText(scores[3].toString())
        wisText.setText(scores[4].toString())
        charismaText.setText(scores[5].toString())
    }

    private fun showDiceRollAlert() {
        val dialogView = layoutInflater.inflate(R.layout.dice_layout, null)

        val diceRollView = dialogView.findViewById<LottieAnimationView>(R.id.diceRollView)

        diceRollView.playAnimation()

        AlertDialog.Builder(this@StatsFragment.context)
            .setTitle("Rolling the Dice...")
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onAnimationStart(animation: Animator) {
        TODO("Not yet implemented")
    }

    override fun onAnimationEnd(animation: Animator) {

    }

    override fun onAnimationCancel(animation: Animator) {
        TODO("Not yet implemented")
    }

    override fun onAnimationRepeat(animation: Animator) {
        TODO("Not yet implemented")
    }
}