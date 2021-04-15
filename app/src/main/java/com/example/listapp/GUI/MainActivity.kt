package com.example.listapp.GUI

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listapp.Model.BEFriend
import com.example.listapp.Model.Friends
import com.example.listapp.Model.RecycleAdapter
import com.example.listapp.R
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cell.view.*
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    lateinit var friendAdapter: RecycleAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    var friends = Friends().getAll()
    var currentLocation = Pair(0.0, 0.0)

    val CREATE_FRIEND = 1
    val DELETE_FRIEND = 2
    val UPDATE_FRIEND = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()
        startListening()
        getLocation()

        //Find the RecyclerView and make a reference to it
        val recycler = findViewById<RecyclerView>(R.id.recyclerView)

        //Setting the recyclers layoutmanager to be a LinearLayoutManager
        recycler.layoutManager = LinearLayoutManager(this)

        //Adding the lines in between the rows
        recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        //Sets the items in the recycler to have a fixed size
        recycler.setHasFixedSize(true)
        friendAdapter = RecycleAdapter(friends)
        recycler.adapter = friendAdapter


        friendAdapter.itemClickListener = { position, chosenFriend ->

            //Opens the detailview with the detailactivity
            val intent = Intent(this, DetailActivity::class.java)
            val friend = friends[position]

            val lat: Double = currentLocation.first
            val lng: Double = currentLocation.second

            intent.putExtra("friend", friend)
            intent.putExtra("isCreate", false)
            intent.putExtra("lat", lat)
            intent.putExtra("lng", lng)

            startActivityForResult(intent, 1)
        }

        // region Unused filtering methods from before
/*
        //Sets a listener on the searchView.
        //Activates when text is written in the search bar
        swSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            //Filters the list
            override fun onQueryTextChange(newText: String?): Boolean {
                (recycler.adapter as RecycleAdapter).filter.filter(newText)
                return false
            }
        })*/


        //Setup for the spinner
        // val spinner = spinFilter
        /*val filter = resources.getStringArray(R.array.filter)

        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filter)
            spinner.adapter = adapter
        }

        //Setting up the listener on the spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        (recycler.adapter as RecycleAdapter).getAll()
                    }
                }
            }
        }
*/
    // endregion

    }

    // region Menu

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.getItemId()


        when (id) {
            R.id.action_new -> {
                //Opens the detailview with the detailactivity
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("isCreate", true)
                startActivityForResult(intent, 1)
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // endregion

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1)
        {
            if(resultCode == CREATE_FRIEND)
            {
                val friend = data?.extras?.getSerializable("friend") as BEFriend
                friendAdapter.addFriend(friend)
                friends = friendAdapter.getList()
            }

            if(resultCode == DELETE_FRIEND)
            {
                val friend = data?.extras?.getSerializable("chosenFriend") as BEFriend
                friendAdapter.deleteFriend(friend)
                friends = friendAdapter.getList()
            }

            if(resultCode == UPDATE_FRIEND)
            {
                val friend = data?.extras?.getSerializable("friend") as BEFriend
                val chosenFriend = data.extras!!.getSerializable("chosenFriend") as BEFriend
                friendAdapter.editFriend(friend, chosenFriend)
                friends = friendAdapter.getList()
            }
        }
    }

    // region Unused Clear Search field method

    //Clears the search field, sets it as iconified (Like not clicked on yet)
    //Selects all friends on the spinner
    // fun onClickClear(view: View) {
     //   swSearch.setQuery("", false)
       // swSearch.isIconified = true

    //    spinFilter.setSelection(0)
    //}

    // endregion

    // region Get Current Location

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    private fun requestPermissions() {
        if (!isPermissionGiven()) {
            println("permission denied to USE GPS - requesting it")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(permissions, 1)
        } else
            println("permission to USE GPS granted!")
            //startListening()
            getLocation()
    }

    private fun isPermissionGiven(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return permissions.all { p -> checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED}
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        if (!isPermissionGiven()) {
            println("No permission given")
            return
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        // The type of location is Location? - it can be null... handle cases

        if (location != null) {
            println("Location = ${location.latitude}, ${location.longitude}")
            currentLocation = Pair(location.altitude, location.longitude)

            startListening()
        } else
            println("Location = null")
    }

    var myLocationListener: LocationListener? = null

    @SuppressLint("MissingPermission")
    private fun startListening() {
        if (!isPermissionGiven())
            return

        if (myLocationListener == null)
            myLocationListener = object : LocationListener {
                var count: Int = 0

                override fun onLocationChanged(location: Location) {
                    count++
                    println("Location changed")
                    println("Location = ${location.latitude}, ${location.longitude}")

                    currentLocation = Pair(location.latitude, location.longitude)
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                }
            }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            0,
            0.0F,
            myLocationListener!!)

    }

    private fun stopListening() {

        if (myLocationListener == null) return

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(myLocationListener!!)
    }

    override fun onStop(){
        stopListening()
        super.onStop()
    }

    // endregion
}