package com.example.listapp.GUI

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listapp.Model.Friends
import com.example.listapp.Model.RecycleAdapter
import com.example.listapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cell.view.*

class MainActivity : AppCompatActivity() {

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

        val friends = Friends().getAll()
        val friendAdapter = RecycleAdapter(friends)
        recycler.adapter = friendAdapter


        //Sets the on click listener
        //When a persons row is clicked the isFavorite boolean will be set to the opposite value.
        //The picture showing if a person is a favorite will also be changed.
        friendAdapter.itemClickListener = { position, friends ->

            friends.isFavorite = !friends.isFavorite

            //The view needs to be found from the layoutmanager, because if you do:
            //recycler[position] it refers to the viewgroups childcount which isn't the same as
            //the itemcount. Therefore it will cause an out of bound exception.
            (recycler.layoutManager as LinearLayoutManager).findViewByPosition(position)?.imgBtnIsFav?.setImageResource(
                if (friends.isFavorite) R.drawable.ok else R.drawable.notok)
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

    //Clears the search field, sets it as iconified (Like not clicked on yet)
    //Selects all friends on the spinner
    fun onClickClear(view: View) {
        swSearch.setQuery("", false)
        swSearch.isIconified = true

        spinFilter.setSelection(0)
    }
}