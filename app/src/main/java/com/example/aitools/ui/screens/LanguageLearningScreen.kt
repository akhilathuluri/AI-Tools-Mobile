package com.example.aitools.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aitools.ui.components.*
import com.example.aitools.viewmodel.LanguageLearningViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LanguageLearningScreen(
    navController: NavController,
    apiKey: String,
    viewModel: LanguageLearningViewModel = viewModel()
) {
    var selectedLanguage by remember { mutableStateOf("Spanish") }
    var selectedLevel by remember { mutableStateOf("beginner") }
    var selectedType by remember { mutableStateOf("conversation") }
    var topic by remember { mutableStateOf("") }
    var context by remember { mutableStateOf("") }
    
    val coroutineScope = rememberCoroutineScope()
    val content by viewModel.content.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val languages = listOf(
        "Spanish" to Icons.Outlined.Language,
        "French" to Icons.Outlined.Language,
        "German" to Icons.Outlined.Language,
        "Italian" to Icons.Outlined.Language,
        "Japanese" to Icons.Outlined.Language,
        "Chinese" to Icons.Outlined.Language,
        "Korean" to Icons.Outlined.Language,
        "Portuguese" to Icons.Outlined.Language,
        "Russian" to Icons.Outlined.Language
    )
    
    val levels = listOf(
        Triple("beginner", "Beginner (A1-A2)", Icons.Outlined.Star),
        Triple("intermediate", "Intermediate (B1-B2)", Icons.Outlined.StarHalf),
        Triple("advanced", "Advanced (C1-C2)", Icons.Outlined.StarBorder)
    )
    
    val types = listOf(
        Triple("conversation", "Conversation Practice", Icons.Outlined.Chat),
        Triple("vocabulary", "Vocabulary Building", Icons.Outlined.List),
        Triple("grammar", "Grammar Focus", Icons.Outlined.Book),
        Triple("exercises", "Practice Exercises", Icons.Outlined.Edit)
    )
    
    LaunchedEffect(Unit) {
        viewModel.initialize(apiKey)
    }
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("Language Learning Assistant")
                        Text(
                            "Master new languages with AI",
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Language Selection Section
            SectionCard(
                title = "Choose Language",
                icon = Icons.Outlined.Translate
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 3
                ) {
                    languages.forEach { (language, icon) ->
                        FilterChip(
                            selected = selectedLanguage == language,
                            onClick = { selectedLanguage = language },
                            label = { Text(language) },
                            leadingIcon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Proficiency Level Section
            SectionCard(
                title = "Proficiency Level",
                icon = Icons.Outlined.Grade
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    levels.forEach { (level, label, icon) ->
                        FilterChip(
                            selected = selectedLevel == level,
                            onClick = { selectedLevel = level },
                            label = { Text(label) },
                            leadingIcon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Learning Type Section
            SectionCard(
                title = "Learning Type",
                icon = Icons.Outlined.School
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    types.forEach { (type, label, icon) ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(label) },
                            leadingIcon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Custom Content Section
            SectionCard(
                title = "Customize Learning",
                icon = Icons.Outlined.Settings
            ) {
                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Specific Topic (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        Text("e.g., Travel, Food, Business, etc.")
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = context,
                    onValueChange = { context = it },
                    label = { Text("Learning Context (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    supportingText = {
                        Text("Describe your learning goals or situation")
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LoadingButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.generateContent(
                            selectedLanguage,
                            selectedLevel,
                            selectedType,
                            topic,
                            context
                        )
                    }
                },
                isLoading = isLoading,
                text = "Generate Learning Content",
                modifier = Modifier.fillMaxWidth()
            )
            
            ErrorMessage(error = error)
            
            if (content.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ResponseField(
                    response = content,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            content()
        }
    }
} 