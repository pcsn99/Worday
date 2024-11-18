package com.example.android.worday

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.android.worday.ui.theme.WorDayTheme
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Composable
fun GameScreen(
    context: Context,
    navController: NavController, // Added NavController
    onGameComplete: (Int, Int) -> Unit,
    margin: Dp = 16.dp
) {
    val sharedPreferences = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
    val lastPlayedTime = sharedPreferences.getLong("lastPlayedTime", 0)
    val currentTime = System.currentTimeMillis()
    var canPlay by remember { mutableStateOf(currentTime - lastPlayedTime > TimeUnit.HOURS.toMillis(24)) }

    var totalScore by remember { mutableStateOf(0) }
    var currentScore by remember { mutableStateOf(100) }
    var hintIndex by remember { mutableStateOf(0) }
    var word by remember { mutableStateOf("") }
    var userGuessList by remember { mutableStateOf(emptyList<String>()) }
    var hints by remember { mutableStateOf(listOf("", "", "")) }
    var round by remember { mutableStateOf(1) }
    var mistakes by remember { mutableStateOf(0) }
    var roundsCorrect by remember { mutableStateOf(0) }
    val maxRounds = 3
    val maxMistakes = 3
    val coroutineScope = rememberCoroutineScope()

    fun saveLastPlayedTime() {
        sharedPreferences.edit().putLong("lastPlayedTime", System.currentTimeMillis()).apply()
    }

    fun resetPlayStatus() {
        sharedPreferences.edit().remove("lastPlayedTime").apply()
        canPlay = true
        navController.navigate("mainMenu") // Navigate back to the main menu
    }

    // Fetches the word and hints
    fun fetchWordAndHints(round: Int) {
        coroutineScope.launch {
            try {
                val apiKey = "AIzaSyDjrfWFgP7WayEt-JB94m5ZYQCcVE8Tjrg"
                val spreadsheetId = "1ml66TRlL-WDLkNk8QjYB2SrmKLVn4lPoutXLdASyDRQ"
                val row = round + 2
                val range = "Sheet1!A${row}:D${row}"

                val response = RetrofitClient.instance.getSheetData(spreadsheetId, range, apiKey)
                if (response.values.isNotEmpty()) {
                    word = response.values[0][0]
                    hints = response.values[0].subList(1, 4)
                } else {
                    word = "EXAMPLE"
                    hints = listOf("Hint 1", "Hint 2", "Hint 3")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                word = "EXAMPLE"
                hints = listOf("Hint 1", "Hint 2", "Hint 3")
            } finally {
                userGuessList = List(word.length) { "" }
                currentScore = 100
                hintIndex = 0
                mistakes = 0
            }
        }
    }

    LaunchedEffect(Unit) {
        if (canPlay) {
            fetchWordAndHints(round)
        }
    }

    if (canPlay) {
        if (word.isNotEmpty() && userGuessList.size == word.length) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(margin),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(margin),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Total Score: $totalScore", fontSize = 18.sp)
                    Text("Round: $round / $maxRounds", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Hint: ${hints.getOrNull(hintIndex) ?: "No hint available"}", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    AutomatedNavigationRow(
                        word = word,
                        userGuessList = userGuessList,
                        onGuessChanged = { updatedList -> userGuessList = updatedList }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        val userGuess = userGuessList.joinToString("")
                        if (userGuess.equals(word, ignoreCase = true)) {
                            totalScore += currentScore
                            roundsCorrect += 1
                            if (round < maxRounds) {
                                round += 1
                                fetchWordAndHints(round)
                            } else {
                                saveLastPlayedTime()
                                onGameComplete(totalScore, roundsCorrect)
                            }
                        } else {
                            mistakes += 1
                            currentScore -= 10
                            hintIndex = (hintIndex + 1).coerceAtMost(hints.size - 1)

                            if (mistakes >= maxMistakes) {
                                if (round < maxRounds) {
                                    round += 1
                                    fetchWordAndHints(round)
                                } else {
                                    saveLastPlayedTime()
                                    onGameComplete(totalScore, roundsCorrect)
                                }
                            }
                        }
                    }) {
                        Text("Submit Guess")
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "You have already played today! Please come back tomorrow.",
                    fontSize = 16.sp
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { resetPlayStatus() }) {
                    Text("Reset (Go to Main Menu)")
                }
            }
        }
    }
}

@Composable
fun AutomatedNavigationRow(
    word: String,
    userGuessList: List<String>,
    onGuessChanged: (List<String>) -> Unit
) {
    val focusRequesters = remember { List(word.length) { FocusRequester() } }

    require(word.length == userGuessList.size) {
        "Word length (${word.length}) and userGuessList size (${userGuessList.size}) must match"
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        word.forEachIndexed { index, _ ->
            OutlinedTextField(
                value = userGuessList[index],
                onValueChange = { input ->
                    if (input.length <= 1) { // Allow only one letter
                        val updatedList = userGuessList.toMutableList()
                        updatedList[index] = input.uppercase()

                        onGuessChanged(updatedList)

                        // Focus management
                        if (input.isNotEmpty() && index < focusRequesters.size - 1) {
                            // Move to the next box if within range
                            focusRequesters[index + 1].requestFocus()
                        } else if (input.isEmpty() && index > 0) {
                            // Move back to the previous box if within range
                            focusRequesters[index - 1].requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .height(60.dp)
                    .width(51.dp)
                    .padding(2.dp)
                    .focusRequester(focusRequesters[index]),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )
        }
    }
}

@Composable
fun SummaryScreen(totalScore: Int, roundsCorrect: Int, onRestartGame: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game Complete!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Total Score: $totalScore", fontSize = 20.sp)
        Text("Rounds Correct: $roundsCorrect / 3", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRestartGame) {
            Text("Restart Game")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    WorDayTheme {
        // Create a mock NavController for preview purposes
        val mockNavController = rememberNavController()

        GameScreen(
            context = LocalContext.current, // Provide the current context
            navController = mockNavController, // Pass the mock NavController
            onGameComplete = { _, _ -> } // Provide a no-op implementation for onGameComplete
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryScreenPreview() {
    WorDayTheme {
        SummaryScreen(totalScore = 250, roundsCorrect = 2, onRestartGame = {})
    }
}
