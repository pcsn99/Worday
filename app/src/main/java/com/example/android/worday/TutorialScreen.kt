package com.example.android.worday

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.worday.ui.theme.WorDayTheme

@Composable
fun TutorialScreen(onBack: () -> Unit) {
    var showContent by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(targetValue = if (showContent) 1f else 0f)

    LaunchedEffect(Unit) {
        // Triggers the animation after the screen is loaded
        showContent = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.background_image), // Replace with your image resource
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Foreground content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Image above the instructions
            Image(
                painter = painterResource(id = R.drawable.title), // Replace with your image resource
                contentDescription = "Instructions Image",
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxWidth()
                    .size(200.dp)
                    .alpha(alpha)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions with bullet points
            Column(modifier = Modifier.alpha(alpha)) {
                Text(
                    "Daily Word Challenge:",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black, fontSize = 24.sp) // Larger font size
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "- Each day, three new words appear.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 18.sp) // Larger font size
                )
                Text(
                    "- Each word has a different difficulty level:",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 18.sp)
                )
                Text("  • Easy", style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 18.sp))
                Text("  • Medium", style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 18.sp))
                Text("  • Hard", style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 18.sp))

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Difficulty Levels & Scoring:",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black, fontSize = 24.sp) // Larger font size
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "- Easy Word: Common word, worth fewer points.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 18.sp)
                )
                Text(
                    "- Medium Word: Slightly harder word, worth moderate points.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 18.sp)
                )
                Text(
                    "- Hard Word: A challenging word, worth the most points.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 18.sp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "- For each word, you have three tries to guess it.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 18.sp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Each word's difficulty level determines its point value. Aim to guess harder words for a higher score!",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 18.sp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Button with rounded corners and animation
            Button(
                onClick = onBack,
                modifier = Modifier
                    .alpha(alpha)
                    .padding(horizontal = 32.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TutorialScreenPreview() {
    WorDayTheme {
        TutorialScreen(onBack = {})
    }
}
