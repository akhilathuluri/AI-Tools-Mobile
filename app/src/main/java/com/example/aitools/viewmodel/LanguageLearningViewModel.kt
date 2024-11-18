package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LanguageLearningViewModel(application: Application) : BaseToolViewModel(application, "Language Learning") {
    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content.asStateFlow()
    
    suspend fun generateContent(
        targetLanguage: String,
        proficiencyLevel: String,
        learningType: String,
        topic: String = "",
        context: String = ""
    ) {
        val prompt = """
            As a language learning expert, create educational content for:
            
            Target Language: $targetLanguage
            Proficiency Level: $proficiencyLevel
            Type of Learning: $learningType
            Topic: ${topic.ifBlank { "general conversation" }}
            Context: ${context.ifBlank { "everyday situations" }}
            
            Based on the learning type, provide:
            
            ${when (learningType) {
                "conversation" -> """
                    1. A dialogue scenario with:
                    - Natural conversation flow
                    - Common expressions and idioms
                    - Cultural context notes
                    - Pronunciation tips
                    - Alternative phrases
                    - Grammar explanations for key structures
                """.trimIndent()
                
                "vocabulary" -> """
                    1. A themed vocabulary list with:
                    - Words and phrases in context
                    - Example sentences
                    - Common collocations
                    - Usage notes
                    - Memory tips
                    - Related expressions
                """.trimIndent()
                
                "grammar" -> """
                    1. Grammar explanation with:
                    - Clear rules and patterns
                    - Multiple examples
                    - Common mistakes to avoid
                    - Practice exercises
                    - Usage in different contexts
                    - Comparison with English structures
                """.trimIndent()
                
                "exercises" -> """
                    1. A set of exercises including:
                    - Fill in the blanks
                    - Sentence construction
                    - Translation practice
                    - Error correction
                    - Situation responses
                    - Answers with explanations
                """.trimIndent()
                
                else -> """
                    1. Mixed practice content with:
                    - Key phrases and vocabulary
                    - Simple dialogues
                    - Basic grammar points
                    - Cultural notes
                """.trimIndent()
            }}
            
            Additional requirements:
            - Include pronunciation guides where relevant
            - Add cultural context and usage notes
            - Provide memory tips and learning strategies
            - Include review questions or exercises
            - Suggest next learning steps
            
            Format the response with clear sections and proper markdown.
        """.trimIndent()
        
        _content.value = generate(prompt)
    }
} 