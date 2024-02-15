package com.example.studytracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studytracker.R
import com.example.studytracker.domain.model.Subject

@Composable
fun AddSubjectDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    title: String = "Add/Update Subject",
    selectedColors: List<Color>,
    subjectName: String,
    goalHours: String,
    onColorChange: (List<Color>) -> Unit,
    onSubjectNameChange: (String) -> Unit,
    onGoalHoursChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {

    var subjectNameError by rememberSaveable { mutableStateOf<String?>(null) }
    var goalNameError by rememberSaveable { mutableStateOf<String?>(null) }

    subjectNameError = when {
        subjectName.isBlank() -> "Please enter subject name."
        subjectName.length < 2 -> "Subject name is too short."
        subjectName.length > 20 -> "Subject name is too long."
        else -> null
    }

    goalNameError = when {
        goalHours.isBlank() -> "Please enter goal study hours."
        goalHours.toFloatOrNull() == null -> "Invalid number."
        goalHours.toFloat() < 1f -> "Please set at least 1 hour."
        goalHours.toFloat() > 1000f -> "Please set a maximum of 1000 hours."
        else -> null
    }


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
                                        .border(
                                            width = 4.dp,
                                            color = if (colors == selectedColors) Color.Black else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .background(brush = Brush.verticalGradient(colors))
                                        .clickable { onColorChange(colors) }
                                )
                            }
                       }
                       OutlinedTextField(
                           value = subjectName,
                           onValueChange = onSubjectNameChange,
                           label =  {
                               Text(text = stringResource(R.string.subject_name))
                           },
                           singleLine = true,
                           isError = subjectNameError != null && subjectName.isNotBlank(),//testing cause is of a boolean type 
                           supportingText = { //text to be shown below the text field
                               Text(text = subjectNameError.orEmpty())//this return the text if the error is not empty or null 
                           }
                       )
                       Spacer(modifier = modifier.height(10.dp))
                       OutlinedTextField(
                           value = goalHours,
                           onValueChange = onGoalHoursChange,
                           label =  {
                               Text(text = stringResource(R.string.goal_study_hours))
                           },
                           singleLine = true,
                           isError = goalNameError != null && goalHours.isNotBlank(),
                           supportingText = { Text(text = goalNameError.orEmpty()) },
                           keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                       )
                   }
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmButtonClick,
                    enabled = subjectNameError == null && goalNameError == null
                ) {
                    Text(
                        text = stringResource(R.string.save)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest ) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            }
        )
    }
}

//@Preview
//@Composable
//fun AlertPreview() {
//    AddSubjectDialog(
//        isOpen = true,
//        onDismissRequest = { /*TODO*/ },
//        onColorChange = {},
//        selectedColors = emptyList()
//    ) {
//
//    }
//}