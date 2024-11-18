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
import com.example.aitools.viewmodel.BlogGeneratorViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogGeneratorScreen(
    navController: NavController,
    apiKey: String,
    viewModel: BlogGeneratorViewModel = viewModel()
) {
    var topic by remember { mutableStateOf("") }
    var targetAudience by remember { mutableStateOf("") }
    var selectedTone by remember { mutableStateOf("professional") }
    var selectedLength by remember { mutableStateOf("medium") }
    var keywords by remember { mutableStateOf("") }
    
    val coroutineScope = rememberCoroutineScope()
    val blogContent by viewModel.blogContent.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.initialize(apiKey)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Blog Content Generator") },
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
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Blog Topic") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        supportingText = {
                            Text("What's your blog post about?")
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = targetAudience,
                        onValueChange = { targetAudience = it },
                        label = { Text("Target Audience") },
                        modifier = Modifier.fillMaxWidth(),
                        supportingText = {
                            Text("Who are you writing for?")
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Writing Tone",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedTone == "professional",
                            onClick = { selectedTone = "professional" },
                            label = { Text("Professional") }
                        )
                        FilterChip(
                            selected = selectedTone == "casual",
                            onClick = { selectedTone = "casual" },
                            label = { Text("Casual") }
                        )
                        FilterChip(
                            selected = selectedTone == "educational",
                            onClick = { selectedTone = "educational" },
                            label = { Text("Educational") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Content Length",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedLength == "short",
                            onClick = { selectedLength = "short" },
                            label = { Text("Short") }
                        )
                        FilterChip(
                            selected = selectedLength == "medium",
                            onClick = { selectedLength = "medium" },
                            label = { Text("Medium") }
                        )
                        FilterChip(
                            selected = selectedLength == "long",
                            onClick = { selectedLength = "long" },
                            label = { Text("Long") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = keywords,
                        onValueChange = { keywords = it },
                        label = { Text("Keywords (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        supportingText = {
                            Text("Separate keywords with commas")
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LoadingButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.generateBlogContent(
                                    topic,
                                    targetAudience,
                                    selectedTone,
                                    selectedLength,
                                    keywords
                                )
                            }
                        },
                        isLoading = isLoading,
                        text = "Generate Blog Content",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = topic.length >= 5 && targetAudience.isNotBlank()
                    )
                }
            }
            
            ErrorMessage(error = error)
            
            if (blogContent.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ResponseField(
                    response = blogContent,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
} 