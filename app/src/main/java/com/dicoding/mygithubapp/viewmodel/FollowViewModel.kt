package com.dicoding.mygithubapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubapp.api.ApiConfig
import com.dicoding.mygithubapp.response.FollowResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _follower = MutableLiveData<List<FollowResponseItem>>()
    var follower: LiveData<List<FollowResponseItem>> = _follower

    private val _following = MutableLiveData<List<FollowResponseItem>>()
    var following: LiveData<List<FollowResponseItem>> = _following

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "FollowViewModel"
    }

    fun showFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _follower.value = response.body()
                } else {
                    _error.value = true
                    Log.e(TAG, "showFollowers onFailure \"onResponse\": ${response.body().toString()} & ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
                Log.e(TAG, "showFollowers onFailure: ${t.message.toString()}")
            }
        })
    }

    fun showFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _following.value = response.body()
                } else {
                    _error.value = true
                    Log.e(TAG, "showFollowing onFailure \"onResponse\": ${response.body().toString()} & ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
                Log.e(TAG, "showFollowing onFailure: ${t.message.toString()}")
            }
        })
    }
}