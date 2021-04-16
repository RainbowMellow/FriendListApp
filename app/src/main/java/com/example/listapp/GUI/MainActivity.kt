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
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.InvalidationTracker
import com.example.listapp.Database.FriendRepositoryInDB
import com.example.listapp.Model.Friend
import com.example.listapp.Model.RecycleAdapter
import com.example.listapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cell.view.*
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    lateinit var friendAdapter: RecycleAdapter

    lateinit var repo: FriendRepositoryInDB


    /**
     * Variable that stores the current location.
     * Changes when the location changes.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    var currentLocation = Pair(0.0, 0.0)

    /**
     * Values that are used as resultcodes.
     */
    val CREATE_FRIEND = 1
    val DELETE_FRIEND = 2
    val UPDATE_FRIEND = 3

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialization of repository
        FriendRepositoryInDB.initialize(this)
        repo = FriendRepositoryInDB.get()

        //insertTestData()

        handleRecycler()

        requestPermissions()
        startListening()
        getLocation()

    }

    /**
     * Handles the recycler and gives it the information it needs.
     */
    fun handleRecycler()
    {
        // Find the RecyclerView and make a reference to it
        val recycler = findViewById<RecyclerView>(R.id.recyclerView)

        // Setting the recyclers layoutmanager to be a LinearLayoutManager
        recycler.layoutManager = LinearLayoutManager(this)

        // Adding the lines in between the rows
        recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        // Sets the items in the recycler to have a fixed size
        recycler.setHasFixedSize(true)


        // Gets the list of friends from the repository
        // and converts it to a list the recycler can use.
        val friendsLiveData = repo.getAll()

        friendsLiveData.observe(
            this,
            Observer { friendList ->
                friendList?.let {
                    val list = ArrayList(friendList)
                    friendAdapter = RecycleAdapter(list)
                    recycler.adapter = friendAdapter

                    // Sets the itemClickListener.
                    friendAdapter.itemClickListener = { position, chosenFriend ->

                        //Opens the detailview with the detailactivity
                        val intent = Intent(this, DetailActivity::class.java)
                        val friend = list[position]

                        val lat: Double = currentLocation.first
                        val lng: Double = currentLocation.second

                        intent.putExtra("friend", friend)
                        intent.putExtra("isCreate", false)
                        intent.putExtra("lat", lat)
                        intent.putExtra("lng", lng)

                        startActivityForResult(intent, 1)
                    }
                }
            }
        )
    }


    /**
     * Inserts test data into the database.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertTestData() {
        val mRep = FriendRepositoryInDB.get()

        mRep.insert(Friend(id = 0, name = "Emyle Chowne", phone = "51887794", address = "461 Crest Line Junction", email = "email.email@hotmail.com", url = "http://www.easv.dk", latitude = 39.421998333333335, longitude = -130.084, birthday = LocalDate.now(), picture = ""))
    }


    // region Menu

    /**
     * Creates the menu.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;

    }

    /**
     * Sets the action that will happen if the user clicks on the menu item.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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

    /**
     * Gets a result from the DetailActivity and calls the appropriate method in the repository.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1)
        {
            if(resultCode == CREATE_FRIEND)
            {
                val friend = data?.extras?.getSerializable("friend") as Friend
                repo.insert(friend)
            }

            if(resultCode == DELETE_FRIEND)
            {
                val friend = data?.extras?.getSerializable("chosenFriend") as Friend
                repo.delete(friend)
            }

            if(resultCode == UPDATE_FRIEND)
            {
                val friend = data?.extras?.getSerializable("friend") as Friend
                repo.update(friend)
            }
        }
    }

    // region Get Current Location

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    /**
     * Requests permission to use the GPS.
     */
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

    /**
     * Gets the location of the phone.
     */
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


    /**
     * Listens for changes in the location of the phone.
     */
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

    /**
     * Stops listening for changes in the location of the phone.
     */
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