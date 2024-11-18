package com.example.aitools.data

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiClient(private val apiKey: String) {
    private val textModel by lazy {
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey
        )
    }

    private val visionModel by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )
    }

    suspend fun generateText(prompt: String): String = withContext(Dispatchers.IO) {
        try {
            val response = textModel.generateContent(prompt)
            response.text ?: "No response generated"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
    
    suspend fun analyzeImage(
        image: Bitmap,
        prompt: String
    ): String = withContext(Dispatchers.IO) {
        try {
            val inputContent = content {
                image(image)
                text(prompt)
            }
            
            val response = visionModel.generateContent(inputContent)
            response.text ?: "No analysis generated"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
} 