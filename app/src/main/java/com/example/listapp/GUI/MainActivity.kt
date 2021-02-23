package com.example.listapp.GUI

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
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

        val recycler = findViewById<RecyclerView>(R.id.recyclerView)

        recycler.layoutManager = LinearLayoutManager(this)

        recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        recycler.setHasFixedSize(true)

        val friends = Friends().getAll()
        val friendAdapter = RecycleAdapter(friends)
        recycler.adapter = friendAdapter


        println((recycler.layoutManager as LinearLayoutManager).itemCount.toString())

        friendAdapter.itemClickListener = { position, friends ->

            friends.isFavorite = !friends.isFavorite
            (recycler.layoutManager as LinearLayoutManager).findViewByPosition(position)?.imgBtnIsFav?.setImageResource(
                if (friends.isFavorite) R.drawable.ok else R.drawable.notok)

            println(position)
            println(friends.name)
        }

        swSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                println("Got query: " + newText)
                (recycler.adapter as RecycleAdapter).filter.filter(newText)
                return false
            }
        })


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

    fun onClickClear(view: View) {
        swSearch.setQuery("", false)
        swSearch.isIconified = true

        spinFilter.setSelection(0)
    }
}