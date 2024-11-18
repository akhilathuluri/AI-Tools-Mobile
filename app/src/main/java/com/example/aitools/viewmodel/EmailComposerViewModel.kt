package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EmailComposerViewModel(application: Application) : BaseToolViewModel(application, "Email Composer") {
    private val _emailContent = MutableStateFlow("")
    val emailContent: StateFlow<String> = _emailContent.asStateFlow()
    
    suspend fun generateEmail(
        recipient: String,
        subject: String,
        tone: String,
        keyPoints: String
    ) {
        val prompt = """
            Write a professional email with the following details:
            To: $recipient
            Subject: $subject
            Tone: $tone
            Key Points to Include: $keyPoints
            
            Please format the email professionally and ensure it's concise and effective.
        """.trimIndent()
        
        _emailContent.value = generate(prompt)
    }
} 