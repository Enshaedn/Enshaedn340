package com.enshaedn.ad340.enshaedn340k

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.util.JsonReader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import java.io.InputStream
import java.io.InputStreamReader

class zombieList : AppCompatActivity() {
    private lateinit var zRecycler: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zombie_list)

        val zFile = application.resources.openRawResource (R.raw.zombie_movies)
        val zList = readStream(zFile)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(zList, this)

        zRecycler = findViewById<RecyclerView>(R.id.zombieRecycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun readStream(zFile: InputStream): ArrayList<zMovie> {
        val zJSON = JsonReader(InputStreamReader(zFile))
        return readZJSON(zJSON)
    }

    private fun readZJSON(zJSON: JsonReader): ArrayList<zMovie> {
        val movies =  ArrayList<zMovie>()
        zJSON.beginArray()
        while (zJSON.hasNext()) {
            movies.add(getMovie(zJSON))
        }
        zJSON.endArray()
        return movies
    }

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