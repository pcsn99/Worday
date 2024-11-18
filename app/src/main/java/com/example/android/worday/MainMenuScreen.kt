package com.example.android.worday

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.worday.ui.theme.WorDayTheme

@Composable
fun MainMenuScreen(
    onHowToPlayClick: ()-> Unit,
    onStartGameClick: ()-> Unit
) {



    Scaffold(
        modifier = Modifier.wrapContentSize(),
        content = { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Background Image
            //    Image(
            //        painter = painterResource(id = R.drawable.background_image),
            //        contentDescription = null,
            //        modifier = Modifier.fillMaxSize(),
            //        contentScale = ContentScale.Crop
            //    )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "WorDay",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )


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