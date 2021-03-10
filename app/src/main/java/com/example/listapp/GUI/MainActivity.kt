package com.example.listapp.GUI

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listapp.Model.BEFriend
import com.example.listapp.Model.Friends
import com.example.listapp.Model.RecycleAdapter
import com.example.listapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cell.view.*
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    lateinit var friendAdapter: RecycleAdapter

    var friends = Friends().getAll()

    val CREATE_FRIEND = 1
    val DELETE_FRIEND = 2
    val UPDATE_FRIEND = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

            println(position)
            println(friend.name)

            intent.putExtra("friend", friend)
            intent.putExtra("isCreate", false)
            startActivityForResult(intent, 1)


        // region Code that makes the friend = !friend.favorite when clicked
            //    friends.isFavorite = !friends.isFavorite

                //The view needs to be found from the layoutmanager, because if you do:
                //recycler[position] it refers to the viewgroups childcount which isn't the same as
                //the itemcount. Therefore it will cause an out of bound exception.

            //    (recycler.layoutManager as LinearLayoutManager).findViewByPosition(position)?.imgBtnIsFav?.setImageResource(
            //        if (friends.isFavorite) R.drawable.ok else R.drawable.notok)
        //endregion

        }

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
        })


        //Setup for the spinner
        val spinner = spinFilter
        val filter = resources.getStringArray(R.array.filter)

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
                    1 -> {
                        (recycler.adapter as RecycleAdapter).getFavorites()
                    }
                    2 -> {
                        (recycler.adapter as RecycleAdapter).getNonFavorites()
                    }
                }
            }
        }

    }

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



    //Clears the search field, sets it as iconified (Like not clicked on yet)
    //Selects all friends on the spinner
    fun onClickClear(view: View) {
        swSearch.setQuery("", false)
        swSearch.isIconified = true

        spinFilter.setSelection(0)
    }

    fun onClickCreate(view: View) {
        //Opens the detailview with the detailactivity
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("isCreate", true)
        startActivityForResult(intent, 1)
    }
}