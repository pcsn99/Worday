import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.worday.R
import com.example.android.worday.ui.theme.WorDayTheme
import kotlinx.coroutines.delay

@Composable
fun MainMenuScreen(
    onHowToPlayClick: () -> Unit,
    onStartGameClick: () -> Unit
) {
    val showContent = remember { mutableStateOf(false) }

    // Trigger animations after a delay
    LaunchedEffect(Unit) {
        delay(500) // Delay before animations start
        showContent.value = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.background_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Foreground Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo Animation: Slides from the top
                    AnimatedVisibility(
                        visible = showContent.value,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                            initialOffsetY = { -200 } // Slides in from the top
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .padding(top = 130.dp) // Reduced bottom padding
                                .fillMaxWidth() // Expand horizontally
                                .height(200.dp) // Set specific height
                        )
                    }

                    // Tagline as Image
                    AnimatedVisibility(
                        visible = showContent.value,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                            initialOffsetY = { 200 } // Slides in from the bottom
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tagline),
                            contentDescription = "Tagline",
                            modifier = Modifier
                                .fillMaxWidth() // Expand horizontally
                                .height(110.dp) // Set specific height
                                .padding(bottom = 16.dp) // Reduced padding below tagline
                        )
                    }

                    // Start Game Button as Image
                    AnimatedVisibility(
                        visible = showContent.value,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                            initialOffsetY = { 200 } // Slides in from the bottom
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.playbutton),
                            contentDescription = "Start Game",
                            modifier = Modifier
                                .padding(start = 30.dp)
                                .size(190.dp) // Standardized size
                                .clickable { onStartGameClick() }
                                .padding(bottom = 90.dp) // Space between buttons
                        )
                    }

                    // Row to Align "How to Play" Button to the Right
                    AnimatedVisibility(
                        visible = showContent.value,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                            initialOffsetY = { 200 } // Slides in from the bottom
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp), // Add margin from the right
                            horizontalArrangement = Arrangement.End // Align to the right
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.how),
                                contentDescription = "How to Play",
                                modifier = Modifier
                                    .size(80.dp) // Standardized size
                                    .clickable { onHowToPlayClick() }
                            )
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


