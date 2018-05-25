package com.enshaedn.ad340.enshaedn340k

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*

const val PASS_PHRASE = "com.enshaedn.ad340.enshaedn340k.MESSAGE"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val msg = "AD340K by Enshaedn: "
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolBar))

        sharedPref = getPreferences(Context.MODE_PRIVATE)
        val pEdit = findViewById<EditText>(R.id.editText)
        val storedPhrase = sharedPref.getString("phraseInput", "")
        if(storedPhrase.isNotEmpty()) {
            pEdit.setText(storedPhrase)
        } else {
            pEdit.setHint("Enter a message")
        }

        val actBar: ActionBar? = supportActionBar
        actBar?.setDisplayHomeAsUpEnabled(true)
        actBar?.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)

        mDrawerLayout = findViewById(R.id.navDrawer)

        val navigationView: NavigationView = findViewById(R.id.navView)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.phrase)?.setVisible(false)
        menu?.findItem(R.id.zlist)?.setVisible(false)
        menu?.findItem(R.id.zDetail)?.setVisible(false)
        menu?.findItem(R.id.about)?.setVisible(false)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_about -> {
                val aboutIntent = Intent(this, aboutApp::class.java)
                startActivity(aboutIntent)
            }

            R.id.nav_movie_list -> {
                if(isOnline()) {
                    val zIntent = Intent(this, zombieList::class.java)
                    startActivity(zIntent)
                } else {
                    Toast.makeText(applicationContext, "Internet Not Available", tDur).show()
                }
            }

            R.id.nav_cam_list -> {
                if(isOnline()) {
                    val zIntent = Intent(this, cameraList::class.java)
                    startActivity(zIntent)
                } else {
                    Toast.makeText(applicationContext, "Internet Not Available", tDur).show()
                }
            }
        }

        mDrawerLayout.closeDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //called when the user presses the 'Click Me!' button
    fun sendPhrase(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        val phrase = editText.text.toString()

        if(phraseCheck(phrase)) {
            val editor = sharedPref.edit()
            editor.putString("phraseInput", phrase)
            editor.commit()

            val intent = Intent(this, DisplayPhraseActivity::class.java).apply {
                putExtra(PASS_PHRASE, phrase)
            }
            startActivity(intent)
        } else {
            Toast.makeText(applicationContext, "You must enter something", tDur).show()
        }
    }

    //kind of pointless function, but had to separate it out to be able to test validation
    fun phraseCheck(phrase: String): Boolean {
        return phrase.isNotEmpty()
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

            R.id.search -> {
                Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show()
                return true
            }

            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    //called when the user presses the '1' button
    fun zombieClick(view: View) {
        val gridb1 = findViewById<Button>(R.id.gridb1)
        if(isOnline()) {
            val zIntent = Intent(this, zombieList::class.java)
            startActivity(zIntent)
        } else {
            Toast.makeText(applicationContext, "Internet Not Available", tDur).show()
        }
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

    fun isOnline(): Boolean {
        val connectivityManager: ConnectivityManager? = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo: NetworkInfo? = connectivityManager?.activeNetworkInfo
        return (networkInfo != null && networkInfo.isConnected)
    }
}