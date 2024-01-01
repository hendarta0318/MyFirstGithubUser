package com.example.myfirstgithubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.myfirstgithubuser.database.FavoriteRoomDatabase
import com.example.myfirstgithubuser.database.FavoriteUser
import com.example.myfirstgithubuser.database.FavoriteUserDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.FavoriteUserDao()
    }

    fun getFavoriteUserByUsername(username: String):LiveData<FavoriteUser> = mFavoriteDao.getFavoriteUserByUsername(username)

    fun getAllFavorite():LiveData<List<FavoriteUser>> = mFavoriteDao.getAllFavorite()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.insert(favoriteUser) }
    }
    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.delete(favoriteUser) }
    }
}