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
import com.example.aitools.viewmodel.GitHubAnalyticsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubAnalyticsScreen(
    navController: NavController,
    apiKey: String,
    viewModel: GitHubAnalyticsViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var selectedAnalysisType by remember { mutableStateOf("profile") }
    var repoName by remember { mutableStateOf("") }
    
    val coroutineScope = rememberCoroutineScope()
    val analysis by viewModel.analysis.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.initialize(apiKey)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GitHub Analytics") },
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
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("GitHub Username") },
                        modifier = Modifier.fillMaxWidth(),
                        supportingText = {
                            Text("Enter a GitHub username to analyze")
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Analysis Type",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedAnalysisType == "profile",
                            onClick = { selectedAnalysisType = "profile" },
                            label = { Text("Profile Analysis") }
                        )
                        FilterChip(
                            selected = selectedAnalysisType == "repository",
                            onClick = { selectedAnalysisType = "repository" },
                            label = { Text("Repository Analysis") }
                        )
                    }
                    
                    if (selectedAnalysisType == "repository") {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = repoName,
                            onValueChange = { repoName = it },
                            label = { Text("Repository Name") },
                            modifier = Modifier.fillMaxWidth(),
                            supportingText = {
                                Text("Enter the repository name to analyze")
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LoadingButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.analyzeGitHub(
                                    username,
                                    selectedAnalysisType,
                                    repoName
                                )
                            }
                        },
                        isLoading = isLoading,
                        text = "Analyze",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = username.isNotBlank() && 
                                (selectedAnalysisType != "repository" || repoName.isNotBlank())
                    )
                }
            }
            
            ErrorMessage(error = error)
            
            if (analysis.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ResponseField(
                    response = analysis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
} 