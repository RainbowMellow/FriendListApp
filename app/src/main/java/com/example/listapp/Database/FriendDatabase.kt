package com.example.listapp.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.listapp.Model.Friend

@Database(entities = [Friend::class], version=5)
@TypeConverters(TypeConverter::class)
abstract class FriendDatabase : RoomDatabase() {

    abstract fun friendDao(): FriendDao
}