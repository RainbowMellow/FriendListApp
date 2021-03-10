package com.example.listapp.GUI

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.listapp.Model.BEFriend
import com.example.listapp.R
import kotlinx.android.synthetic.main.detailview.*
import java.io.Serializable

class DetailActivity : AppCompatActivity() {

    var isCreate: Boolean = false
    var chosenFriend = BEFriend("", "", "", false)

    val CREATE_FRIEND = 1
    val DELETE_FRIEND = 2
    val UPDATE_FRIEND = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailview)

        if (intent.extras != null) {
            val extras: Bundle = intent.extras!!
            val create = extras.getBoolean("isCreate")

            isCreate = create

            if(!isCreate)
            {
                val friend = extras.getSerializable("friend")

                with (friend as BEFriend)
                {
                    etName.setText(friend.name)
                    etPhone.setText(friend.phone)
                    etAddress.setText(friend.address)

                    cbFavorite.isChecked = friend.isFavorite

                    chosenFriend = friend
                }
            }

        }

        if(isCreate)
        {
            ibTrash.visibility = View.INVISIBLE
        }
    }

    //Finish ends the current activity
    fun onClickBack(view: View) {finish()}

    fun onClickDelete(view: View) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete Friend")
        builder.setMessage("Do you want to delete this friend?")
        builder.setIcon(R.drawable.trash)

        builder.setPositiveButton("Yes")
        {dialogInterface, which ->
            val intent = Intent()
            intent.putExtra("chosenFriend", chosenFriend)
            setResult(DELETE_FRIEND, intent)
            finish();
        }

        builder.setNeutralButton("Cancel")
        {dialogInterface, which -> println("Clicked Cancel")}

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun onClickSave(view: View) {
        val builder = AlertDialog.Builder(this)

        if (isCreate)
        {
            builder.setTitle("Create Friend")
            builder.setMessage("Do you want to create this friend?")
            builder.setIcon(R.drawable.save)

            builder.setPositiveButton("Yes")
            {dialogInterface, which ->

                val name = etName.text.toString()
                val phone = etPhone.text.toString()
                val address = etAddress.text.toString()
                val isFavorite = cbFavorite.isChecked

                val friend = createFriend(name, phone, address, isFavorite)

                val intent = Intent()
                intent.putExtra("friend", friend)
                setResult(CREATE_FRIEND, intent)
                finish();
            }

            builder.setNeutralButton("Cancel")
            {dialogInterface, which -> println("Clicked Cancel")}
        }

        else if (!isCreate)
        {
            builder.setTitle("Save Friend")
            builder.setMessage("Do you want to save this friend?")
            builder.setIcon(R.drawable.save)

            builder.setPositiveButton("Yes")
            {dialogInterface, which ->
                val name = etName.text.toString()
                val phone = etPhone.text.toString()
                val address = etAddress.text.toString()
                val isFavorite = cbFavorite.isChecked

                val friend = createFriend(name, phone, address, isFavorite)

                val intent = Intent()
                intent.putExtra("friend", friend)
                intent.putExtra("chosenFriend", chosenFriend)
                setResult(UPDATE_FRIEND, intent)
                finish();
            }

            builder.setNeutralButton("Cancel")
            {dialogInterface, which -> println("Clicked Cancel")}
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun createFriend(name: String, phone: String, address: String, isFavorite: Boolean): BEFriend
    {
        val friend = BEFriend( name = name, phone = phone, address = address, isFavorite = isFavorite)
        return friend
    }

}