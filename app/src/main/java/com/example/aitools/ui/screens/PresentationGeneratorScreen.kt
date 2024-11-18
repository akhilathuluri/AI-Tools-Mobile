package com.example.aitools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aitools.ui.components.*
import com.example.aitools.viewmodel.PresentationGeneratorViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PresentationGeneratorScreen(
    navController: NavController,
    apiKey: String,
    viewModel: PresentationGeneratorViewModel = viewModel()
) {
    var topic by remember { mutableStateOf("") }
    var audience by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("30") }
    var selectedStyle by remember { mutableStateOf("professional") }
    var purpose by remember { mutableStateOf("") }
    var keyPoints by remember { mutableStateOf("") }
    var includeDesign by remember { mutableStateOf(true) }
    
    val coroutineScope = rememberCoroutineScope()
    val presentationContent by viewModel.presentationContent.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val styles = listOf(
        "professional" to "Professional",
        "creative" to "Creative",
        "minimalist" to "Minimalist",
        "academic" to "Academic",
        "storytelling" to "Storytelling"
    )
    
    LaunchedEffect(Unit) {
        viewModel.initialize(apiKey)
    }
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("Presentation Generator")
                        Text(
                            "Create engaging presentations with AI",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
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
            // Basic Information Section
            SectionCard(
                title = "Basic Information",
                icon = Icons.Outlined.Info
            ) {
                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Presentation Topic") },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        Text("What is your presentation about?")
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = audience,
                    onValueChange = { audience = it },
                    label = { Text("Target Audience") },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        Text("Who will be attending?")
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = duration,
                    onValueChange = { 
                        if (it.isEmpty() || it.toIntOrNull() != null) {
                            duration = it
                        }
                    },
                    label = { Text("Duration (minutes)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Style Section
            SectionCard(
                title = "Presentation Style",
                icon = Icons.Outlined.Style
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 3
                ) {
                    styles.forEach { (style, label) ->
                        FilterChip(
                            selected = selectedStyle == style,
                            onClick = { selectedStyle = style },
                            label = { Text(label) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Content Details Section
            SectionCard(
                title = "Content Details",
                icon = Icons.Outlined.Description
            ) {
                OutlinedTextField(
                    value = purpose,
                    onValueChange = { purpose = it },
                    label = { Text("Presentation Purpose") },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        Text("What do you want to achieve?")
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = keyPoints,
                    onValueChange = { keyPoints = it },
                    label = { Text("Key Points (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    supportingText = {
                        Text("Main points you want to cover")
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = includeDesign,
                        onCheckedChange = { includeDesign = it }
                    )
                    Text("Include design suggestions")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LoadingButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.generatePresentation(
                            topic,
                            audience,
                            duration.toIntOrNull() ?: 30,
                            selectedStyle,
                            purpose,
                            keyPoints,
                            includeDesign
                        )
                    }
                },
                isLoading = isLoading,
                text = "Generate Presentation",
                modifier = Modifier.fillMaxWidth(),
                enabled = topic.isNotBlank() && audience.isNotBlank() && purpose.isNotBlank()
            )
            
            ErrorMessage(error = error)
            
            if (presentationContent.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ResponseField(
                    response = presentationContent,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
} 