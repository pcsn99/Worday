package com.example.android.worday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android.worday.ui.theme.WorDayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorDayTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "MainMenu"
    ) {
        // Main Menu Screen
        composable("MainMenu") {
            MainMenuScreen(
                onHowToPlayClick = { navController.navigate("tutorial") },
                onStartGameClick = { navController.navigate("game") }
            )
        }

        // Tutorial Screen
        composable("tutorial") {
            TutorialScreen(onBack = { navController.popBackStack() })
        }

        // Game Screen
        composable("game") {
            GameScreen(
                context = LocalContext.current,
                navController = navController, // Ensure this matches the name of your NavController
                onGameComplete = { totalScore, roundsCorrect ->
                    navController.navigate("summary/$totalScore/$roundsCorrect")
                },
                margin = 16.dp
            )
        }


        // Summary Screen
        composable(
            route = "summary/{totalScore}/{roundsCorrect}",
            arguments = listOf(
                navArgument("totalScore") { type = NavType.IntType },
                navArgument("roundsCorrect") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // Extract arguments from the back stack
            val totalScore = backStackEntry.arguments?.getInt("totalScore") ?: 0
            val roundsCorrect = backStackEntry.arguments?.getInt("roundsCorrect") ?: 0
            SummaryScreen(
                totalScore = totalScore,
                roundsCorrect = roundsCorrect,
                onRestartGame = {
                    // Navigate back to the main menu to restart the game
                    navController.navigate("MainMenu") {
                        popUpTo("MainMenu") { inclusive = true }
                    }
                }
            )
        }
    }
}
