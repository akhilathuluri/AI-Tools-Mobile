package com.example.aitools.viewmodel

import android.app.Application
import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ImageAnalyzerViewModel(application: Application) : BaseToolViewModel(application, "Image Analyzer") {
    private val _analysis = MutableStateFlow("")
    val analysis: StateFlow<String> = _analysis.asStateFlow()
    
    private val _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage: StateFlow<Bitmap?> = _selectedImage.asStateFlow()
    
    fun setImage(bitmap: Bitmap) {
        _selectedImage.value = bitmap
    }
    
    suspend fun analyzeImage(
        analysisType: String = "general",
        specificQuery: String = ""
    ) {
        val image = selectedImage.value
        if (image == null) {
            _error.value = "Please select an image first"
            return
        }
        
        val prompt = when (analysisType) {
            "general" -> """
                Analyze this image and provide a detailed description including:
                - Main subjects/objects
                - Colors and composition
                - Mood/atmosphere
                - Notable details
                - Context or setting
            """.trimIndent()
            
            "technical" -> """
                Provide a technical analysis of this image including:
                - Image quality and resolution
                - Lighting conditions
                - Composition techniques
                - Camera settings (if detectable)
                - Suggested improvements
            """.trimIndent()
            
            "custom" -> specificQuery
            
            else -> "Describe what you see in this image."
        }
        
        try {
            _analysis.value = geminiClient.analyzeImage(image, prompt)
        } catch (e: Exception) {
            _error.value = "Failed to analyze image: ${e.message}"
        }
    }
} 