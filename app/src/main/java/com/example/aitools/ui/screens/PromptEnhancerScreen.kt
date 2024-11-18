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
import com.example.aitools.viewmodel.PromptEnhancerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptEnhancerScreen(
    navController: NavController,
    apiKey: String,
    viewModel: PromptEnhancerViewModel = viewModel()
) {
    var originalPrompt by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("detailed") }
    
    val coroutineScope = rememberCoroutineScope()
    val enhancedPrompt by viewModel.enhancedPrompt.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.initialize(apiKey)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prompt Enhancer") },
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
                    OutlinedTextField(
                        value = originalPrompt,
                        onValueChange = { originalPrompt = it },
                        label = { Text("Original Prompt") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        supportingText = {
                            Text("Enter the prompt you want to enhance")
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = goal,
                        onValueChange = { goal = it },
                        label = { Text("What's your goal?") },
                        modifier = Modifier.fillMaxWidth(),
                        supportingText = {
                            Text("Describe what you want to achieve")
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Style Preference",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedStyle == "detailed",
                            onClick = { selectedStyle = "detailed" },
                            label = { Text("Detailed") }
                        )
                        FilterChip(
                            selected = selectedStyle == "concise",
                            onClick = { selectedStyle = "concise" },
                            label = { Text("Concise") }
                        )
                        FilterChip(
                            selected = selectedStyle == "creative",
                            onClick = { selectedStyle = "creative" },
                            label = { Text("Creative") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LoadingButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.enhancePrompt(
                                    originalPrompt,
                                    goal,
                                    selectedStyle
                                )
                            }
                        },
                        isLoading = isLoading,
                        text = "Enhance Prompt",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = originalPrompt.length >= 5 && goal.isNotBlank()
                    )
                }
            }
            
            ErrorMessage(error = error)
            
            if (enhancedPrompt.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ResponseField(
                    response = enhancedPrompt,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
} 