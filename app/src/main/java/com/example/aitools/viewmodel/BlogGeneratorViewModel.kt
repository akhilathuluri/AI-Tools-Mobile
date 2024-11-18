package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BlogGeneratorViewModel(application: Application) : BaseToolViewModel(application, "Blog Generator") {
    private val _blogContent = MutableStateFlow("")
    val blogContent: StateFlow<String> = _blogContent.asStateFlow()
    
    suspend fun generateBlogContent(
        topic: String,
        targetAudience: String,
        tone: String = "professional",
        length: String = "medium",
        includeKeywords: String = ""
    ) {
        if (topic.length < 5) {
            _error.value = "Topic must be at least 5 characters long"
            return
        }
        
        val prompt = """
            Generate a comprehensive blog post with the following details:
            
            Topic: $topic
            Target Audience: $targetAudience
            Tone: $tone
            Length: $length
            Keywords to Include: $includeKeywords
            
            Please structure the blog post with:
            1. Engaging title
            2. Introduction that hooks the reader
            3. Main content sections with subheadings
            4. Practical examples or case studies
            5. Key takeaways or conclusion
            6. Call to action
            
            Additional requirements:
            - Use a $tone writing style
            - Target content length: ${when(length) {
                "short" -> "800-1000 words"
                "medium" -> "1200-1500 words"
                "long" -> "2000+ words"
                else -> "1200-1500 words"
            }}
            - Naturally incorporate SEO keywords: $includeKeywords
            - Include relevant statistics or data points
            - Make it engaging and valuable for $targetAudience
            
            Format the post with proper markdown for headings and sections.
        """.trimIndent()
        
        _blogContent.value = generate(prompt)
    }
} 