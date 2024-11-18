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
import com.example.aitools.viewmodel.HashtagGeneratorViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HashtagGeneratorScreen(
    navController: NavController,
    apiKey: String,
    viewModel: HashtagGeneratorViewModel = viewModel()
) {
    var content by remember { mutableStateOf("") }
    var hashtagCount by remember { mutableStateOf("30") }
    var selectedStyle by remember { mutableStateOf("trending") }
    
    val coroutineScope = rememberCoroutineScope()
    val hashtags by viewModel.hashtags.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.initialize(apiKey)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hashtag Generator") },
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
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Content Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        supportingText = {
                            Text("Describe your post content")
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Hashtag Style",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedStyle == "trending",
                            onClick = { selectedStyle = "trending" },
                            label = { Text("Trending") }
                        )
                        FilterChip(
                            selected = selectedStyle == "niche",
                            onClick = { selectedStyle = "niche" },
                            label = { Text("Niche") }
                        )
                        FilterChip(
                            selected = selectedStyle == "mixed",
                            onClick = { selectedStyle = "mixed" },
                            label = { Text("Mixed") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = hashtagCount,
                        onValueChange = { 
                            if (it.isEmpty() || it.toIntOrNull() != null) {
                                hashtagCount = it
                            }
                        },
                        label = { Text("Number of Hashtags") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LoadingButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.generateHashtags(
                                    content,
                                    hashtagCount.toIntOrNull() ?: 30,
                                    selectedStyle
                                )
                            }
                        },
                        isLoading = isLoading,
                        text = "Generate Hashtags",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = content.length >= 10
                    )
                }
            }
            
            ErrorMessage(error = error)
            
            if (hashtags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ResponseField(
                    response = hashtags,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
} 