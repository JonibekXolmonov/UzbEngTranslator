package com.example.uzbengtranslator.viewmodel.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.uzbengtranslator.networking.services.ApiService
import com.example.uzbengtranslator.viewmodel.MainViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}