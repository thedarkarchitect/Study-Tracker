package com.example.studytracker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CountCard(
    modifier: Modifier = Modifier,
    headingText: String,
    count: String
) {
    ElevatedCard {
        Column(
            modifier = modifier
                .padding(horizontal = 4.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = headingText,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = modifier.height(5.dp))
            Text(
                text = count,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 30.sp)
            )
        }
    }
}


@Preview(apiLevel = 33, showBackground = true)
@Composable
fun Preview1(){
    Row{
        CountCard(headingText = "english", count = "25")
        CountCard(headingText = "english", count = "25")
        CountCard(headingText = "english", count = "25")
    }
}