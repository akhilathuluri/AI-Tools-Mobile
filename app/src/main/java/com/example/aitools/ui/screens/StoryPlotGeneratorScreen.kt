package com.example.aitools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aitools.ui.components.*
import com.example.aitools.viewmodel.StoryPlotGeneratorViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StoryPlotGeneratorScreen(
    navController: NavController,
    apiKey: String,
    viewModel: StoryPlotGeneratorViewModel = viewModel()
) {
    var selectedGenre by remember { mutableStateOf("Fantasy") }
    var setting by remember { mutableStateOf("") }
    var characterCount by remember { mutableStateOf("3") }
    var selectedComplexity by remember { mutableStateOf("medium") }
    var additionalRequirements by remember { mutableStateOf("") }
    
    val selectedThemes = remember { mutableStateListOf<String>() }
    
    val coroutineScope = rememberCoroutineScope()
    val storyPlot by viewModel.storyPlot.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val genres = listOf("Fantasy", "Sci-Fi", "Mystery", "Romance", "Horror", "Adventure", "Drama")
    val themes = listOf(
        "Redemption", "Love", "Betrayal", "Coming of Age", "Power", "Justice",
        "Family", "Survival", "Identity", "Good vs Evil", "Technology", "Nature"
    )
    
    LaunchedEffect(Unit) {
        viewModel.initialize(apiKey)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Story Plot Generator") },
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
                        text = "Genre",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        genres.forEach { genre ->
                            FilterChip(
                                selected = selectedGenre == genre,
                                onClick = { selectedGenre = genre },
                                label = { Text(genre) }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Themes (Select Multiple)",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 3
                    ) {
                        themes.forEach { theme ->
                            FilterChip(
                                selected = theme in selectedThemes,
                                onClick = {
                                    if (theme in selectedThemes) {
                                        selectedThemes.remove(theme)
                                    } else {
                                        selectedThemes.add(theme)
                                    }
                                },
                                label = { Text(theme) }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = setting,
                        onValueChange = { setting = it },
                        label = { Text("Setting") },
                        modifier = Modifier.fillMaxWidth(),
                        supportingText = {
                            Text("Describe the world/environment of your story")
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = characterCount,
                        onValueChange = { 
                            if (it.isEmpty() || it.toIntOrNull() != null) {
                                characterCount = it
                            }
                        },
                        label = { Text("Number of Main Characters") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Plot Complexity",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedComplexity == "simple",
                            onClick = { selectedComplexity = "simple" },
                            label = { Text("Simple") }
                        )
                        FilterChip(
                            selected = selectedComplexity == "medium",
                            onClick = { selectedComplexity = "medium" },
                            label = { Text("Medium") }
                        )
                        FilterChip(
                            selected = selectedComplexity == "complex",
                            onClick = { selectedComplexity = "complex" },
                            label = { Text("Complex") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = additionalRequirements,
                        onValueChange = { additionalRequirements = it },
                        label = { Text("Additional Requirements (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        supportingText = {
                            Text("Any specific elements you want to include")
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LoadingButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.generateStoryPlot(
                                    selectedGenre,
                                    selectedThemes,
                                    setting,
                                    characterCount.toIntOrNull() ?: 3,
                                    selectedComplexity,
                                    additionalRequirements
                                )
                            }
                        },
                        isLoading = isLoading,
                        text = "Generate Story Plot",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedThemes.isNotEmpty() && setting.isNotBlank()
                    )
                }
            }
            
            ErrorMessage(error = error)
            
            if (storyPlot.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ResponseField(
                    response = storyPlot,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
 