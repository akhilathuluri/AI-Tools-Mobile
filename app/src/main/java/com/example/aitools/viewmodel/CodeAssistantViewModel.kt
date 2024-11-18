package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CodeAssistantViewModel(application: Application) : BaseToolViewModel(application, "Code Assistant") {
    private val _response = MutableStateFlow("")
    val response: StateFlow<String> = _response.asStateFlow()
    
    suspend fun generateCodeHelp(
        query: String,
        language: String,
        type: String = "explanation"
    ) {
        if (query.length < 10) {
            _error.value = "Query must be at least 10 characters long"
            return
        }
        
        val prompt = """
            As an expert programmer, help with the following code-related query:
            Query: $query
            Programming Language: $language
            Type of Help: $type
            
            ${when (type) {
                "explanation" -> "Please explain the concept clearly with examples."
                "debug" -> "Analyze the code for potential issues and provide solutions."
                "optimize" -> "Suggest optimizations and best practices."
                else -> "Provide a detailed response with examples."
            }}
            
            Format the response with proper markdown for code blocks and sections.
        """.trimIndent()
        
        _response.value = generate(prompt)
    }
} 