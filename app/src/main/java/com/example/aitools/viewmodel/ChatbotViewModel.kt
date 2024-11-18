package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatbotViewModel(application: Application) : BaseToolViewModel(application, "AI Chatbot") {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    private val conversationContext = StringBuilder()
    
    suspend fun sendMessage(userMessage: String) {
        if (userMessage.isBlank()) {
            _error.value = "Message cannot be empty"
            return
        }
        
        // Add user message to the list
        _messages.value = _messages.value + ChatMessage(userMessage, true)
        
        // Update conversation context
        conversationContext.append("\nUser: $userMessage")
        
        val prompt = """
            Previous conversation:
            $conversationContext
            
            Please provide a helpful, natural, and contextually relevant response.
            Keep the tone conversational but informative.
            If you don't know something, admit it.
            If the user asks something unclear, ask for clarification.
            
            Respond in a direct way without mentioning that you're an AI.
        """.trimIndent()
        
        val response = generate(prompt)
        
        // Update conversation context with bot's response
        conversationContext.append("\nAssistant: $response")
        
        // Add bot response to the list
        _messages.value = _messages.value + ChatMessage(response, false)
    }
    
    fun clearChat() {
        _messages.value = emptyList()
        conversationContext.clear()
    }
}

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) 