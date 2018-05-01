package com.enshaedn.ad340.enshaedn340k

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.activity_display_movie.*

class displayMovie : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_movie)

        //get the data in JSON format from the intent passed from the zombieList
        val zData = intent.getParcelableExtra<zMovie>("zSelect")

        //create variables for the view objects in the displayMovie activity layout
        DownloadImageTask(findViewById(R.id.picView)).execute(zData.image)
        val picView = findViewById<ImageView>(R.id.picView)
        val titleView = findViewById<TextView>(R.id.dispTitle)
        val dirView = findViewById<TextView>(R.id.dispDir)
        val yearView = findViewById<TextView>(R.id.dispYear)
        val descView = findViewById<TextView>(R.id.dispDesc)

        //populate the views with their respective data
        picView.contentDescription = zData.title
        titleView.text = zData.title
        dirView.append(zData.director)
        yearView.append(zData.year.toString())
        descView.text = zData.description
        descView.movementMethod = ScrollingMovementMethod()

        //set toolbar
        setSupportActionBar(findViewById(R.id.toolBar))

        //add up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ad340app, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_settings -> {
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}

//class to download the images from their URLs
private class DownloadImageTask(internal var bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {
    override fun doInBackground(vararg urls: String): Bitmap? {
        val urlDisp = urls[0]
        var zPic: Bitmap? = null

        try {
            val pic = java.net.URL(urlDisp).openStream()
            zPic = BitmapFactory.decodeStream(pic)
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return zPic
    }

    override fun onPostExecute(result: Bitmap) {
        bmImage.setImageBitmap(result)
    }
}