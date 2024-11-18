package com.example.android.worday

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.worday.ui.theme.WorDayTheme

@Composable
fun TutorialScreen(onBack: () -> Unit){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ){
        Text("How to Play", style = MaterialTheme.typography.headlineMedium)
        Text("1. Guess the word based on the hint.\n2. Each incorrect guess unlocks a new hint.\n3. Fewer attempts mean a higher score!")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
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