package com.example.aitools.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aitools.ui.components.*
import com.example.aitools.utils.BitmapUtils
import com.example.aitools.viewmodel.ImageAnalyzerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageAnalyzerScreen(
    navController: NavController,
    apiKey: String,
    viewModel: ImageAnalyzerViewModel = viewModel()
) {
    var selectedAnalysisType by remember { mutableStateOf("general") }
    var customQuery by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val analysis by viewModel.analysis.collectAsState()
    val selectedImage by viewModel.selectedImage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            BitmapUtils.getBitmapFromUri(context, it)?.let { bitmap ->
                viewModel.setImage(bitmap)
            }
        }
    }
    
    LaunchedEffect(Unit) {
        viewModel.initialize(apiKey)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Image Analyzer") },
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
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (selectedImage != null) {
                        Image(
                            bitmap = selectedImage!!.asImageBitmap(),
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        FilledTonalButton(
                            onClick = { imagePicker.launch("image/*") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Image,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Select Image")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Analysis Type",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedAnalysisType == "general",
                            onClick = { selectedAnalysisType = "general" },
                            label = { Text("General") }
                        )
                        FilterChip(
                            selected = selectedAnalysisType == "technical",
                            onClick = { selectedAnalysisType = "technical" },
                            label = { Text("Technical") }
                        )
                        FilterChip(
                            selected = selectedAnalysisType == "custom",
                            onClick = { selectedAnalysisType = "custom" },
                            label = { Text("Custom") }
                        )
                    }
                    
                    if (selectedAnalysisType == "custom") {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = customQuery,
                            onValueChange = { customQuery = it },
                            label = { Text("Custom Analysis Query") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    LoadingButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.analyzeImage(
                                    selectedAnalysisType,
                                    if (selectedAnalysisType == "custom") customQuery else ""
                                )
                            }
                        },
                        isLoading = isLoading,
                        text = "Analyze Image",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedImage != null
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