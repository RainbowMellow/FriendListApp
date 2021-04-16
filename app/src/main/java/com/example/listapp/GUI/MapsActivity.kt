package com.example.listapp.GUI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.listapp.Model.Friend
import com.example.listapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var friends = ArrayList<Friend>()
    private var currentLocation = LatLng(0.0, 0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (intent.extras != null) {
            val extras: Bundle = intent.extras!!
            val friendList = extras.getSerializable("friendList") as ArrayList<Friend>
            val lat = extras.getDouble("lat")
            val lng = extras.getDouble("lng")

            currentLocation = LatLng(lat, lng)

            friends = friendList
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        for(friend in friends)
        {
            if(friend.latitude != null && friend.longitude != null)
            {
                val location = LatLng(friend.latitude!!, friend.longitude!!)
                mMap.addMarker(MarkerOptions().position(location).title("Location Of " + friend.name))
            }
        }

        mMap.addMarker(MarkerOptions().position(currentLocation).title("My Current Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
    }
}