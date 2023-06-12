package com.dicoding.mygithubapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubapp.api.ApiConfig
import com.dicoding.mygithubapp.database.UserRepository
import com.dicoding.mygithubapp.response.DetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _user = MutableLiveData<DetailUserResponse>()
    val user: LiveData<DetailUserResponse> = _user

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mUserRepository: UserRepository = UserRepository(application)

    fun setDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>,
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    _user.value = responseBody!!
                } else {
                    _error.value = true
                    Log.e(
                        TAG,
                        "onFailure \"onResponse\": ${
                            response.body().toString()
                        } & ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun insertFavorite(id: Int, username: String, avatarUrl: String) {
        mUserRepository.insertFavorite(id, username, avatarUrl)
    }

    suspend fun checkUser(id: Int) = mUserRepository.checkUser(id)

    fun removeFavorite(id: Int) {
        mUserRepository.removeFavorite(id)
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}