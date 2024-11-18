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
import com.example.aitools.viewmodel.TextSummarizerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextSummarizerScreen(
    navController: NavController,
    apiKey: String,
    viewModel: TextSummarizerViewModel = viewModel()
) {
    var text by remember { mutableStateOf("") }
    var selectedLength by remember { mutableStateOf("medium") }
    var selectedStyle by remember { mutableStateOf("informative") }
    
    val coroutineScope = rememberCoroutineScope()
    val summary by viewModel.summary.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.initialize(apiKey)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Text Summarizer") },
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
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Text to Summarize") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 5,
                        supportingText = {
                            Text("Minimum 50 characters required")
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Summary Length",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SummaryOption(
                            text = "Short",
                            selected = selectedLength == "short",
                            onClick = { selectedLength = "short" }
                        )
                        SummaryOption(
                            text = "Medium",
                            selected = selectedLength == "medium",
                            onClick = { selectedLength = "medium" }
                        )
                        SummaryOption(
                            text = "Long",
                            selected = selectedLength == "long",
                            onClick = { selectedLength = "long" }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Summary Style",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SummaryOption(
                            text = "Informative",
                            selected = selectedStyle == "informative",
                            onClick = { selectedStyle = "informative" }
                        )
                        SummaryOption(
                            text = "Casual",
                            selected = selectedStyle == "casual",
                            onClick = { selectedStyle = "casual" }
                        )
                        SummaryOption(
                            text = "Technical",
                            selected = selectedStyle == "technical",
                            onClick = { selectedStyle = "technical" }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LoadingButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.summarizeText(text, selectedLength, selectedStyle)
                            }
                        },
                        isLoading = isLoading,
                        text = "Generate Summary",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = text.length >= 50
                    )
                }
            }
            
            ErrorMessage(error = error)
            
            if (summary.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ResponseField(
                    response = summary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SummaryOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) }
    )
} 