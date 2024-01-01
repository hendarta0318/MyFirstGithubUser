package com.example.myfirstgithubuser.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myfirstgithubuser.data.response.GithubResponse
import com.example.myfirstgithubuser.data.response.ItemsItem
import com.example.myfirstgithubuser.data.retrofit.ApiConfig
import com.example.myfirstgithubuser.database.FavoriteUser
import com.example.myfirstgithubuser.database.FavoriteUserDao
import com.example.myfirstgithubuser.repository.FavoriteRepository
import com.example.myfirstgithubuser.ui.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences): ViewModel() {

    private val _githubusr = MutableLiveData<GithubResponse>()
    val githubusr: LiveData<GithubResponse> = _githubusr

    private val _item = MutableLiveData<List<ItemsItem>>()
    val item: LiveData<List<ItemsItem>> = _item

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
    }

    init {
        findUser("arif")
    }

    fun findUser(q:String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(q)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _item.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}
