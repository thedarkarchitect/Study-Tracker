package com.example.studytracker.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TaskCheckBox(
    modifier: Modifier = Modifier,
    isComplete: Boolean,
    borderColor: Color,
    onCheckBoxClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(25.dp)
            .clip(CircleShape)
            .border(2.dp, borderColor, CircleShape)
            .clickable { onCheckBoxClick() },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = isComplete) {
            Icon(
                modifier = modifier.size(18.dp),
                imageVector = Icons.Rounded.CheckCircle,
                tint = borderColor,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun taskCheckPreview() {
    TaskCheckBox(isComplete = true, borderColor = MaterialTheme.colorScheme.primary) {}
}