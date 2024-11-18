package com.example.aitools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.aitools.ui.screens.BlogGeneratorScreen
import com.example.aitools.ui.screens.ChatbotScreen
import com.example.aitools.ui.screens.CodeAssistantScreen
import com.example.aitools.ui.screens.EmailComposerScreen
import com.example.aitools.ui.screens.GitHubAnalyticsScreen
import com.example.aitools.ui.screens.HashtagGeneratorScreen
import com.example.aitools.ui.theme.AiToolsTheme
import com.example.aitools.ui.screens.HomeScreen
import com.example.aitools.ui.screens.ImageAnalyzerScreen
import com.example.aitools.ui.screens.LanguageLearningScreen
import com.example.aitools.ui.screens.OnboardingScreen
import com.example.aitools.ui.screens.PromptEnhancerScreen
import com.example.aitools.ui.screens.RecipeGeneratorScreen
import com.example.aitools.ui.screens.StoryPlotGeneratorScreen
import com.example.aitools.ui.screens.TextSummarizerScreen
import com.example.aitools.viewmodel.AuthViewModel
import com.example.aitools.ui.screens.DeveloperProfileScreen
import com.example.aitools.ui.screens.PresentationGeneratorScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiToolsTheme {
                AiToolsApp()
            }
        }
    }
}

@Composable
fun AiToolsApp() {
    val navController = rememberNavController()
    val viewModel: AuthViewModel = viewModel()
    val apiKey by viewModel.apiKeyState.collectAsState()
    
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "onboarding",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("onboarding") {
                OnboardingScreen(navController)
            }
            composable("home") {
                HomeScreen(navController, apiKey)
            }
            composable(
                route = "email?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    EmailComposerScreen(navController, key)
                }
            }
            composable(
                route = "summarize?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    TextSummarizerScreen(navController, key)
                }
            }
            composable(
                route = "hashtags?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    HashtagGeneratorScreen(navController, key)
                }
            }
            composable(
                route = "code?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    CodeAssistantScreen(navController, key)
                }
            }
            composable(
                route = "image?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    ImageAnalyzerScreen(navController, key)
                }
            }
            composable(
                route = "github?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    GitHubAnalyticsScreen(navController, key)
                }
            }
            composable(
                route = "prompt?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    PromptEnhancerScreen(navController, key)
                }
            }
            composable(
                route = "blog?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    BlogGeneratorScreen(navController, key)
                }
            }
            composable(
                route = "story?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    StoryPlotGeneratorScreen(navController, key)
                }
            }
            composable(
                route = "recipe?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    RecipeGeneratorScreen(navController, key)
                }
            }
            composable(
                route = "language?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    LanguageLearningScreen(navController, key)
                }
            }
            composable(
                route = "chat?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    ChatbotScreen(navController, key)
                }
            }
            composable("developer") {
                DeveloperProfileScreen(navController)
            }
            composable(
                route = "presentation?apiKey={apiKey}",
                arguments = listOf(navArgument("apiKey") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("apiKey")?.let { key ->
                    PresentationGeneratorScreen(navController, key)
                }
            }
        }
    }
}