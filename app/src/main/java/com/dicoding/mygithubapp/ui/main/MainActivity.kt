package com.dicoding.mygithubapp.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubapp.R
import com.dicoding.mygithubapp.ui.detail.DetailActivity
import com.dicoding.mygithubapp.viewmodel.MainViewModel
import com.dicoding.mygithubapp.adapter.UserAdapter
import com.dicoding.mygithubapp.databinding.ActivityMainBinding
import com.dicoding.mygithubapp.datastore.SettingsPreferences
import com.dicoding.mygithubapp.response.ItemsItem
import com.dicoding.mygithubapp.ui.SettingsActivity
import com.dicoding.mygithubapp.ui.favorite.FavoriteUserActivity
import com.dicoding.mygithubapp.viewmodel.ModeViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingsPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, ModeViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.getUser()

        binding.apply {
            searchView.isFocusable = false
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchUser(query!!)
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        mainViewModel.user.observe(this) { user ->
            setUserData(user)
        }

        mainViewModel.error.observe(this) {
            if (it)
                Toast.makeText(this@MainActivity, "Error Fetching Data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchUser(query: String) {
        showLoading(true)
        mainViewModel.findUser(query)

        mainViewModel.nullQuery.observe(this) {
            if (it)
                Toast.makeText(this@MainActivity, "User Not Found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUserData(users: List<ItemsItem>) {
        adapter = UserAdapter(users)
        binding.rvUser.layoutManager = LinearLayoutManager(this)

        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: ItemsItem) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.ARG_USERNAME, user.login)
                    it.putExtra(DetailActivity.EXTRA_ID, user.id)
                    it.putExtra(DetailActivity.EXTRA_URL, user.avatarUrl)
                    startActivity(it)
                }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                Intent(this@MainActivity, FavoriteUserActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.action_settings -> {
                Intent(this@MainActivity, SettingsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}