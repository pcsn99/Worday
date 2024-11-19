import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.worday.ui.theme.WorDayTheme
import kotlinx.coroutines.delay

@Composable
fun MainMenuScreen(
    onHowToPlayClick: () -> Unit,
    onStartGameClick: () -> Unit
) {
    // State variables to control the visibility of animations
    val showTitle = remember { mutableStateOf(false) }
    val showTagline = remember { mutableStateOf(false) }
    val showButtons = remember { mutableStateOf(false) }

    // Animations: Sequentially fade in elements with slide-in effects
    LaunchedEffect(Unit) {
        delay(500) // Delay before title appears
        showTitle.value = true
        delay(500) // Delay before tagline appears
        showTagline.value = true
        delay(500) // Delay before buttons appear
        showButtons.value = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title Animation
                    AnimatedVisibility(
                        visible = showTitle.value,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                            initialOffsetY = { -50 },
                            animationSpec = tween(500)
                        )
                    ) {
                        Text(
                            "WorDay",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Tagline Animation
                    AnimatedVisibility(
                        visible = showTagline.value,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                            initialOffsetY = { 50 },
                            animationSpec = tween(500)
                        )
                    ) {
                        Text(
                            "How many points can you get by guessing the words?",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )
                    }

                    // Buttons Animation
                    AnimatedVisibility(
                        visible = showButtons.value,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                            initialOffsetY = { 100 },
                            animationSpec = tween(500)
                        )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(
                                onClick = onHowToPlayClick,
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .padding(vertical = 8.dp)
                            ) {
                                Text("How to Play")
                            }

                            Button(
                                onClick = onStartGameClick,
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .padding(vertical = 8.dp)
                            ) {
                                Text("Start Game")
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    WorDayTheme {
        MainMenuScreen(
            onHowToPlayClick = {}, onStartGameClick = {}
        )
    }
}
