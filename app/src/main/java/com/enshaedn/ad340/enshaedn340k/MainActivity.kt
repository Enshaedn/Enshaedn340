package com.enshaedn.ad340.enshaedn340k

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*

const val PASS_PHRASE = "com.enshaedn.ad340.enshaedn340k.MESSAGE"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolBar))
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ad340app, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

    //called when the user presses the '1' button
    fun zombieClick(view: View) {
        val gridb1 = findViewById<Button>(R.id.gridb1)
        val zIntent = Intent(this, zombieList::class.java)
        startActivity(zIntent)
    }

    //toast duration
    private val tDur = Toast.LENGTH_SHORT

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