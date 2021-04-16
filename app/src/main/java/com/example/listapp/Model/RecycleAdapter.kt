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
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.listapp.R
import kotlinx.android.synthetic.main.cell.*
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class RecycleAdapter(private val friends: ArrayList<Friend>) : RecyclerView.Adapter<RecycleAdapter.FriendViewHolder>()
{

    var friendFilterList = friends

    var itemClickListener: ((position: Int, friend: Friend) -> Unit)? = null

    /**
     * Creates and returns the FriendViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        println(friendFilterList)
        // Inflating R.layout.name_item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell, parent, false)
        return FriendViewHolder(view)
    }

    /**
     * Binds the data to the FriendViewHolder that should be shown in each cell.
     */
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
            val mSaveBit = Uri.parse(element.picture)
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

    /**
     * Returns the size of the friendFilterList.
     */
    override fun getItemCount(): Int {
        return friendFilterList.size
    }

    /**
     * Finds all the views we want to fill with info and makes them known to the FriendViewHolder.
     */
    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName = itemView.findViewById(R.id.tvName) as TextView
        val txtPhone = itemView.findViewById(R.id.tvPhone) as TextView
        val txtAddress = itemView.findViewById(R.id.tvAddress) as TextView
        val picture = itemView.findViewById(R.id.ivPicture) as ImageView
        val birthday = itemView.findViewById(R.id.ivFlag) as ImageView
    }
}