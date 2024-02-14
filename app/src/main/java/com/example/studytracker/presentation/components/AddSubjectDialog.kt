package com.example.studytracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studytracker.R
import com.example.studytracker.domain.model.Subject

@Composable
fun AddSubjectDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    title: String = "Add/Update Subject",
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    if(isOpen){
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = title)
            },
            text = {
                   Column{
                       Row(
                           modifier = modifier
                               .fillMaxWidth()
                               .padding(bottom = 16.dp),
                           horizontalArrangement = Arrangement.SpaceAround
                       ){
                            Subject.subjectCardColors.forEach { colors ->
                                Box(
                                    modifier = modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(brush = Brush.verticalGradient(colors))
                                )
                            }
                       }
                   }
            },
            confirmButton = {
                TextButton(onClick = onConfirmButtonClick) {
                    Text(
                        text = stringResource(R.string.save)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun AlertPreview() {
    AddSubjectDialog(isOpen = true, onDismissRequest = { /*TODO*/ }) {
        
    }
}