package com.dicoding.mygithubapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert
    suspend fun insertFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * from favorite_user ORDER BY id ASC")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favorite_user WHERE favorite_user.id = :id")
    suspend fun checkUser(id: Int): Int

    @Query("DELETE FROM favorite_user WHERE favorite_user.id = :id")
    suspend fun removeFavorite(id: Int): Int
}