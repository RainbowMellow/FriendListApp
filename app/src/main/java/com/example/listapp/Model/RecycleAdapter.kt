package com.example.listapp.Model

import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.listapp.R
import kotlinx.android.synthetic.main.cell.*
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class RecycleAdapter(private val friends: ArrayList<BEFriend>) : RecyclerView.Adapter<RecycleAdapter.FriendViewHolder>(),
    Filterable {

    //FriendFilterList = List shown in the recycleView
    var friendFilterList = ArrayList<BEFriend>()

    //ListOfFilteredFriends = The list after it has been filtered
    // (So you can use the spinner on the filtered text.)
    var listOfFilteredFriends = ArrayList<BEFriend>()

    init {
        friendFilterList = friends
        listOfFilteredFriends = friendFilterList
    }


    var itemClickListener: ((position: Int, friend: BEFriend) -> Unit)? = null

    //Sets what should be filtered
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

                    //The user input from the search bar should be looked for in name, address and phone
                    for (row in friends) {
                        if(row.name.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(Locale.ROOT).trim()
                            ) ||
                            row.address.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                ).trim()
                            ) ||
                            row.phone.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                ).trim()
                            ))
                        {
                            resultList.add(row)
                        }
                    }
                    friendFilterList = resultList

                    //Setting the listOfFilteredFriends to be stuck at this instance of friendFilterList
                    //Because friendFilterList changes depending on what the list should show on screen
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        // Getting element from friend list at this position
        val element = friendFilterList[position]

        val colours = intArrayOf(
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#CCCCCC")
        )

        // Updating the text of the views in the cell view with this elements info
        holder.txtName.text = element.name
        holder.txtPhone.text = element.phone
        holder.txtAddress.text = element.address

        if(element.picture != null)
        {
            val mSaveBit = element.picture
            val filePath: String = mSaveBit?.path.toString()
            val bitmap = BitmapFactory.decodeFile(filePath)
            holder.picture.setImageBitmap(bitmap)
            holder.picture.rotation = 90F;
        }
        else {
            holder.picture.setImageResource(R.drawable.avatar)
        }

        if(element.birthday == LocalDate.now())
        {
            holder.birthday.visibility = View.VISIBLE
        }
        else {
            holder.birthday.visibility = View.INVISIBLE
        }

        holder.itemView.setBackgroundColor(colours[position % colours.size])

        holder.itemView.setOnClickListener {
            // Invoking itemClickListener and passing it the position and friend
            itemClickListener?.invoke(position, element)
        }
    }

    override fun getItemCount(): Int {
        return friendFilterList.size
    }

    //Returns the list to all friends
    fun getAll() {
        friendFilterList = listOfFilteredFriends
        notifyDataSetChanged()
    }

    //Finds all the views we want to fill with info
    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById(R.id.tvName) as TextView
        val txtPhone = itemView.findViewById(R.id.tvPhone) as TextView
        val txtAddress = itemView.findViewById(R.id.tvAddress) as TextView
        val picture = itemView.findViewById(R.id.ivPicture) as ImageView
        val birthday = itemView.findViewById(R.id.ivFlag) as ImageView
    }

    fun addFriend(friend: BEFriend)
    {
        friendFilterList.add(friend)

        var index = 0

        friendFilterList.forEach{ f ->
            if (f.name == friend.name && f.phone == friend.phone
                    && f.address == friend.address)
            {
                index = friendFilterList.indexOf(f)
            }
        }

        notifyDataSetChanged()
        notifyItemRangeChanged(index, friendFilterList.size)
    }

    fun editFriend(friend: BEFriend, chosenFriend: BEFriend) {

        println(chosenFriend.name)
        println(chosenFriend.address)
        println(chosenFriend.phone)

        var index = 0

        friendFilterList.forEach{ f ->
            if (f.name == chosenFriend.name && f.phone == chosenFriend.phone
                    && f.address == chosenFriend.address)
            {
                index = friendFilterList.indexOf(f)
            }
        }

        println(index)

        val editedFriend = friendFilterList[index]
        editedFriend.name = friend.name
        editedFriend.phone = friend.phone
        editedFriend.address = friend.address
        editedFriend.email = friend.email
        editedFriend.url = friend.url
        editedFriend.birthday = friend.birthday

        editedFriend.picture = friend.picture

        notifyItemChanged(index)
    }

    fun getList() : ArrayList<BEFriend>
    {
        return friendFilterList
    }

    fun deleteFriend(chosenFriend: BEFriend) {
        var index = 0

        friendFilterList.forEach{ f ->
            if (f.name == chosenFriend.name && f.phone == chosenFriend.phone
                    && f.address == chosenFriend.address)
            {
                index = friendFilterList.indexOf(f)
            }
        }

        friendFilterList.removeAt(index)

        notifyItemRemoved(index)
        notifyItemRangeChanged(index, friendFilterList.size)
    }

}