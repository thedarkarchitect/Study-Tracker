package com.example.studytracker.presentation.task

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studytracker.R
import com.example.studytracker.presentation.components.DeleteDialog
import com.example.studytracker.presentation.components.SubjectListBottomSheet
import com.example.studytracker.presentation.components.TaskCheckBox
import com.example.studytracker.presentation.components.TaskDatePicker
import com.example.studytracker.subjects
import com.example.studytracker.ui.theme.OrangeError
import com.example.studytracker.ui.theme.StudyTrackerTheme
import com.example.studytracker.util.Priority
import com.example.studytracker.util.changeMillisToDateString
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.time.Instant

data class TaskScreenNavArgs(
    val taskId: Int?,
    val subjectId: Int?
)

@Destination(navArgsDelegate = TaskScreenNavArgs::class)
@Composable
fun TaskScreenRoute(
    navigator: DestinationsNavigator
) {
    TaskScreen(
        onBackButtonClick = { navigator.navigateUp() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    onBackButtonClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var isDeleteDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    var isDatePickerDialogOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var taskTitleError by rememberSaveable { mutableStateOf<String?>(null) }
    taskTitleError = when {
        title.isBlank() -> "Please enter task title."
        title.length < 4 -> "Task title is too short."
        title.length > 30 -> "Task title is too long."
        else -> null
    }

    DeleteDialog(
        isOpen = isDeleteDialogOpen,
        title = "Delete Task?",
        bodyText = "Are you sure, you want to delete this task? This action can not be undone.",
        onDismissRequest = { isDeleteDialogOpen = false },
        onConfirmButtonClick = { isDeleteDialogOpen = false }
    )

    TaskDatePicker(
        state = datePickerState,
        isOpen = isDatePickerDialogOpen,
        onDismissRequest = { isDatePickerDialogOpen = false },
        onConfirmButtonClicked = { isDatePickerDialogOpen = false }
    )

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = subjects,
        onSubjectClicked = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if(!sheetState.isVisible) isBottomSheetOpen = false
            }
        },
        onDismissRequest = { isBottomSheetOpen = false }
    )
    Scaffold(
        topBar = {
            TaskScreenTopBar(
                doesTaskExist = true,
                isComplete = false,
                checkBoxBorderColor = OrangeError,
                onBackButtonClick = onBackButtonClick,
                onDeleteButtonClick = { isDeleteDialogOpen = true },
                onCheckBoxClick = { }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .verticalScroll(state = rememberScrollState())
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ){
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text(text = stringResource(R.string.title)) },
                singleLine = true,
                isError = taskTitleError != null && title.isNotBlank(),
                supportingText = { Text(text = taskTitleError.orEmpty()) }
            )
            Spacer(modifier = modifier.height(10.dp))
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = { Text(text = stringResource(R.string.description)) },
                singleLine = true
            )
            Spacer(modifier = modifier.height(20.dp))
            Text(
                text = "Due Date",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = datePickerState.selectedDateMillis.changeMillisToDateString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { isDatePickerDialogOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.select_due_date)
                    )
                }
            }
            Spacer(modifier = modifier.height(10.dp))
            Text(
                text = "Priority",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = modifier.height(10.dp))
            Row( modifier = modifier.fillMaxWidth()) {
                Priority.entries.forEach { priority ->
                    PriorityButton(
                        modifier = modifier.weight(1f),
                        label = priority.title,
                        backgroundColor = priority.color,
                        borderColor = if (priority == Priority.MEDIUM) Color.White  else Color.Transparent,
                        labelColor = if(priority == Priority.MEDIUM) Color.White else Color.White.copy(alpha = 0.7f),
                        onClick = {  }
                    )
                }
            }
            Spacer(modifier = modifier.height(30.dp))
            Text(
                text = "Related to subject",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "English",
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { isBottomSheetOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Subject"
                    )
                }
            }
            Button(
                enabled = taskTitleError == null,
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary // Look for this in case color doesn't change
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(text = "Save")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreenTopBar(
    doesTaskExist: Boolean,
    isComplete: Boolean,
    checkBoxBorderColor: Color,
    onBackButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onCheckBoxClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigation_back)
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.task),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            if(doesTaskExist){
                TaskCheckBox(
                    isComplete = isComplete, 
                    borderColor = checkBoxBorderColor,
                    onCheckBoxClick = onCheckBoxClick
                )
                IconButton(onClick = onDeleteButtonClick) {
                    Icon(
                        imageVector = Icons.Default.Delete, 
                        contentDescription = stringResource(id = R.string.delete)
                    )
                }
            }
        }
    )
}

@Composable
fun PriorityButton(
    modifier: Modifier = Modifier,
    label: String,
    backgroundColor: Color,
    borderColor: Color,
    labelColor: Color,
    onClick: () -> Unit
) {
   Box(
       modifier = modifier
           .background(backgroundColor)
           .clickable { onClick() }
           .padding(5.dp)
           .border(1.dp, borderColor, RoundedCornerShape(5.dp))
           .padding(5.dp),
       contentAlignment = Alignment.Center
   ){
        Text(
            text = label,
            color = labelColor
        )
   }
}

@Preview(apiLevel = 33)
@Preview(apiLevel = 33, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TaskPreview() {
    StudyTrackerTheme {
        TaskScreen(
            onBackButtonClick = {}
        )
    }
}