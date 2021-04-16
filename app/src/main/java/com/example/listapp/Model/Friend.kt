package com.example.listapp.Model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.io.Serializable
import java.time.LocalDate

@Entity
data class Friend (
        @PrimaryKey(autoGenerate = true) var id: Int,
        var name: String,
        var phone: String,
        var address: String,
        var latitude: Double?,
        var longitude: Double?,
        var email: String,
        var url: String,
        var birthday: LocalDate?,
        var picture: String?
) : Serializable
