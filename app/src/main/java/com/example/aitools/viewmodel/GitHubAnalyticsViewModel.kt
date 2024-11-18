package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GitHubAnalyticsViewModel(application: Application) : BaseToolViewModel(application, "GitHub Analytics") {
    private val _analysis = MutableStateFlow("")
    val analysis: StateFlow<String> = _analysis.asStateFlow()
    
    suspend fun analyzeGitHub(
        username: String,
        analysisType: String = "profile",
        specificRepo: String = ""
    ) {
        if (username.isBlank()) {
            _error.value = "Please enter a GitHub username"
            return
        }
        
        val prompt = when (analysisType) {
            "profile" -> """
                Analyze the GitHub profile for user: $username
                
                Please provide a comprehensive analysis including:
                - Overall profile assessment
                - Main technologies and languages used
                - Activity patterns and contribution trends
                - Notable projects and contributions
                - Suggestions for profile improvement
                
                Format the response in a clear, structured way.
            """.trimIndent()
            
            "repository" -> """
                Analyze the GitHub repository: $username/$specificRepo
                
                Please provide a detailed analysis including:
                - Project overview and purpose
                - Code quality and structure assessment
                - Documentation quality
                - Activity and maintenance status
                - Notable features and potential improvements
                
                Format the response in a clear, structured way.
            """.trimIndent()
            
            else -> "Analyze the GitHub profile: $username"
        }
        
        _analysis.value = generate(prompt)
    }
} 