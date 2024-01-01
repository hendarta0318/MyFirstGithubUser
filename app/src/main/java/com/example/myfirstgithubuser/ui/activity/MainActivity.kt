package com.example.myfirstgithubuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstgithubuser.R
import com.example.myfirstgithubuser.data.response.ItemsItem
import com.example.myfirstgithubuser.databinding.ActivityMainBinding
import com.example.myfirstgithubuser.repository.FavoriteRepository
import com.example.myfirstgithubuser.ui.SettingPreferences
import com.example.myfirstgithubuser.ui.viewmodel.MainViewModel
import com.example.myfirstgithubuser.ui.adapter.UserAdapter
import com.example.myfirstgithubuser.ui.dataStore
import com.example.myfirstgithubuser.ui.viewmodel.DetailViewModel
import com.example.myfirstgithubuser.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_PROFILE = "extra_profile"
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref, application))[MainViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsername.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsername.addItemDecoration(itemDecoration)


        mainViewModel.item.observe(this) { item ->
            setItemData(item)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    Toast.makeText(
                        this@MainActivity,searchView.text.toString(),Toast.LENGTH_SHORT).show()
                    val query = searchView.text.toString()
                    mainViewModel.findUser(query)
                    mainViewModel.item.observe(
                        this@MainActivity,Observer { setItemData(it) })
                    searchView.hide()
                    false
                }
        }
        binding.topAppBar.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.halaman_favorite ->{
                    val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.halaman_settings->{
                    val intent = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.halaman_profile->{
                    val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                    else -> false
                }
            }

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }



    private fun setItemData(item: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(item)
        binding.rvUsername.adapter = adapter
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}