package com.dicoding.mygithubapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.mygithubapp.viewmodel.DetailViewModel
import com.dicoding.mygithubapp.R
import com.dicoding.mygithubapp.adapter.SectionPagerAdapter
import com.dicoding.mygithubapp.databinding.ActivityDetailBinding
import com.dicoding.mygithubapp.response.DetailUserResponse
import com.dicoding.mygithubapp.ui.SettingsActivity
import com.dicoding.mygithubapp.ui.favorite.FavoriteUserActivity
import com.dicoding.mygithubapp.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = getString(R.string.detail_section)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val detailViewModel = obtainViewModel(this@DetailActivity)

        val username = intent.getStringExtra(ARG_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.setDetail(username!!)
        detailViewModel.user.observe(this) { user ->
            setUserDetail(user)
        }

        detailViewModel.error.observe(this) {
            if (it)
                Toast.makeText(this@DetailActivity, "Error Fetching Data", Toast.LENGTH_SHORT).show()
        }

        var flag = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count > 0) {
                    flag = true
                    binding.fab.apply {
                        setImageResource(R.drawable.ic_favorite)
                    }
                } else {
                    flag = false
                    binding.fab.apply {
                        setImageResource(R.drawable.ic_favorite_border)
                    }
                }
            }
        }

        binding.fab.setOnClickListener {
            flag = !flag
            if (flag) {
                detailViewModel.insertFavorite(id, username, avatarUrl!!)
                binding.fab.apply {
                    setImageResource(R.drawable.ic_favorite)
                }
            } else {
                detailViewModel.removeFavorite(id)
                binding.fab.apply {
                    setImageResource(R.drawable.ic_favorite_border)
                }
            }
        }

        setFollowPager(username)
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun setUserDetail(user: DetailUserResponse) {
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.login
            tvFollowers.text = resources.getString(R.string.follower_count, user.followers)
            tvFollowing.text = resources.getString(R.string.following_count, user.followers)
            Glide.with(this@DetailActivity)
                .load(user.avatarUrl)
                .centerCrop()
                .into(userPicture)
        }
    }

    private fun setFollowPager(username: String) {
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        sectionsPagerAdapter.username = username

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
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
                Intent(this@DetailActivity, FavoriteUserActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.action_settings -> {
                Intent(this@DetailActivity, SettingsActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val ARG_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
