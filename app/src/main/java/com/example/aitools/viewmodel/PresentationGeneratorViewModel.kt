package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PresentationGeneratorViewModel(application: Application) : BaseToolViewModel(application, "Presentation Generator") {
    private val _presentationContent = MutableStateFlow("")
    val presentationContent: StateFlow<String> = _presentationContent.asStateFlow()
    
    suspend fun generatePresentation(
        topic: String,
        audience: String,
        duration: Int,
        style: String,
        purpose: String,
        keyPoints: String = "",
        includeDesign: Boolean = true
    ) {
        if (topic.length < 5) {
            _error.value = "Topic must be at least 5 characters long"
            return
        }
        
        val prompt = """
            As a professional presentation designer, create a comprehensive presentation outline with the following requirements:
            
            Topic: $topic
            Target Audience: $audience
            Duration: $duration minutes
            Presentation Style: $style
            Purpose: $purpose
            Key Points to Include: $keyPoints
            
            Please provide:
            
            1. Title Slide
            - Engaging title
            - Impactful subtitle
            - Hook statement
            
            2. Presentation Structure
            - Introduction (with attention grabber)
            - Main sections with timing allocation
            - Conclusion and call to action
            
            3. Content Details for Each Section
            - Key messages
            - Supporting points
            - Data/statistics to include
            - Storytelling elements
            
            4. Visual Design Suggestions
            - Color scheme recommendations
            - Layout ideas for key slides
            - Visual elements to include
            - Typography recommendations
            
            5. Engagement Elements
            - Interactive components
            - Questions for audience engagement
            - Discussion points
            - Activities or exercises
            
            6. Presentation Tips
            - Delivery suggestions
            - Timing guidelines
            - Transition techniques
            - Body language recommendations
            
            Additional Requirements:
            - Keep content concise and impactful
            - Include memorable takeaways
            - Balance text and visual elements
            - Consider audience attention span
            - Incorporate storytelling techniques
            
            Format the response with clear sections and proper markdown for easy reading and implementation.
        """.trimIndent()
        
        _presentationContent.value = generate(prompt)
    }
} 