package com.example.listapp.GUI

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.listapp.Model.BEFriend
import com.example.listapp.R
import kotlinx.android.synthetic.main.detailview.*
import java.io.Serializable

class DetailActivity : AppCompatActivity() {

    var isCreate: Boolean = false
    var chosenFriend = BEFriend("", "", "", "", "", false)

    val CREATE_FRIEND = 1
    val DELETE_FRIEND = 2
    val UPDATE_FRIEND = 3
    val PERMISSION_REQUEST_CODE = 1


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
                    etEmail.setText(friend.email)
                    etURL.setText(friend.url)

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
                val email = etEmail.text.toString()
                val url = etURL.text.toString()

                val friend = createFriend(name, phone, address, email, url, isFavorite)

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
                val email = etEmail.text.toString()
                val url = etURL.text.toString()

                val friend = createFriend(name, phone, address, email, url, isFavorite)

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

    fun createFriend(name: String, phone: String, address: String, email: String, url: String, isFavorite: Boolean): BEFriend
    {
        val friend = BEFriend( name = name, phone = phone, address = address, email = email, url = url, isFavorite = isFavorite)
        return friend
    }

    fun onClickCall(view: View) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:${chosenFriend.phone}")
        startActivity(callIntent)
    }

    fun onClickText(view: View) {
        showYesNoDialog()
    }

    private fun showYesNoDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("SMS Handling")
        alertDialogBuilder
                .setMessage("Click Direct if SMS should be send directly. Click Start to start SMS app...")
                .setCancelable(true)
                .setPositiveButton("Direct") { dialog, id -> sendSMSDirectly() }
                .setNegativeButton("Start", { dialog, id -> startSMSActivity() } )
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun sendMessage() {
        val m = SmsManager.getDefault()
        val text = "Hi, it goes well on the android course..."
        m.sendTextMessage(chosenFriend.phone, null, text, null, null)
    }

    private fun startSMSActivity() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("sms:${chosenFriend.phone}")
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...")
        startActivity(sendIntent)
    }

    fun onClickEMAIL(view: View) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "plain/text"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, chosenFriend.email)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test")
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                "Hej, Hope that it is ok, Best Regards android...;-)")
        startActivity(emailIntent)
    }

    fun onClickBROWSER(view: View) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(chosenFriend.url)
        startActivity(i)
    }

    private fun sendSMSDirectly() {
        Toast.makeText(this, "An sms will be send", Toast.LENGTH_LONG)
                .show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {
                println("permission denied to SEND_SMS - requesting it")
                val permissions = arrayOf(Manifest.permission.SEND_SMS)
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
                return
            } else println( "permission to SEND_SMS granted!")
        } else println( "Runtime permission not needed")
        sendMessage()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        println("Permission: " + permissions[0] + " - grantResult: " + grantResults[0])
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage()
        }
    }
}