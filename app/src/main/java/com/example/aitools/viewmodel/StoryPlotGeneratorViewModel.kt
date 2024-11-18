package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StoryPlotGeneratorViewModel(application: Application) : BaseToolViewModel(application, "Story Plot Generator") {
    private val _storyPlot = MutableStateFlow("")
    val storyPlot: StateFlow<String> = _storyPlot.asStateFlow()
    
    suspend fun generateStoryPlot(
        genre: String,
        themes: List<String>,
        setting: String,
        characterCount: Int,
        complexity: String = "medium",
        additionalRequirements: String = ""
    ) {
        if (themes.isEmpty()) {
            _error.value = "Please select at least one theme"
            return
        }
        
        val prompt = """
            As a master storyteller, create a unique and engaging story plot with the following elements:
            
            Genre: $genre
            Themes: ${themes.joinToString(", ")}
            Setting: $setting
            Number of Main Characters: $characterCount
            Complexity Level: $complexity
            Additional Requirements: $additionalRequirements
            
            Please provide:
            1. Story Title
            2. High-Level Plot Summary (2-3 paragraphs)
            3. Main Characters Overview
            - Brief descriptions and roles
            - Key relationships and conflicts
            4. Plot Structure
            - Setup/Inciting Incident
            - Major Plot Points
            - Climax Elements
            - Resolution Ideas
            5. Unique Story Elements
            - Plot twists or unexpected elements
            - Thematic symbolism
            - Key scenes or moments
            
            Make the story:
            - Original and engaging
            - Logically structured
            - Emotionally resonant
            - Thematically coherent
            - Suitable for the specified genre
            
            Complexity guide:
            - Simple: Clear, linear plot with straightforward conflicts
            - Medium: Multiple subplots and character arcs
            - Complex: Intricate plot layers, multiple timelines, or complex character relationships
            
            Format the response with clear sections and proper markdown.
        """.trimIndent()
        
        _storyPlot.value = generate(prompt)
    }
} 