package com.example.uzbengtranslator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uzbengtranslator.model.LanguageDetection
import com.example.uzbengtranslator.model.TranslationResponse
import com.example.uzbengtranslator.networking.services.ApiService
import com.example.uzbengtranslator.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val apiService: ApiService) : ViewModel() {

    private var _language = MutableStateFlow<UiStateObject<LanguageDetection>>(UiStateObject.EMPTY)
    var language = _language
    private var _translationResponse =
        MutableStateFlow<UiStateObject<TranslationResponse>>(UiStateObject.EMPTY)
    val translation = _translationResponse

    fun detectLanguage(q: String) = viewModelScope.launch {
        _language.value = UiStateObject.LOADING
        try {
            val detectedLanguage = apiService.detectLanguage(q)

            _language.value = UiStateObject.SUCCESS(detectedLanguage)


        } catch (e: Exception) {
            _language.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection", false)
        }
    }

    fun translate(q: String, source: String, target: String) = viewModelScope.launch {
        _translationResponse.value = UiStateObject.LOADING
        try {
            val translation = apiService.translateToEng(q, source, target)

            _translationResponse.value = UiStateObject.SUCCESS(translation)


        } catch (e: Exception) {
            _translationResponse.value =
                UiStateObject.ERROR(e.localizedMessage ?: "No Connection", false)
        }
    }
}