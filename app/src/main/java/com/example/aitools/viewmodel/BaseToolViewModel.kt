package com.example.aitools.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.aitools.data.GeminiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseToolViewModel(
    application: Application,
    private val toolName: String
) : AndroidViewModel(application) {
    
    protected lateinit var geminiClient: GeminiClient
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    protected val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun initialize(apiKey: String) {
        geminiClient = GeminiClient(apiKey)
    }
    
    protected suspend fun generate(prompt: String): String {
        _isLoading.value = true
        _error.value = null
        
        try {
            return geminiClient.generateText(prompt)
        } catch (e: Exception) {
            _error.value = e.message
            return "Error: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
} 