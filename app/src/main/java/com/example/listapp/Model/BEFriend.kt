package com.example.listapp.Model

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.io.File
import java.io.Serializable
import java.time.LocalDate
import java.util.*

class BEFriend(
    var id: Int, var name: String, var phone: String, var address: String, var location: Pair<Double, Double>,
    var email: String, var url: String, var birthday: LocalDate, var picture: File?
) : Serializable {

}
