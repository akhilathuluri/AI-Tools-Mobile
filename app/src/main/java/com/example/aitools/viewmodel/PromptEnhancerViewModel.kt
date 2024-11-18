package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PromptEnhancerViewModel(application: Application) : BaseToolViewModel(application, "Prompt Enhancer") {
    private val _enhancedPrompt = MutableStateFlow("")
    val enhancedPrompt: StateFlow<String> = _enhancedPrompt.asStateFlow()
    
    suspend fun enhancePrompt(
        originalPrompt: String,
        goal: String,
        style: String = "detailed"
    ) {
        if (originalPrompt.length < 5) {
            _error.value = "Prompt must be at least 5 characters long"
            return
        }
        
        val prompt = """
            As an AI prompt engineering expert, enhance the following prompt to get better results:
            
            Original Prompt: $originalPrompt
            Goal: $goal
            Style Preference: $style
            
            Please provide:
            1. An enhanced version of the prompt
            2. Explanation of improvements made
            3. Additional tips for better results
            4. Alternative versions (if applicable)
            
            Focus on making the prompt:
            - More specific and detailed
            - Better structured
            - Including relevant context
            - Using clear instructions
            - Incorporating best practices
            
            Format the response in a clear, organized way.
        """.trimIndent()
        
        _enhancedPrompt.value = generate(prompt)
    }
} 