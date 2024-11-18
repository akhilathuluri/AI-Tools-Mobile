package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TextSummarizerViewModel(application: Application) : BaseToolViewModel(application, "Text Summarizer") {
    private val _summary = MutableStateFlow("")
    val summary: StateFlow<String> = _summary.asStateFlow()
    
    suspend fun summarizeText(
        text: String,
        length: String = "medium",
        style: String = "informative"
    ) {
        if (text.length < 50) {
            _error.value = "Text must be at least 50 characters long"
            return
        }
        
        val prompt = """
            Summarize the following text:
            Text: $text
            
            Please provide a $length length summary in a $style style.
            Focus on the key points and maintain the main message.
            
            Length guide:
            - Short: 2-3 sentences
            - Medium: 4-5 sentences
            - Long: 6-8 sentences
        """.trimIndent()
        
        _summary.value = generate(prompt)
    }
} 