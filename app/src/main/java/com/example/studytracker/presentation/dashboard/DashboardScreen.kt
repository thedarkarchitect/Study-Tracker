package com.example.studytracker.presentation.dashboard


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studytracker.R
import com.example.studytracker.domain.model.Subject
import com.example.studytracker.presentation.components.AddSubjectDialog
import com.example.studytracker.presentation.components.CountCard
import com.example.studytracker.presentation.components.DeleteDialog
import com.example.studytracker.presentation.components.SubjectCard
import com.example.studytracker.presentation.components.studySessionsList
import com.example.studytracker.presentation.components.tasksList
import com.example.studytracker.presentation.destinations.SessionScreenRouteDestination
import com.example.studytracker.presentation.destinations.SubjectScreenRouteDestination
import com.example.studytracker.presentation.destinations.TaskScreenRouteDestination
import com.example.studytracker.presentation.subject.SubjectScreenNavArgs
import com.example.studytracker.presentation.task.TaskScreenNavArgs
import com.example.studytracker.sessions
import com.example.studytracker.subjects
import com.example.studytracker.tasks
import com.example.studytracker.ui.theme.StudyTrackerTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun DashboardScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel: DashboardViewModel = hiltViewModel()

    DashboardScreen(
        onSubjectCardClick = { subjectId ->
            subjectId?.let {
                val navArg = SubjectScreenNavArgs(subjectId = subjectId)
                navigator.navigate(SubjectScreenRouteDestination(navArgs = navArg))
            }
        },
        onTaskCardClick = { taskId ->
            val navArg = TaskScreenNavArgs(taskId = taskId, subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))
        },
        onStartSessionButtonClick = {
            navigator.navigate(SessionScreenRouteDestination())
        }
    )
}

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onSubjectCardClick: (Int?) -> Unit,
    onTaskCardClick: (Int?) -> Unit,
    onStartSessionButtonClick: () -> Unit
) {


    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
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
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session",
        bodyText = "Are you sure, you want to delete this session? Your studied hours will be reduced by this session time. This action can not be undone.",
        onDismissRequest = { isDeleteSessionDialogOpen = false },
        onConfirmButtonClick = { isDeleteSessionDialogOpen = false }
    )

    Scaffold(
        topBar = { DashboardTopBar() }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){

            item {
                CountCardsSection(
                    subjectCount = 5,
                    studiedHours = "10",
                    goalHours = "15"
                )
            }

//            item {
//                SubjectCardsSection(
//                    modifier = modifier.fillMaxWidth(),
//                    subjectList = emptyList()
//                )
//            }

            item {
                SubjectCardsSection(
                    modifier = modifier.fillMaxWidth(),
                    subjectList = subjects,
                    onAddIconClicked = { isAddSubjectDialogOpen = true },
                    onSubjectCardClick = onSubjectCardClick
                )
            }
            item {
                Button(
                    onClick = onStartSessionButtonClick,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.inversePrimary
                    )
                ){
                    Text(
                        text = stringResource(R.string.start_study_session)
                    )
                }
            }
            tasksList(
                sectionTitle = "UPCOMING TASKS",
                emptyTaskText = "You don't have any upcoming tasks.\n Click the + button in subject screen to add new task.",
//                tasks = emptyList()
                tasks = tasks,
                onCheckBoxClick = {},
                onTaskCardClick = onTaskCardClick
            )

            item {
                Spacer(modifier = modifier.height(20.dp))
            }

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
fun DashboardTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.studytracker),
                style = MaterialTheme.typography.displaySmall
            )
        }
    )
}

@Composable
fun CountCardsSection(
    modifier: Modifier = Modifier,
    subjectCount: Int,
    studiedHours: String,
    goalHours: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        CountCard(
            headingText = "Subject Count",
            count = "$subjectCount"
        )
        Spacer(modifier = modifier.width(10.dp))
        CountCard(
            headingText = "Studied Hours",
            count = studiedHours
        )
        Spacer(modifier = modifier.width(10.dp))
        CountCard(
            headingText = "Goals Study Hours",
            count = goalHours
        )
    }
}

@Composable
fun SubjectCardsSection(
    modifier: Modifier = Modifier,
    subjectList: List<Subject>,
    emptyListText: String = stringResource(R.string.empty_subject),
    onAddIconClicked: () -> Unit,
    onSubjectCardClick: (Int?) -> Unit
) {
    Column{
        //always shown
        Row (
            modifier = modifier.padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.subjects),
                style = MaterialTheme.typography.bodySmall,
            )

            IconButton(
                onClick = onAddIconClicked
            ) {
               Icon(
                   imageVector = Icons.Default.Add,
                   contentDescription = stringResource(R.string.add_subject)
               )
            }
        }

        //this appears on condition
        if (subjectList.isEmpty()){
            Image(
                modifier = modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp, bottom = 8.dp),
                painter = painterResource(id = R.drawable.books),
                contentDescription = stringResource(R.string.empty_subject)
            )
            Text(
                modifier = modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ){
            items(subjectList){ it ->
                SubjectCard(
                    subjectName = it.name,
                    gradientColors = it.colors.map{  colorInt ->
                        Color(colorInt)
                    },
                    onClick = { onSubjectCardClick(it.subjectId) }
                )
            }
        }
    }
}

@Preview(apiLevel = 33, showBackground = true)
@Preview(apiLevel = 33, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashBoardPreview() {
    StudyTrackerTheme {
        DashboardScreen(
            onSubjectCardClick = {},
            onTaskCardClick = {},
            onStartSessionButtonClick = {}
        )
    }
}

