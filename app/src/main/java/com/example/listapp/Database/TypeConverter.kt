package com.example.listapp.Database

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.io.File
import java.time.LocalDate
import java.util.*

class TypeConverter {

    /**
     * Converts the string from the database to a LocalDate.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDate(dateString: String?): LocalDate? {
        return if (dateString == null) {
            null
        } else {
            LocalDate.parse(dateString)
        }
    }

    /**
     * Converts the localDate to a string for storing in the database.
     */
    @TypeConverter
    fun toDateString(date: LocalDate?): String? {
        return date?.toString()
    }
}