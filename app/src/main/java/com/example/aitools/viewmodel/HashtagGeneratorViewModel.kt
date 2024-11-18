package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HashtagGeneratorViewModel(application: Application) : BaseToolViewModel(application, "Hashtag Generator") {
    private val _hashtags = MutableStateFlow("")
    val hashtags: StateFlow<String> = _hashtags.asStateFlow()
    
    suspend fun generateHashtags(
        content: String,
        count: Int = 30,
        style: String = "trending"
    ) {
        if (content.length < 10) {
            _error.value = "Content must be at least 10 characters long"
            return
        }
        
        val prompt = """
            Generate Instagram hashtags for the following content:
            Content: $content
            
            Please provide $count relevant hashtags that are:
            - Popular and trending
            - Specific to the content
            - Mix of popular and niche hashtags
            - Style: $style
            
            Format the hashtags in a clean, easy-to-copy format.
        """.trimIndent()
        
        _hashtags.value = generate(prompt)
    }
} 