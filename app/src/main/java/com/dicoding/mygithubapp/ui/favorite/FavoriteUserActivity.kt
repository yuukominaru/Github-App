package com.dicoding.mygithubapp.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubapp.adapter.FavoriteUserAdapter
import com.dicoding.mygithubapp.database.FavoriteUser
import com.dicoding.mygithubapp.databinding.ActivityFavoriteUserBinding
import com.dicoding.mygithubapp.ui.detail.DetailActivity
import com.dicoding.mygithubapp.viewmodel.FavoriteViewModel
import com.dicoding.mygithubapp.viewmodel.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private var _binding: ActivityFavoriteUserBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        val favoriteViewModel = obtainViewModel(this@FavoriteUserActivity)
        favoriteViewModel.getAllFavoriteUser().observe(this) {
            if (it != null) {
                adapter.setListFavorite(it)
            }
        }

        setUserData()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUserData() {
        adapter = FavoriteUserAdapter()
        binding?.rvUser?.layoutManager = LinearLayoutManager(this)
        binding?.rvUser?.setHasFixedSize(true)
        binding?.rvUser?.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteUserAdapter.OnItemClickCallback {
            override fun onItemClicked(favorite: FavoriteUser) {
                Intent(this@FavoriteUserActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.ARG_USERNAME, favorite.login)
                    it.putExtra(DetailActivity.EXTRA_ID, favorite.id)
                    it.putExtra(DetailActivity.EXTRA_URL, favorite.avatarUrl)
                    startActivity(it)
                }
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}