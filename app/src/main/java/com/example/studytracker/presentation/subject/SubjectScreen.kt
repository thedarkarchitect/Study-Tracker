package com.example.studytracker.presentation.subject

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studytracker.R
import com.example.studytracker.domain.model.Subject
import com.example.studytracker.presentation.components.AddSubjectDialog
import com.example.studytracker.presentation.components.CountCard
import com.example.studytracker.presentation.components.DeleteDialog
import com.example.studytracker.presentation.components.studySessionsList
import com.example.studytracker.presentation.components.tasksList
import com.example.studytracker.presentation.destinations.TaskScreenRouteDestination
import com.example.studytracker.presentation.task.TaskScreenNavArgs
import com.example.studytracker.sessions
import com.example.studytracker.tasks
import com.example.studytracker.ui.theme.OrangeError
import com.example.studytracker.ui.theme.StudyTrackerTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data class SubjectScreenNavArgs(
    val subjectId: Int
)

@Destination(navArgsDelegate = SubjectScreenNavArgs::class)
@Composable
fun SubjectScreenRoute(
    navigator: DestinationsNavigator
) {
    SubjectScreen(
        onBackButtonClick = { navigator.navigateUp() },
        onAddTaskButtonClick = {
            val navArg = TaskScreenNavArgs( taskId = null, subjectId = -1 )
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))
        },
        onTaskCardClick = { taskId ->
            val navArg = TaskScreenNavArgs(taskId = taskId, subjectId = null)
            navigator.navigate( TaskScreenRouteDestination(navArgs = navArg) )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(
    modifier : Modifier = Modifier,
    onBackButtonClick: () -> Unit,
    onAddTaskButtonClick: () -> Unit,
    onTaskCardClick: (Int?) -> Unit
) {

    val viewModel: SubjectViewModel = hiltViewModel()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState() // remember scroll state of items of the lazy column
    val isFABExpanded by remember {
        derivedStateOf { // this used to affect state of a selected item index in the lazy column
            listState.firstVisibleItemIndex == 0
        }
    }

    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }

    var subjectName by remember { mutableStateOf("") }
    var goalHours by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Subject.subjectCardColors.random()) }


    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        subjectName = subjectName,
        goalHours = goalHours,
        onSubjectNameChange = { subjectName = it },
        onGoalHoursChange = { goalHours = it },
        onDismissRequest = { isAddSubjectDialogOpen = false },
        selectedColors = emptyList(),
        onColorChange = { selectedColor = it },
        onConfirmButtonClick = { isAddSubjectDialogOpen = false }
    )

    DeleteDialog(
        isOpen = isDeleteSubjectDialogOpen,
        title = "Delete Subject",
        bodyText = "Are you sure, you want to delete this subject? All related tasks and study sessions will be permanently removed. this action can not be undone.",
        onDismissRequest = { isDeleteSessionDialogOpen = false },
        onConfirmButtonClick = { isDeleteSessionDialogOpen = false }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session",
        bodyText = "Are you sure, you want to delete this session? Your studied hours will be reduced by this session time. This action can not be undone.",
        onDismissRequest = { isDeleteSessionDialogOpen = false },
        onConfirmButtonClick = { isDeleteSessionDialogOpen = false }
    )

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SubjectScreenTopBar(
                title = "English",
                onBackButtonClick = onBackButtonClick,
                onDeleteButtonClick = { isDeleteSubjectDialogOpen = true },
                onEditButtonClick = { isAddSubjectDialogOpen = true },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskButtonClick,
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add)) },
                text = { Text(text = stringResource(R.string.add_task)) },
                containerColor = MaterialTheme.colorScheme.inversePrimary,
                expanded = isFABExpanded // this will expand the floating action button when index 0 is shown and hide it when index 0 is not showing
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = listState
        ) {
            item {
                SubjectOverviewSection(
                    studiedHours = "10",
                    goalHours = "15",
                    progress = 0.09f
                )
            }
            tasksList(
                sectionTitle = "UPCOMING TASKS",
                emptyTaskText = "You don't have any upcoming tasks.\n Click the + button to add new task.",
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClick = onTaskCardClick
            )

            item {
                Spacer(modifier = modifier.height(20.dp))
            }

            tasksList(
                sectionTitle = "COMPLETED TASKS",
                emptyTaskText = "You don't have any completed tasks.\n Click the check box on completion of task.",
//                tasks = emptyList()
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClick = onTaskCardClick
            )

            studySessionsList(
                sectionTitle = "RECENT STUDY SESSIONS",
                emptyTaskText = "You don't have any recent study sessions.\n Start a study session to begin recording your progress.",
                sessions = sessions,
                onDeleteIconClick = { isDeleteSessionDialogOpen = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreenTopBar(
    title: String,
    onBackButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick =  onBackButtonClick ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.navigation_back)
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            IconButton(onClick =  onDeleteButtonClick ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_subject)
                )
            }
            IconButton(onClick =  onEditButtonClick ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_subject)
                )
            }
        }
    )
}

@Composable
fun SubjectOverviewSection(
    modifier: Modifier = Modifier,
    studiedHours: String,
    goalHours: String,
    progress: Float
) {

    val percentageProgress = remember(progress){
        (progress * 100).toInt().coerceIn(0, 100)//turns float into int percentage
    }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        CountCard(
            modifier = modifier.padding(horizontal = 12.dp),
            headingText ="Goal Study Hours" ,
            count = goalHours
        )

        CountCard(
            modifier = modifier.padding(horizontal = 32.dp),
            headingText ="Study Hours" ,
            count = studiedHours
        )

        Box(
            modifier = modifier.size(75.dp),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                progress = { progress },
                modifier = modifier.fillMaxSize(),
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant //might change
            )
            CircularProgressIndicator(
                progress = { progress },
                modifier = modifier.fillMaxSize(),
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = if (progress < 0.10f ) {
                    OrangeError
                }else if (progress < 0.50f){
                    MaterialTheme.colorScheme.inversePrimary
                } else {
                    Color.Green
                }
            )
            Text(text = "$percentageProgress%")
        }
    }
}

@Preview(apiLevel = 33, showBackground = true)
@Preview(apiLevel = 33, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SubjectPreview() {
    StudyTrackerTheme {
        SubjectScreen(
            onBackButtonClick = {},
            onAddTaskButtonClick = {},
            onTaskCardClick = {}
        )
    }
}