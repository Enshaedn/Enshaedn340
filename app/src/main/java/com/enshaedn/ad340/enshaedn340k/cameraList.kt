package com.enshaedn.ad340.enshaedn340k

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class cameraList : AppCompatActivity() {
    private lateinit var trafficCamList: RecyclerView
    private lateinit var trafficCamAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    //private var tCams: MutableList<trafficCam> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_list)

        //set toolbar
        setSupportActionBar(findViewById(R.id.toolBar))

        //add up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        buildCams(this).execute("https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2")
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.phrase)?.setVisible(false)
        menu?.findItem(R.id.about)?.setVisible(false)
        menu?.findItem(R.id.zDetail)?.setVisible(false)
        menu?.findItem(R.id.zlist)?.setVisible(false)
        menu?.findItem(R.id.search)?.setVisible(false)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ad340app, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //val filterCams: MutableList<trafficCam> = mutableListOf()
        when(item.itemId) {
            R.id.menu_settings -> {
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                return true
            }

            /*R.id.sdot -> {
                filterCams.clear()
                for (i in 0..(tCams.size - 1)) {
                    if(tCams[i].type.equals("sdot")) {
                        filterCams.add(tCams[i])
                    }
                }
                camAdapter(filterCams, this).refreshRows(filterCams)
                trafficCamAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Only SDOT Displayed", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.wsdot -> {
                filterCams.clear()
                for (i in 0..(tCams.size - 1)) {
                    if(tCams[i].type.equals("wsdot")) {
                        filterCams.add(tCams[i])
                    }
                }
                camAdapter(filterCams, this).refreshRows(filterCams)
                trafficCamAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Only WSDOT Displayed", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.all -> {
                buildCams(this).execute("https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2")
                Toast.makeText(this, "Both Displayed", Toast.LENGTH_SHORT).show()
                return true
            }*/

            else -> return super.onOptionsItemSelected(item)
        }
    }

    /*fun setCamList(list: MutableList<trafficCam>) {
        this.tCams = list
    }*/

    private inner class buildCams(var tcContext: cameraList): AsyncTask<String, String, String>() {

        override fun doInBackground(vararg params: String?): String {
            var connection: HttpsURLConnection? = null
            var readJSON: BufferedReader? = null

            try {
                val url = URL(params[0])
                connection = url.openConnection() as HttpsURLConnection
                connection.connect()

                readJSON = BufferedReader(InputStreamReader(connection.inputStream))

                return readJSON.readLine()
            } catch(e: MalformedURLException) {
                e.printStackTrace()
            } catch(e: IOException) {
                e.printStackTrace()
            } finally {
                if(connection != null) {
                    connection.disconnect()
                }
                try {
                    if(readJSON != null) {
                        readJSON.close()
                    }
                } catch(e: IOException) {
                    e.printStackTrace()
                }
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var coords = ""
            var id = ""
            var desc = ""
            var image = ""
            var type = ""
            val tcList: MutableList<trafficCam> = mutableListOf()

            val jObj = JSONObject(result)
            val jArray = jObj.getJSONArray("Features")

            for(i in 0..(jArray.length() - 1)) {
                val list = JSONObject(jArray[i].toString())
                val count = JSONArray(list.getString("Cameras")).length() - 1
                val camera = JSONArray(list.getString("Cameras"))

                for(j in 0..count) {
                    coords = JSONObject(jArray[i].toString()).getString("PointCoordinate")
                    id = camera.getJSONObject(j).getString("Id")
                    desc = camera.getJSONObject(j).getString("Description")
                    image = camera.getJSONObject(j).getString("ImageUrl")
                    type = camera.getJSONObject(j).getString("Type")

                    if(type.equals("sdot")) {
                        image = "http://www.seattle.gov/trafficcams/images/" + image
                    } else {
                        image = "http://images.wsdot.wa.gov/nw/" + image
                    }

                    tcList.add(trafficCam(coords, id, desc, image, type))
                }
            }
            viewManager = LinearLayoutManager(tcContext)
            trafficCamAdapter = camAdapter(tcList,tcContext)

            trafficCamList = findViewById<RecyclerView>(R.id.cameraRecycler).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = trafficCamAdapter
            }

            //setCamList(tcList)
        }
    }
}

//adapter class to create ViewHolders for each row (view) within the recycler
class camAdapter(private val myDataset: MutableList<trafficCam>, private val tcContext: cameraList): RecyclerView.Adapter<camAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val tView: TextView = view.findViewById(R.id.cam_type)
        val iView: ImageView = view.findViewById(R.id.cam_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): camAdapter.ViewHolder {
        val view: View = LayoutInflater.from(tcContext).inflate(R.layout.traffic_cam_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("**************", myDataset[position].id)
        Log.d("**************", myDataset[position].desc)
        Log.d("**************", myDataset[position].image)
        holder.tView.text = myDataset[position].desc + " (" + myDataset[position].type.toUpperCase() + ")"
        DownloadImageTask(holder.iView).execute(myDataset[position].image)
    }

    /*fun refreshRows(fCams: MutableList<trafficCam>) {
        this.myDataset.clear()
        this.myDataset.addAll(fCams)
        notifyDataSetChanged()
    }*/

    override fun getItemCount() = myDataset.size
}