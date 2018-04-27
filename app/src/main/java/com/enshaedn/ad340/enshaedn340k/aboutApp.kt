package com.enshaedn.ad340.enshaedn340k

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class aboutApp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

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
}