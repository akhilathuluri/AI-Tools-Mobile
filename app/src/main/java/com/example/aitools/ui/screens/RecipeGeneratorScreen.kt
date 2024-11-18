package com.example.aitools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aitools.ui.components.*
import com.example.aitools.viewmodel.RecipeGeneratorViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RecipeGeneratorScreen(
    navController: NavController,
    apiKey: String,
    viewModel: RecipeGeneratorViewModel = viewModel()
) {
    var newIngredient by remember { mutableStateOf("") }
    var selectedCuisine by remember { mutableStateOf("Italian") }
    var cookingTime by remember { mutableStateOf("30-60 minutes") }
    var skillLevel by remember { mutableStateOf("intermediate") }
    var servings by remember { mutableStateOf("4") }
    
    val ingredients = remember { mutableStateListOf<String>() }
    val selectedDiets = remember { mutableStateListOf<String>() }
    
    val coroutineScope = rememberCoroutineScope()
    val recipe by viewModel.recipe.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val cuisines = listOf("Italian", "Chinese", "Indian", "Mexican", "Japanese", "French", "Mediterranean")
    val dietaryRestrictions = listOf(
        "Vegetarian", "Vegan", "Gluten-Free", "Dairy-Free",
        "Low-Carb", "Keto", "Paleo", "Nut-Free"
    )
    
    LaunchedEffect(Unit) {
        viewModel.initialize(apiKey)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Generator") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Main Ingredients",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newIngredient,
                            onValueChange = { newIngredient = it },
                            label = { Text("Add Ingredient") },
                            modifier = Modifier.weight(1f)
                        )
                        
                        IconButton(
                            onClick = {
                                if (newIngredient.isNotBlank()) {
                                    ingredients.add(newIngredient)
                                    newIngredient = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add ingredient")
                        }
                    }
                    
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ingredients.forEach { ingredient ->
                            AssistChip(
                                onClick = { ingredients.remove(ingredient) },
                                label = { Text(ingredient) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Remove"
                                    )
                                }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Cuisine Type",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        cuisines.forEach { cuisine ->
                            FilterChip(
                                selected = selectedCuisine == cuisine,
                                onClick = { selectedCuisine = cuisine },
                                label = { Text(cuisine) }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Dietary Restrictions",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        dietaryRestrictions.forEach { diet ->
                            FilterChip(
                                selected = diet in selectedDiets,
                                onClick = {
                                    if (diet in selectedDiets) {
                                        selectedDiets.remove(diet)
                                    } else {
                                        selectedDiets.add(diet)
                                    }
                                },
                                label = { Text(diet) }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Cooking Time",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = cookingTime == "under 30 minutes",
                            onClick = { cookingTime = "under 30 minutes" },
                            label = { Text("Quick (<30 min)") }
                        )
                        FilterChip(
                            selected = cookingTime == "30-60 minutes",
                            onClick = { cookingTime = "30-60 minutes" },
                            label = { Text("Medium (30-60 min)") }
                        )
                        FilterChip(
                            selected = cookingTime == "over 60 minutes",
                            onClick = { cookingTime = "over 60 minutes" },
                            label = { Text("Long (>60 min)") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Skill Level",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = skillLevel == "beginner",
                            onClick = { skillLevel = "beginner" },
                            label = { Text("Beginner") }
                        )
                        FilterChip(
                            selected = skillLevel == "intermediate",
                            onClick = { skillLevel = "intermediate" },
                            label = { Text("Intermediate") }
                        )
                        FilterChip(
                            selected = skillLevel == "advanced",
                            onClick = { skillLevel = "advanced" },
                            label = { Text("Advanced") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = servings,
                        onValueChange = { 
                            if (it.isEmpty() || it.toIntOrNull() != null) {
                                servings = it
                            }
                        },
                        label = { Text("Number of Servings") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LoadingButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.generateRecipe(
                                    ingredients,
                                    selectedCuisine,
                                    selectedDiets,
                                    cookingTime,
                                    skillLevel,
                                    servings.toIntOrNull() ?: 4
                                )
                            }
                        },
                        isLoading = isLoading,
                        text = "Generate Recipe",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = ingredients.isNotEmpty()
                    )
                }
            }
            
            ErrorMessage(error = error)
            
            if (recipe.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ResponseField(
                    response = recipe,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
} 