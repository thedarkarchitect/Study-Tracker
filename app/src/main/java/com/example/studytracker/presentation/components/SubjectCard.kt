package com.example.studytracker.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studytracker.R
import com.example.studytracker.ui.theme.gradient1

@Composable
fun SubjectCard(
    modifier: Modifier = Modifier,
    subjectName: String,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(150.dp)
            .clickable { onClick() }
            .background(
                brush = Brush.verticalGradient(gradientColors),
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.books),
                contentDescription = subjectName,
                modifier = modifier.size(80.dp)
            )
            Text(
                text = subjectName,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun CardPreview() {
    SubjectCard(
        subjectName = "English" ,
        gradientColors = gradient1
    ) {

    }
}