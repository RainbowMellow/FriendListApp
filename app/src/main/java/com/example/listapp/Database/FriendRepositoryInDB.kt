package com.example.listapp.Database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.listapp.Model.Friend
import java.util.concurrent.Executors

class FriendRepositoryInDB private constructor(context: Context) {

    /**
     * Builds database
     * Note the decision to destroy and recreate database on version change
     */
    private val database : FriendDatabase = Room.databaseBuilder(
        context.applicationContext,
        FriendDatabase::class.java,
        "friend-database"
    ).fallbackToDestructiveMigration().build()

    private val friendDao = database.friendDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAll(): LiveData<List<Friend>> = friendDao.getAll()

    fun insert(p: Friend) {
        executor.execute{ friendDao.insert(p) }
    }

    fun update(p: Friend) {
        executor.execute { friendDao.update(p) }
    }

    fun delete(p: Friend) {
        executor.execute { friendDao.delete(p) }
    }

    fun clear() {
        executor.execute { friendDao.deleteAll() }
    }


    companion object {
        private var Instance: FriendRepositoryInDB? = null

        fun initialize(context: Context) {
            if (Instance == null)
                Instance = FriendRepositoryInDB(context)
        }

        fun get(): FriendRepositoryInDB {
            if (Instance != null) return Instance!!
            throw IllegalStateException("Friend repo not initialized")
        }
    }

}