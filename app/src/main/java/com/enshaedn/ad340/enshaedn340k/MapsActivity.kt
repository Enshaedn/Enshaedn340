package com.enshaedn.ad340.enshaedn340k

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.security.auth.callback.Callback

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var infoAdapter: customInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //set toolbar
        setSupportActionBar(findViewById(R.id.toolBar))

        //add up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        //blue dot showing user's location, using marker below instead
        //mMap.isMyLocationEnabled = true

        val mapSettings = mMap.uiSettings
        mapSettings.isZoomControlsEnabled = true
        mapSettings.isMyLocationButtonEnabled = true
        mapSettings.isRotateGesturesEnabled = false
        mapSettings.isMapToolbarEnabled = false

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15.0f))
            }
        }

        buildCams(this).execute("https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=17&type=2")
        infoAdapter = customInfoAdapter(this)
        mMap.setInfoWindowAdapter(infoAdapter)
    }

    private fun getAddress(latlng: LatLng): String {
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0..address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        val titleStr = getAddress(location)
        markerOptions.title(titleStr)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        mMap.addMarker(markerOptions)
    }

    private fun placeMarkerOnMap(location: LatLng, camera: trafficCam) {
        val markerOptions = MarkerOptions().position(location)
        //markerOptions.title(camera.type)
        if (camera.type.equals("sdot")) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        } else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        }
        mMap.addMarker(markerOptions).tag = camera
    }

    private inner class buildCams(var tcContext: MapsActivity) : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg params: String?): String {
            var connection: HttpsURLConnection? = null
            var readJSON: BufferedReader? = null

            try {
                val url = URL(params[0])
                connection = url.openConnection() as HttpsURLConnection
                connection.connect()

                readJSON = BufferedReader(InputStreamReader(connection.inputStream))

                return readJSON.readLine()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
                try {
                    if (readJSON != null) {
                        readJSON.close()
                    }
                } catch (e: IOException) {
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

            val jObj = JSONObject(result)
            val jArray = jObj.getJSONArray("Features")

            for (i in 0..(jArray.length() - 1)) {
                val list = JSONObject(jArray[i].toString())
                val count = JSONArray(list.getString("Cameras")).length() - 1
                val camera = JSONArray(list.getString("Cameras"))

                for (j in 0..count) {
                    coords = JSONObject(jArray[i].toString()).getString("PointCoordinate")
                    id = camera.getJSONObject(j).getString("Id")
                    desc = camera.getJSONObject(j).getString("Description")
                    image = camera.getJSONObject(j).getString("ImageUrl")
                    type = camera.getJSONObject(j).getString("Type")

                    if (type.equals("sdot")) {
                        image = "http://www.seattle.gov/trafficcams/images/" + image
                    } else {
                        image = "http://images.wsdot.wa.gov/nw/" + image
                    }

                    val lat = coords.substring(1, coords.indexOf(",")).toDouble()
                    val lng = coords.substring(coords.indexOf(",") + 1, coords.length - 1).toDouble()

                    val temp = trafficCam(coords, id, desc, image, type)

                    placeMarkerOnMap(LatLng(lat, lng), temp)
                }
            }
        }
    }
}

class customInfoAdapter(private val context: MapsActivity) : GoogleMap.InfoWindowAdapter {
    private val view: View

    init {
        view = LayoutInflater.from(context).inflate(R.layout.cam_info_window, null)
    }

    override fun getInfoWindow(p0: Marker): View? {
        return null
    }

    override fun getInfoContents(p0: Marker): View? {
        val details: TextView = view.findViewById(R.id.map_cam_text)
        val camImg: ImageView = view.findViewById(R.id.map_cam)

        if(p0.tag != null) {
            val temp = p0.tag as trafficCam

            details.text = temp.desc + " (" + temp.type.toUpperCase() + ")"

            DownloadImageTask(camImg).execute(temp.image)

            return view
        } else {
            return null
        }
    }
}