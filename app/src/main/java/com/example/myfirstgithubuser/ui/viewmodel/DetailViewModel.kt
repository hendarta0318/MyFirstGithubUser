package com.example.myfirstgithubuser.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfirstgithubuser.data.response.DetailUserResponse
import com.example.myfirstgithubuser.data.response.ItemsItem
import com.example.myfirstgithubuser.data.retrofit.ApiConfig
import com.example.myfirstgithubuser.database.FavoriteUser
import com.example.myfirstgithubuser.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): ViewModel(){
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _detailuser = MutableLiveData<DetailUserResponse>()
    val detailuser: LiveData<DetailUserResponse> = _detailuser

    private val _listFollower = MutableLiveData<List<ItemsItem>>()
    val listFollower: LiveData<List<ItemsItem>> = _listFollower

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "DetailViewModel"
        private const val USERNAME = ""

    }

    init {
        findDetailUser(USERNAME)
        findFollowerUser(USERNAME)
        findFollowingUser(USERNAME)
    }

    fun findDetailUser(username:String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailuser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun findFollowerUser(username :String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollower.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun findFollowingUser(username :String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavoriteRepository.delete(favoriteUser)
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>{
        return mFavoriteRepository.getFavoriteUserByUsername(username)
    }
}