package com.example.aitools.ui.screens

import androidx.compose.foundation.horizontalScroll
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
import com.example.aitools.viewmodel.CodeAssistantViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeAssistantScreen(
    navController: NavController,
    apiKey: String,
    viewModel: CodeAssistantViewModel = viewModel()
) {
    var query by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("Python") }
    var selectedType by remember { mutableStateOf("explanation") }
    
    val coroutineScope = rememberCoroutineScope()
    val response by viewModel.response.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val languages = listOf("Python", "JavaScript", "Java", "Kotlin", "C++", "Other")
    val helpTypes = listOf(
        "explanation" to "Explain Concept",
        "debug" to "Debug Code",
        "optimize" to "Optimize Code",
        "example" to "Show Examples"
    )
    
    LaunchedEffect(Unit) {
        viewModel.initialize(apiKey)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Code Assistant") },
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
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Code Query") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        supportingText = {
                            Text("Describe your coding question or paste code")
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Programming Language",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        languages.forEach { language ->
                            FilterChip(
                                selected = selectedLanguage == language,
                                onClick = { selectedLanguage = language },
                                label = { Text(language) }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Type of Help",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        helpTypes.forEach { (type, label) ->
                            FilterChip(
                                selected = selectedType == type,
                                onClick = { selectedType = type },
                                label = { Text(label) }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LoadingButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.generateCodeHelp(
                                    query,
                                    selectedLanguage,
                                    selectedType
                                )
                            }
                        },
                        isLoading = isLoading,
                        text = "Get Help",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = query.length >= 10
                    )
                }
            }
            
            ErrorMessage(error = error)
            
            if (response.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ResponseField(
                    response = response,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
} 