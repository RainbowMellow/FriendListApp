package com.example.listapp.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.listapp.Model.Friend

@Dao
interface FriendDao {

    @Query("SELECT * from Friend order by id")
    fun getAll(): LiveData<List<Friend>>

    @Query("SELECT name from Friend order by name")
    fun getAllNames(): LiveData<List<String>>

    @Query("SELECT * from Friend where id = (:id)")
    fun getById(id: Int): LiveData<Friend>

    @Insert
    fun insert(p: Friend)

    @Update
    fun update(p: Friend)

    @Delete
    fun delete(p: Friend)

    @Query("DELETE from Friend")
    fun deleteAll()
}