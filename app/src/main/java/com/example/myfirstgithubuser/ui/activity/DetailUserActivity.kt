package com.example.myfirstgithubuser.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.example.myfirstgithubuser.R
import com.example.myfirstgithubuser.data.response.DetailUserResponse
import com.example.myfirstgithubuser.database.FavoriteUser
import com.example.myfirstgithubuser.databinding.ActivityDetailUserBinding
import com.example.myfirstgithubuser.ui.SettingPreferences
import com.example.myfirstgithubuser.ui.viewmodel.DetailViewModel
import com.example.myfirstgithubuser.ui.activity.MainActivity.Companion.EXTRA_PROFILE
import com.example.myfirstgithubuser.ui.adapter.SectionsPagerAdapter
import com.example.myfirstgithubuser.ui.dataStore
import com.example.myfirstgithubuser.ui.viewmodel.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following,
        )
    }
    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val detailViewModel = ViewModelProvider(this, ViewModelFactory(pref, application))[DetailViewModel::class.java]
        val username = intent.getStringExtra(EXTRA_PROFILE).toString()
        detailViewModel.findDetailUser(username)
        detailViewModel.findFollowerUser(username)
        detailViewModel.findFollowingUser(username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        detailViewModel.detailuser.observe(this) { detail ->
            setDetailUserData(detail)
            val fab: FloatingActionButton = findViewById(R.id.fav_add)
            val favUser= FavoriteUser(detail.login, detail.avatarUrl)
            detailViewModel.getFavoriteUserByUsername(detail.login).observe(this){ favUs->
                fab.apply {
                    if (favUs != null){
                        fab.setImageResource(R.drawable.baseline_favorite_24)
                        fab.setOnClickListener {
                            detailViewModel.delete(favUs)
                            Toast.makeText(this@DetailUserActivity,"Berhasil Dihapus",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        fab.setImageResource(R.drawable.baseline_favorite_border_24)
                        fab.setOnClickListener {
                            detailViewModel.insert(favUser)
                            Toast.makeText(this@DetailUserActivity,"Berhasil Ditambahkan",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setDetailUserData(detailuser: DetailUserResponse) {
        Glide.with(this)
            .load(detailuser.avatarUrl)
            .transition(
                DrawableTransitionOptions
                .withCrossFade(DrawableCrossFadeFactory.Builder(1500)))
            .centerCrop()
            .circleCrop()
            .into(binding.imageView)
        binding.tvName.text = detailuser.login
        binding.tvUserName.text = detailuser.name
        binding.tvFollower.text = "${detailuser.followers}  Follower"
        binding.tvFollowing.text = "${detailuser.following}  Following"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}