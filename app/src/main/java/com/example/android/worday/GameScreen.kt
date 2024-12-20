package com.example.android.worday

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
    navController: NavController,
    onGameComplete: (Int, Int) -> Unit,
    margin: Dp = 16.dp
) {
    val sharedPreferences = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
    val lastPlayedTime = sharedPreferences.getLong("lastPlayedTime", 0)
    val currentTime = System.currentTimeMillis()
    var canPlay by remember {
        mutableStateOf(
            currentTime - lastPlayedTime > TimeUnit.HOURS.toMillis(
                24
            )
        )
    }

    var totalScore by remember { mutableStateOf(0) }
    var currentScore by remember { mutableStateOf(100) }
    var hintIndex by remember { mutableStateOf(0) }
    var word by remember { mutableStateOf("") }
    var userGuessList by remember { mutableStateOf(emptyList<String>()) }
    var hints by remember { mutableStateOf(listOf("", "", "")) }
    var round by remember { mutableStateOf(1) }
    var mistakes by remember { mutableStateOf(0) }
    var roundsCorrect by remember { mutableStateOf(0) }
    val maxRounds = 20
    val maxMistakes = 3
    val coroutineScope = rememberCoroutineScope()

    fun saveLastPlayedTime() {
        sharedPreferences.edit().putLong("lastPlayedTime", System.currentTimeMillis()).apply()
    }

    fun resetPlayStatus() {
        sharedPreferences.edit().remove("lastPlayedTime").apply()
        canPlay = true
        navController.navigate("mainMenu")
    }

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
            ) {

                Image(
                    painter = painterResource(id = R.drawable.background_image),
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(margin),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top bar with Total Score
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Round: $round / $maxRounds", fontSize = 16.sp)
                        Text("Score: $totalScore", fontSize = 16.sp)
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp)
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(maxMistakes) { index ->
                            if (index < mistakes) {
                                Text(
                                    text = "X",
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(
                                            MaterialTheme.colorScheme.secondary,
                                            CircleShape
                                        )
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }

                    // Word Guess Grid
                    AutomatedNavigationRow(
                        word = word,
                        userGuessList = userGuessList,
                        onGuessChanged = { updatedList -> userGuessList = updatedList }
                    )

                    // Hint Text (if available)
                    Text(
                        text = "Hint: ${hints.getOrNull(hintIndex) ?: "No hint available"}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    // Submit Button
                    Button(
                        onClick = {
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
                        }
                    ) {
                        Text("Submit")
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
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_image),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "You have already played today!",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF001F54)
                )

                Text(
                    "Please come back tomorrow.",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color(0xFF001F54)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { resetPlayStatus() },
                    colors = ButtonDefaults.buttonColors(Color(0xFF001F54))
                ) {
                    Text("Reset (Go to Main Menu)", color = Color.White)
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

    val focusRequesters = remember(word) { List(word.length) { FocusRequester() } }

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

    // Automatically request focus for the first box when the row is displayed
    LaunchedEffect(focusRequesters) {
        focusRequesters.firstOrNull()?.requestFocus()
    }
}

@Composable
fun SummaryScreen(
    totalScore: Int,
    roundsCorrect: Int,
    onRestartGame: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.result_bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp).padding(top = 80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Game Complete!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Cursive,
                    color = Color(0xFF001F54)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Total Score: $totalScore",
                fontSize = 20.sp,
                color = Color(0xFF001F54),
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Rounds Correct: $roundsCorrect / 20",
                fontSize = 18.sp,
                color = Color(0xFF001F54),
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onRestartGame) {
                Text("Main Menu")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    WorDayTheme {

        val mockNavController = rememberNavController()

        GameScreen(
            context = LocalContext.current,
            navController = mockNavController,
            onGameComplete = { _, _ -> }
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
