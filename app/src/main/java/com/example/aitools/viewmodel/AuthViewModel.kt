package com.example.aitools.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitools.data.ApiKeyDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val apiKeyDataStore = ApiKeyDataStore(application)
    
    private val _apiKeyState = MutableStateFlow<String?>(null)
    val apiKeyState: StateFlow<String?> = _apiKeyState.asStateFlow()
    
    init {
        viewModelScope.launch {
            apiKeyDataStore.apiKey.collect { apiKey ->
                _apiKeyState.value = if (apiKey?.isBlank() == true) null else apiKey
            }
        }
    }
    
    suspend fun saveApiKey(apiKey: String) {
        if (apiKey.isBlank()) {
            return
        }
        apiKeyDataStore.saveApiKey(apiKey)
    }
} 