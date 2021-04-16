package com.example.listapp.GUI

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.telephony.SmsManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.listapp.Model.Friend
import com.example.listapp.R
import kotlinx.android.synthetic.main.detailview.*
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class DetailActivity : AppCompatActivity() {

    /**
     * Boolean that changes based on if the user is creating- or editing/viewing a friend.
     */
    var isCreate: Boolean = false

    /**
     * Variable that gets overwritten with the friend the user has clicked on.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    var chosenFriend = Friend(
        0,"", "", "", 0.0, 0.0, "", "", LocalDate.now(), "")

    /**
     * Variable that gets overwritten with the current location of the phone
     * when the user clicked a friend.
     */
    var currentLocation = Pair(0.0, 0.0)

    /**
     * Values that are used for resultcodes and requestcodes.
     */
    val CREATE_FRIEND = 1
    val DELETE_FRIEND = 2
    val UPDATE_FRIEND = 3
    val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE = 101

    /**
     * Variable that stores the picture that user took of their friend.
     */
    var mFile: File? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailview)

        checkPermissions()

        if (intent.extras != null) {
            val extras: Bundle = intent.extras!!
            val create = extras.getBoolean("isCreate")
            val lat = extras.getDouble("lat")
            val lng = extras.getDouble("lng")

            currentLocation = Pair(lat, lng)

            isCreate = create

            //Inputs all info from the chosen friend into the forms.
            if(!isCreate)
            {
                val friend = extras.getSerializable("friend")

                with(friend as Friend)
                {
                    etName.setText(friend.name)
                    etPhone.setText(friend.phone)
                    etAddress.setText(friend.address)
                    etEmail.setText(friend.email)
                    etURL.setText(friend.url)
                    etBirthday.setText(friend.birthday.toString())

                    if(friend.birthday == LocalDate.now())
                    {
                        ivBirthday.visibility = View.VISIBLE
                    }
                    else {
                        ivBirthday.visibility = View.INVISIBLE
                    }


                    if(friend.picture != null)
                    {
                        ibPicture.setImageURI(Uri.parse(friend.picture))
                    }
                    else {
                        ibPicture.setImageResource(R.drawable.avatar_big)
                    }


                    chosenFriend = friend
                }
            }

        }

        //Hides the delete button when the user is creating a new friend.
        if(isCreate)
        {
            ibTrash.visibility = View.INVISIBLE
        }
    }

    // region Menu

    /**
     * Creates the menu.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.detail_menu, menu);

        return true;
    }

    /**
     * Sets the action that will happen if the user clicks on the menu item.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()

        when (id) {
            R.id.action_back -> {
                finish()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // endregion


    /**
     * Confirms with the user that they are sure they want to delete the current friend.
     * Sends a delete requestcode if the user confirms.
     */
    fun onClickDelete(view: View) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete Friend")
        builder.setMessage("Do you want to delete this friend?")
        builder.setIcon(R.drawable.trash)

        builder.setPositiveButton("Yes")
        { dialogInterface, which ->
            val intent = Intent()
            intent.putExtra("chosenFriend", chosenFriend)
            setResult(DELETE_FRIEND, intent)
            finish();
        }

        builder.setNeutralButton("Cancel")
        { dialogInterface, which -> println("Clicked Cancel")}

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    /**
     * Confirms with the user that they are sure they want save/create the friend.
     * Sends a save/create requestcode and the friend if the user confirms.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickSave(view: View) {
        val builder = AlertDialog.Builder(this)

        if (isCreate)
        {
            builder.setTitle("Create Friend")
            builder.setMessage("Do you want to create this friend?")
            builder.setIcon(R.drawable.save)

            builder.setPositiveButton("Yes")
            { dialogInterface, which ->

                val name = etName.text.toString()
                val phone = etPhone.text.toString()
                val address = etAddress.text.toString()
                val email = etEmail.text.toString()
                val url = etURL.text.toString()
                val birthday = LocalDate.parse(etBirthday.text.toString())

                var picture: String? = ""

                picture = if(mFile != null) {
                    Uri.fromFile(mFile!!).toString()
                } else {
                    null
                }

                val friend = Friend(0, name, phone, address, currentLocation.first, currentLocation.second, email, url, birthday, picture)

                val intent = Intent()
                intent.putExtra("friend", friend)
                setResult(CREATE_FRIEND, intent)
                finish();
            }

            builder.setNeutralButton("Cancel")
            { dialogInterface, which -> println("Clicked Cancel")}
        }

        else if (!isCreate)
        {
            builder.setTitle("Save Friend")
            builder.setMessage("Do you want to save this friend?")
            builder.setIcon(R.drawable.save)

            builder.setPositiveButton("Yes")
            { dialogInterface, which ->
                val name = etName.text.toString()
                val phone = etPhone.text.toString()
                val address = etAddress.text.toString()
                val email = etEmail.text.toString()
                val url = etURL.text.toString()
                val birthday = LocalDate.parse(etBirthday.text.toString())

                var picture: String? = ""

                picture = when {
                    mFile != null -> {
                        Uri.fromFile(mFile!!).toString()
                    }
                    chosenFriend.picture != null -> {
                        chosenFriend.picture
                    }
                    else -> {
                        null
                    }
                }

                val friend = Friend(chosenFriend.id, name, phone, address, chosenFriend.latitude, chosenFriend.longitude, email, url, birthday, picture)

                val intent = Intent()
                intent.putExtra("friend", friend)
                intent.putExtra("chosenFriend", chosenFriend)
                setResult(UPDATE_FRIEND, intent)
                finish();
            }

            builder.setNeutralButton("Cancel")
            { dialogInterface, which -> println("Clicked Cancel")}
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    // region Use other apps

    /**
     * Starts phone app
     */
    fun onClickCall(view: View) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:${chosenFriend.phone}")
        startActivity(callIntent)
    }

    /**
     * Opens dialog.
     */
    fun onClickText(view: View) {
        showYesNoDialog()
    }

    /**
     * Asks the user in a dialog box if they want to go to the texting app, og send a direct text.
     */
    private fun showYesNoDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("SMS Handling")
        alertDialogBuilder
                .setMessage("Click Direct if SMS should be send directly. Click Start to start SMS app...")
                .setCancelable(true)
                .setPositiveButton("Direct") { dialog, id -> sendSMSDirectly() }
                .setNegativeButton("Start", { dialog, id -> startSMSActivity() })
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    /**
     * Sends a direct message with a default text.
     */
    private fun sendMessage() {
        val m = SmsManager.getDefault()
        val text = "Hi, it goes well on the android course..."
        m.sendTextMessage(chosenFriend.phone, null, text, null, null)
    }

    /**
     * Starts texting app
     */
    private fun startSMSActivity() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("sms:${chosenFriend.phone}")
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...")
        startActivity(sendIntent)
    }

    /**
     * Starts email app.
     */
    fun onClickEMAIL(view: View) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "plain/text"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, chosenFriend.email)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test")
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hej, Hope that it is ok, Best Regards android...;-)"
        )
        startActivity(emailIntent)
    }

    /**
     * Starts browser with the friends' url.
     */
    fun onClickBROWSER(view: View) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(chosenFriend.url)
        startActivity(i)
    }

    /**
     * Makes sure that we have permission to send a direct text.
     */
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
            } else println("permission to SEND_SMS granted!")
        } else println("Runtime permission not needed")
        sendMessage()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        println("Permission: " + permissions[0] + " - grantResult: " + grantResults[0])
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage()
        }
    }

    // endregion

    // region Picture

    /**
     * Checks permission for using the camera app.
     */
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val permissions = mutableListOf<String>()
        if ( ! isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ) permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if ( ! isGranted(Manifest.permission.CAMERA) ) permissions.add(Manifest.permission.CAMERA)
        if (permissions.size > 0)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
    }

    private fun isGranted(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED


    /**
     * Creates file for storing the taken picture, and opens the camera app.
     */
    fun onClickPicture(view: View) {
        mFile = getOutputMediaFile("Camera01") // create a file to save the image

        if (mFile == null) {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show()
            return
        }

        // create Intent to take a picture
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val applicationId = "com.example.listapp"
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
            this,
            "${applicationId}.provider",  //use your app signature + ".provider"
            mFile!!))

        try {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE)
        } catch (e: ActivityNotFoundException) {
            println("camera app could NOT be started")
            println(e)
        }
    }

    /**
     * Creates a file or returns null if it fails.
     */
    private fun getOutputMediaFile(folder: String): File? {
        // in an emulated device you can see the external files in /sdcard/Android/data/<your app>.
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                println("failed to create directory")
                return null
            }
        }

        // Create a media file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val postfix = "jpg"
        val prefix = "IMG"
        return File(mediaStorageDir.path +
                File.separator + prefix +
                "_" + timeStamp + "." + postfix)
    }

    /**
     * Checks if the user accepted the photo or canceled/other action.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val image = findViewById<ImageButton>(R.id.ibPicture)
        when (requestCode) {

            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE ->
                if (resultCode == RESULT_OK)
                    showImageFromFile(image, mFile!!)
                else handleOther(resultCode)
        }
    }

    /**
     * Handles if another result than OK comes back.
     */
    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }

    /**
     * Sets the imageButtons picture to the picture taken.
     */
    private fun showImageFromFile(img: ImageButton, f: File) {
        img.setImageURI(Uri.fromFile(f))
    }

    // endregion

    // region Map

    /**
     * Sets the friends location to the current location
     */
    fun onClickHome(view: View) {
        chosenFriend.latitude = currentLocation.first
        chosenFriend.longitude = currentLocation.second
    }

    /**
     * Opens the maps app with the current location and the friends' location.
     */
    fun onClickMap(view: View) {

        if(currentLocation != Pair(0.0, 0.0))
        {
            val friendList = ArrayList<Friend>()
            friendList.add(chosenFriend)

            val intent = Intent(this, MapsActivity::class.java)

            intent.putExtra("friendList", friendList)
            intent.putExtra("lat", currentLocation.first)
            intent.putExtra("lng", currentLocation.second)

            startActivity(intent)
        }
        else {
            println("Wait a moment, the location is not set yet.")
        }

    }

    // endregion
}