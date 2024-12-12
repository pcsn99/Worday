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


    LaunchedEffect(Unit) {
        delay(500)
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

                Image(
                    painter = painterResource(id = R.drawable.background_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AnimatedVisibility(
                        visible = showContent.value,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                            initialOffsetY = { -200 }
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .padding(top = 130.dp)
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }


                    AnimatedVisibility(
                        visible = showContent.value,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                            initialOffsetY = { 200 }
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tagline),
                            contentDescription = "Tagline",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .padding(bottom = 16.dp)
                        )
                    }


                    AnimatedVisibility(
                        visible = showContent.value,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                            initialOffsetY = { 200 }
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.playbutton),
                            contentDescription = "Start Game",
                            modifier = Modifier
                                .padding(start = 30.dp)
                                .size(190.dp)
                                .clickable { onStartGameClick() }
                                .padding(bottom = 90.dp)
                        )
                    }


                    AnimatedVisibility(
                        visible = showContent.value,
                        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                            initialOffsetY = { 200 }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.how),
                                contentDescription = "How to Play",
                                modifier = Modifier
                                    .size(80.dp)
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


