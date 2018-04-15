package com.enshaedn.ad340.enshaedn340k

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText

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
}