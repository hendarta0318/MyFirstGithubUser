package com.example.myfirstgithubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfirstgithubuser.database.FavoriteUser
import com.example.myfirstgithubuser.repository.FavoriteRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllFavorite(): LiveData<List<FavoriteUser>> = mFavoriteRepository.getAllFavorite()

}