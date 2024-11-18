package com.example.aitools.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    apiKey: String?
) {
    val tools = listOf(
        Tool(
            name = "Developer Profile",
            description = "About the developer and app information",
            route = "developer",
            icon = Icons.Outlined.Person
        ),
        Tool(
            name = "Email Composer",
            description = "Write professional emails with AI assistance",
            route = "email",
            icon = Icons.Outlined.Email
        ),
        Tool(
            name = "Text Summarizer",
            description = "Get concise summaries of long texts",
            route = "summarize",
            icon = Icons.Outlined.Subject
        ),
        Tool(
            name = "Hashtag Generator",
            description = "Generate relevant Instagram hashtags",
            route = "hashtags",
            icon = Icons.Outlined.Tag
        ),
        Tool(
            name = "Image Analyzer",
            description = "Analyze images using AI",
            route = "image",
            icon = Icons.Outlined.Image
        ),
        Tool(
            name = "GitHub Analytics",
            description = "Analyze GitHub profiles",
            route = "github",
            icon = Icons.Outlined.Code
        ),
        Tool(
            name = "Code Assistant",
            description = "Get help with coding",
            route = "code",
            icon = Icons.Outlined.Terminal
        ),
        Tool(
            name = "Prompt Enhancer",
            description = "Improve your AI prompts for better results",
            route = "prompt",
            icon = Icons.Outlined.AutoFixHigh
        ),
        Tool(
            name = "Blog Generator",
            description = "Create engaging blog content",
            route = "blog",
            icon = Icons.Outlined.Article
        ),
        Tool(
            name = "Story Plot Generator",
            description = "Create unique story plots and characters",
            route = "story",
            icon = Icons.Outlined.AutoStories
        ),
        Tool(
            name = "Recipe Generator",
            description = "Create unique recipes with detailed instructions",
            route = "recipe",
            icon = Icons.Outlined.Restaurant
        ),
        Tool(
            name = "Language Learning",
            description = "AI-powered language learning assistant",
            route = "language",
            icon = Icons.Outlined.Language
        ),
        Tool(
            name = "AI Chatbot",
            description = "Have a conversation with AI assistant",
            route = "chat",
            icon = Icons.Outlined.Chat
        ),
        Tool(
            name = "Presentation Generator",
            description = "Create professional presentation outlines",
            route = "presentation",
            icon = Icons.Outlined.Slideshow
        )
    )

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("AI Tools")
                        Text(
                            "Powered by Google Gemini",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        if (apiKey.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Please set up your API key first",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(onClick = { navController.navigate("onboarding") }) {
                        Text("Go to Setup")
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = padding.calculateTopPadding(),
                    bottom = 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tools) { tool ->
                    ToolCard(
                        tool = tool,
                        onClick = { 
                            if (tool.route == "developer") {
                                navController.navigate(tool.route)
                            } else {
                                navController.navigate("${tool.route}?apiKey=$apiKey")
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolCard(
    tool: Tool,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = tool.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = tool.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = tool.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class Tool(
    val name: String,
    val description: String,
    val route: String,
    val icon: ImageVector
) 