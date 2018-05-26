package com.enshaedn.ad340.enshaedn340k

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.JsonReader
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import java.io.InputStream
import java.io.InputStreamReader

class zombieList : AppCompatActivity() {
    private lateinit var zRecycler: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zombie_list)

        //open/set JSON file data to a variable
        val zFile = application.resources.openRawResource (R.raw.zombie_movies)
        val zList = readStream(zFile)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(zList, this)

        zRecycler = findViewById<RecyclerView>(R.id.zombieRecycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        //set toolbar
        setSupportActionBar(findViewById(R.id.toolBar))

        //add up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.phrase)?.setVisible(false)
        menu?.findItem(R.id.about)?.setVisible(false)
        menu?.findItem(R.id.zDetail)?.setVisible(false)
        menu?.findItem(R.id.sdot)?.setVisible(false)
        menu?.findItem(R.id.wsdot)?.setVisible(false)
        menu?.findItem(R.id.all)?.setVisible(false)
        return true
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

            R.id.zlist -> {
                Toast.makeText(this, "Sort", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    //pass JSON file through InputStream - read file
    private fun readStream(zFile: InputStream): ArrayList<zMovie> {
        val zJSON = JsonReader(InputStreamReader(zFile))
        return readZJSON(zJSON)
    }

    //read each JSON line and return array of zMovie objects
    private fun readZJSON(zJSON: JsonReader): ArrayList<zMovie> {
        val movies =  ArrayList<zMovie>()
        zJSON.beginArray()
        while (zJSON.hasNext()) {
            movies.add(getMovie(zJSON))
        }
        zJSON.endArray()
        return movies
    }

    //creates the zMovie objects to send back to the readZJSON function
    private fun getMovie(zJSON: JsonReader): zMovie {
        var title = ""
        var year = 0
        var director = "name"
        var image = "imgLoc"
        var description = "temp"

        zJSON.beginObject()
        while(zJSON.hasNext()) {
            val name: String =  zJSON.nextName()
            when(name) {
                "title" -> title = zJSON.nextString()
                "year" -> year = zJSON.nextInt()
                "director" -> director = zJSON.nextString()
                "image" -> image = zJSON.nextString()
                "description" -> description = zJSON.nextString()
                else -> zJSON.skipValue()
            }
        }
        zJSON.endObject()
        return zMovie(title, year, director, image, description)
    }
}

//class to create movie objects based on the JSON object passed through
class zMovie(title: String, year: Int, director: String, image: String, description: String): Parcelable {
    val title = title
    val year = year
    val director = director
    val image = image
    val description = description

    constructor(parcel: Parcel): this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeInt(year)
        parcel.writeString(director)
        parcel.writeString(image)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<zMovie> {
        override fun createFromParcel(parcel: Parcel): zMovie {
            return zMovie(parcel)
        }

        override fun newArray(size: Int): Array<zMovie?> {
            return arrayOfNulls(size)
        }
    }
}


//adapter class to create ViewHolders for each row (view) within the recycler
class MyAdapter(private val myDataset: ArrayList<zMovie>, private val zContext: zombieList): RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val tView: TextView = view.findViewById(R.id.rowTitle)
        val yView: TextView = view.findViewById(R.id.rowYear)
        val rLayout: RelativeLayout = view.findViewById(R.id.zRowParent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val view: View = LayoutInflater.from(zContext).inflate(R.layout.zrow, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tView.text = myDataset[position].title
        holder.yView.text = myDataset[position].year.toString()
        holder.view.setOnClickListener {
            val zIntent = Intent(zContext, displayMovie::class.java).apply {
                putExtra("zSelect", myDataset[position])
            }
            zContext.startActivity(zIntent)
        }
    }

    override fun getItemCount() = myDataset.size
}