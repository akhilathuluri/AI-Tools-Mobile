package com.example.aitools.viewmodel

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeGeneratorViewModel(application: Application) : BaseToolViewModel(application, "Recipe Generator") {
    private val _recipe = MutableStateFlow("")
    val recipe: StateFlow<String> = _recipe.asStateFlow()
    
    suspend fun generateRecipe(
        mainIngredients: List<String>,
        cuisine: String,
        dietaryRestrictions: List<String>,
        cookingTime: String,
        skillLevel: String,
        servings: Int
    ) {
        if (mainIngredients.isEmpty()) {
            _error.value = "Please enter at least one main ingredient"
            return
        }
        
        val prompt = """
            As a professional chef, create a unique and detailed recipe with the following requirements:
            
            Main Ingredients: ${mainIngredients.joinToString(", ")}
            Cuisine Type: $cuisine
            Dietary Restrictions: ${dietaryRestrictions.joinToString(", ")}
            Cooking Time: $cookingTime
            Skill Level: $skillLevel
            Servings: $servings
            
            Please provide:
            1. Recipe Title
            2. Brief Description
            3. Preparation Time & Cooking Time
            4. Complete Ingredients List with Measurements
            5. Step-by-Step Instructions
            6. Nutritional Information (approximate)
            7. Chef's Tips and Variations
            8. Storage and Reheating Instructions
            
            Additional Requirements:
            - Make the recipe easy to follow
            - Include cooking techniques explanation
            - Suggest possible ingredient substitutions
            - Add presentation tips
            - Include any special equipment needed
            
            Format the response with clear sections and proper markdown.
        """.trimIndent()
        
        _recipe.value = generate(prompt)
    }
} 