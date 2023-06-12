package com.dicoding.mygithubapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubapp.database.FavoriteUser
import com.dicoding.mygithubapp.database.UserRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mUserRepository.getAllFavoriteUser()
}