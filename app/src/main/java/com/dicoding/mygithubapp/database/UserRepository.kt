package com.dicoding.mygithubapp.database

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(application: Application) {
    private val mUserDao: FavoriteUserDao

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mUserDao.getFavoriteUser()

    fun insertFavorite(id: Int, username: String, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(username, id, avatarUrl)
            mUserDao.insertFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = mUserDao.checkUser(id)

    fun removeFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            mUserDao.removeFavorite(id)
        }
    }
}