package com.example.myfirstgithubuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstgithubuser.data.response.ItemsItem
import com.example.myfirstgithubuser.databinding.ActivityFavoriteUserBinding
import com.example.myfirstgithubuser.ui.SettingPreferences
import com.example.myfirstgithubuser.ui.adapter.UserAdapter
import com.example.myfirstgithubuser.ui.dataStore
import com.example.myfirstgithubuser.ui.viewmodel.DetailViewModel
import com.example.myfirstgithubuser.ui.viewmodel.FavoriteViewModel
import com.example.myfirstgithubuser.ui.viewmodel.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val favoriteViewModel = ViewModelProvider(this, ViewModelFactory(pref,application))[FavoriteViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavuser.layoutManager = layoutManager

        favoriteViewModel.apply {
            getAllFavorite().observe(this@FavoriteUserActivity) { users ->
                val items = arrayListOf<ItemsItem>()
                users.map {
                    val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                    items.add(item)
                }
                val adapter = UserAdapter()
                adapter.submitList(items)
                binding.rvFavuser.adapter = adapter
            }
        }

        favoriteViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
