package com.enshaedn.ad340.enshaedn340k

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class cameraList : AppCompatActivity() {
    private val msg = "AD340K by Enshaedn: "
    private lateinit var trafficCamList: RecyclerView
    private lateinit var trafficCamAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var tcList: ArrayList<trafficCam>
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_list)

        viewManager = LinearLayoutManager(this)
        trafficCamAdapter = camAdapter(tcList,this)

        trafficCamList = findViewById<RecyclerView>(R.id.cameraRecycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = trafficCamAdapter
        }

        //set toolbar
        setSupportActionBar(findViewById(R.id.toolBar))

        //add up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        requestQueue = Volley.newRequestQueue(this)
        buildCamList()
    }

    fun buildCamList() {
        val url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    val jsonArr: JSONArray = response.getJSONArray("Features")
                    //Log.d(msg, jsonArr.toString())
                    for (i in jsonArr.iterator()) {
                        val pCoord = i
                        val cameras = i.getJSONArray("Cameras")
                        //Log.d(msg, pCoord.getString("PointCoordinate"))
                        //Log.d(msg, cameras.toString())
                        for (cam in cameras.iterator()) {
                            val id = cam.getString("Id")
                            val desc = cam.getString("Description")
                            var image = cam.getString("ImageUrl")
                            val type = cam.getString("Type")
                            if(type.equals("sdot")) {
                                image = "http://www.seattle.gov/trafficcams/images/" + image
                            } else {
                                image = "http://images.wsdot.wa.gov/nw/" + image
                            }
                            //Log.d(msg, image)
                            tcList.add(trafficCam(1.1, 1.1, id, desc, image, type))
                        }
                    }

                },
                Response.ErrorListener { error ->  }
        )

        requestQueue.add(request)
    }

    operator fun JSONArray.iterator(): Iterator<JSONObject> = (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()
}

//adapter class to create ViewHolders for each row (view) within the recycler
class camAdapter(private val myDataset: ArrayList<trafficCam>, private val tcContext: cameraList): RecyclerView.Adapter<camAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        //val cView: CardView = view.findViewById(R.id.tcCardView)
        val tView: TextView = view.findViewById(R.id.cam_type)
        val iView: ImageView = view.findViewById(R.id.cam_view)
        //val cLayout: ConstraintLayout = view.findViewById(R.id.tcLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): camAdapter.ViewHolder {
        val view: View = LayoutInflater.from(tcContext).inflate(R.layout.traffic_cam_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tView.text = myDataset[position].desc

    }

    override fun getItemCount() = myDataset.size
}