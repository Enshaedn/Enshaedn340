package com.enshaedn.ad340.enshaedn340k

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_display_phrase.*

class DisplayPhraseActivity : AppCompatActivity() {
    private val msg = "AD340K by Enshaedn: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_phrase)

        //get the Intent that started this activity and extract the string
        val message = intent.getStringExtra(PASS_PHRASE)

        //capture the layout's TextView and set the string as its text
        textView3.text = message.toString()

        Log.d(msg,"Second Activity Created (onCreate called)")

        //set toolbar
        setSupportActionBar(findViewById(R.id.toolBar))

        //add up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ad340app, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        Log.d(msg, "Second Activity Started (onStart called)")
    }

    override fun onResume() {
        super.onResume()
        Log.d(msg, "Second Activity Resumed (onResume called)")
    }

    override fun onPause() {
        super.onPause()
        Log.d(msg, "Second Activity Paused (onPause called)")
    }

    override fun onStop() {
        super.onStop()
        Log.d(msg, "Second Activity Stopped (onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(msg, "Second Activity Destroyed (onDestroy called)")
    }
}