package com.example.listapp.Model

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.listapp.R
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList


class RecycleAdapter(private val friends: ArrayList<BEFriend>) : RecyclerView.Adapter<RecycleAdapter.FriendViewHolder>(),
    Filterable {

    var friendFilterList = ArrayList<BEFriend>()
    var listOfFilteredFriends = ArrayList<BEFriend>()

    init {
        friendFilterList = friends
        listOfFilteredFriends = friendFilterList
    }

    var itemClickListener: ((position: Int, friend: BEFriend) -> Unit)? = null


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    friendFilterList = friends
                    listOfFilteredFriends = friendFilterList
                }
                else {
                    val resultList = ArrayList<BEFriend>()
                    for (row in friends) {
                        if(row.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT).trim()) ||
                            row.address.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT).trim()) ||
                            row.phone.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT).trim()))
                        {
                            resultList.add(row)
                        }
                    }
                    friendFilterList = resultList
                    listOfFilteredFriends = friendFilterList
                }
                val filterResults = FilterResults()
                filterResults.values = friendFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                friendFilterList = results?.values as ArrayList<BEFriend>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        // Inflating R.layout.name_item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        // Getting element from names list at this position
        val element = friendFilterList[position]

        val colours = intArrayOf(
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#CCCCCC")
        )

        // Updating the text of the txtName with this element
        holder.txtName.text = element.name
        holder.txtPhone.text = element.phone
        holder.txtAddress.text = element.address
        holder.imgBtnIsFav.setImageResource(if (element.isFavorite) R.drawable.ok else R.drawable.notok)
        holder.itemView.setBackgroundColor(colours[position % colours.size])

        holder.itemView.setOnClickListener {
            // Invoking itemClickListener and passing it the position and name
            itemClickListener?.invoke(position, element)
        }
    }

    override fun getItemCount(): Int {
        return friendFilterList.size
    }


    fun getFavorites() {
        val favoriteList: List<BEFriend> = listOfFilteredFriends.filter { f -> f.isFavorite}
        friendFilterList = favoriteList as ArrayList<BEFriend>
        notifyDataSetChanged()
    }
    fun getNonFavorites() {
        val nonFavoriteList: List<BEFriend> = listOfFilteredFriends.filter { f -> !f.isFavorite}
        friendFilterList = nonFavoriteList as ArrayList<BEFriend>
        notifyDataSetChanged()
    }
    fun getAll() {
        friendFilterList = listOfFilteredFriends
        notifyDataSetChanged()
    }

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById(R.id.tvName) as TextView
        val txtPhone = itemView.findViewById(R.id.tvPhone) as TextView
        val txtAddress = itemView.findViewById(R.id.tvAddress) as TextView
        val imgBtnIsFav = itemView.findViewById(R.id.imgBtnIsFav) as ImageView
    }

}