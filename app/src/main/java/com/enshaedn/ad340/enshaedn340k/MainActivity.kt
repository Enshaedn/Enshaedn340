package com.enshaedn.ad340.enshaedn340k

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast

const val PASS_PHRASE = "com.enshaedn.ad340.enshaedn340k.MESSAGE"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //called when the user presses the 'Click Me!' button
    fun sendPhrase(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        val phrase = editText.text.toString()
        val intent = Intent(this, DisplayPhraseActivity::class.java).apply {
            putExtra(PASS_PHRASE, phrase)
        }
        startActivity(intent)
    }
    //toast duration
    private val tDur = Toast.LENGTH_SHORT
    /*
    fun gridClicked(view: View, button: Button) {
        when(button.id) {
            gridb2.id -> Toast.makeText(applicationContext, "Button 2 clicked!", tDur).show()
            gridb3.id -> Toast.makeText(applicationContext, "Button 3 clicked!", tDur).show()
            gridb4.id -> Toast.makeText(applicationContext, "Button 4 clicked!", tDur).show()
        }
    }*/

    //toast message for button 2
    fun gridB2Clicked(view: View) {
        Toast.makeText(applicationContext, "Button 2 clicked!", tDur).show()
    }

    //toast message for button 3
    fun gridB3Clicked(view: View) {
        Toast.makeText(applicationContext, "Button 3 clicked!", tDur).show()
    }

    //toast message for button 4
    fun gridB4Clicked(view: View) {
        Toast.makeText(applicationContext, "Button 4 clicked!", tDur).show()
    }
}