package com.example.studytracker.presentation.components


import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(
    state: DatePickerState,
    isOpen: Boolean,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
    onDismissRequest: () -> Unit,
    onConfirmButtonClicked: () -> Unit
) {
    //need to comeback and customise the date picker further after learning new api
    if(isOpen){
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onConfirmButtonClicked) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = dismissButtonText)
                }
            },
            content = {
                DatePicker(
                    state = state
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(apiLevel = 33)
@Composable
fun DatePreview() {
    TaskDatePicker(
        state = rememberDatePickerState(
            initialSelectedDateMillis = Instant.now().toEpochMilli()
        ),
        isOpen = true,
        onDismissRequest = { },
        onConfirmButtonClicked = {}
    )
}