package com.example.studytracker.presentation.session.components

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studytracker.presentation.components.DeleteDialog
import com.example.studytracker.presentation.components.SubjectListBottomSheet
import com.example.studytracker.presentation.components.studySessionsList
import com.example.studytracker.presentation.session.ServiceHelper
import com.example.studytracker.presentation.session.SessionEvent
import com.example.studytracker.presentation.session.SessionState
import com.example.studytracker.presentation.session.SessionViewModel
import com.example.studytracker.presentation.session.StudySessionTimerService
import com.example.studytracker.presentation.session.TimerState
import com.example.studytracker.ui.theme.StudyTrackerTheme
import com.example.studytracker.util.Constants.ACTION_SERVICE_CANCEL
import com.example.studytracker.util.Constants.ACTION_SERVICE_START
import com.example.studytracker.util.Constants.ACTION_SERVICE_STOP
import com.example.studytracker.util.SnackbarEvent
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit

@Destination(
    deepLinks = [
        DeepLink(
            action = Intent.ACTION_VIEW,
            uriPattern = "study_tracker://dashboard/session"
        )
    ]
)
@Composable
fun SessionScreenRoute(
    navigator: DestinationsNavigator,
    timerService: StudySessionTimerService
) {

    val viewModel: SessionViewModel = hiltViewModel()

    SessionScreen(
        state = viewModel.state.collectAsState().value,
        onEvent = viewModel::onEvent,
        onBackButtonClick = { navigator.navigateUp() },
        timerService = timerService,
        snackbarEvent = viewModel.snackbarEventFlow
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    modifier: Modifier = Modifier,
    state: SessionState,
    onEvent: (SessionEvent) -> Unit,
    onBackButtonClick: () -> Unit,
    snackbarEvent: SharedFlow<SnackbarEvent>,
    timerService: StudySessionTimerService
) {

    val hours by timerService.hours
    val minutes by timerService.minutes
    val seconds by timerService.seconds
    val currentTimerState by timerService.currentTimerState

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember { mutableStateOf(false) }

    var isDeleteSessionDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }


    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true){ // this is useful when you want to execute non composable code or code that runs in a coroutine
        snackbarEvent.collectLatest {  event ->
            when(event){
                is SnackbarEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackbarEvent.NavigateUp -> {

                }
            }
        }
    }

    LaunchedEffect(key1 = state.subjects) {
        val subjectId = timerService.subjectId.value
        onEvent(
            SessionEvent.UpdateSubjectIdAndRelatedSubject(
                subjectId = subjectId,
                relatedToSubject = state.subjects.find { it.subjectId == subjectId }?.name
            )
        )
    }

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isBottomSheetOpen,
        subjects = state.subjects,
        onSubjectClicked = { subject ->
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if(!sheetState.isVisible) isBottomSheetOpen = false
            }
            onEvent(SessionEvent.OnRelatedSubjectChange(subject))
        },
        onDismissRequest = { isBottomSheetOpen = false }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session",
        bodyText = "Are you sure, you want to delete this session? Your studied hours will be reduced by this session time. This action can not be undone.",
        onDismissRequest = { isDeleteSessionDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(SessionEvent.DeleteSession)
            isDeleteSessionDialogOpen = false
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            SessionScreenTopBar(
                onBackButtonClick = onBackButtonClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            item {
                TimerSection(
                    modifier = modifier
                        .padding(20.dp)
                        .fillMaxSize()
                        .aspectRatio(1f),
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds
                )
            }
            item {
                RelatedToSubjectSection(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    relatedToSubject = state.relatedToSubject ?: "",
                    selectSubjectButtonClick = { isBottomSheetOpen = true },
                    seconds = seconds
                )
            }
            item {
                ButtonSection(
                    startButtonClicked = {
                        if (state.subjectId != null && state.relatedToSubject != null){
                            ServiceHelper.triggerForegroundService(
                                context = context,
                                action = if(currentTimerState == TimerState.STARTED) ACTION_SERVICE_STOP else ACTION_SERVICE_START
                            )
                            timerService.subjectId.value = state.subjectId
                        } else {
                            onEvent(SessionEvent.NotifyToUpdateSubject)
                        }
                    },
                    cancelButtonClicked = {
                        ServiceHelper.triggerForegroundService(
                        context = context,
                        action = ACTION_SERVICE_CANCEL
                        )
                    },
                    finishButtonClicked = {
                        val duration = timerService.duration.toLong(DurationUnit.SECONDS)
                        if(duration >= 36){
                            ServiceHelper.triggerForegroundService(
                                context = context,
                                action = ACTION_SERVICE_CANCEL
                            )
                        }
                        onEvent(SessionEvent.SaveSession(duration))
                    },
                    timerState = currentTimerState,
                    seconds = seconds
                )
            }
            studySessionsList(
                sectionTitle = "RECENT STUDY SESSIONS",
                emptyTaskText = "You don't have any recent study sessions.\n Start a study session to begin recording your progress.",
                sessions = state.sessions,
                onDeleteIconClick = { session ->
                    isDeleteSessionDialogOpen = true
                    onEvent(SessionEvent.OnDeleteSessionButtonClick(session))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreenTopBar(
    onBackButtonClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
             IconButton(onClick = onBackButtonClick) {
                 Icon(
                     imageVector = Icons.Default.ArrowBack,
                     contentDescription = "Navigate to Back Screen"
                 )
             }
        },
        title = { 
            Text(
                text = "Study Sessions",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    )    
}

@Composable
fun TimerSection(
    modifier: Modifier = Modifier,
    hours: String,
    minutes: String,
    seconds: String,
) {
   Box(
       modifier = modifier,
       contentAlignment = Alignment.Center
   ) {
        Box(
            modifier = modifier
                .size(250.dp)
                .border(5.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
        )

        Row {
            AnimatedContent(
                targetState = hours,
                label = hours,
                transitionSpec = { timerTextAnimation() }
            ){ hours ->
                Text(
                    text = "$hours:",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }
            AnimatedContent(
                targetState = minutes,
                label = minutes,
                transitionSpec = { timerTextAnimation() }
            ){ minutes ->
                Text(
                    text = "$minutes:",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }
            AnimatedContent(
                targetState = seconds,
                label = seconds,
                transitionSpec = { timerTextAnimation() }
            ){ seconds ->
                Text(
                    text = seconds,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp)
                )
            }
        }
   }
}

@Composable
fun RelatedToSubjectSection(
    modifier: Modifier = Modifier,
    relatedToSubject: String,
    selectSubjectButtonClick: () -> Unit,
    seconds: String
) {
    Column(
        modifier = modifier
    ){
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
                text = relatedToSubject,
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(
                onClick = selectSubjectButtonClick,
                enabled = seconds == "00"
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Subject"
                )
            }
        }
    }
}

@Composable
fun ButtonSection(
    modifier: Modifier = Modifier,
    startButtonClicked: () -> Unit,
    cancelButtonClicked: () -> Unit,
    finishButtonClicked: () -> Unit,
    timerState: TimerState,
    seconds: String
) {
    Row(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = cancelButtonClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary
            ),
            enabled = seconds != "00" && timerState != TimerState.STARTED
        ) {
            Text(
                modifier = modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Cancel"
            )
        }
        Button(
            onClick = startButtonClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = if(timerState == TimerState.STARTED) Color.Red else MaterialTheme.colorScheme.inversePrimary,
                contentColor = Color.White
            )
        ) {
            Text(
                modifier = modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = when(timerState) {
                    TimerState.STARTED -> "Stop"
                    TimerState.STOPPED -> "Resume"
                    else -> "Start"
                }
            )
        }
        Button(
            onClick = finishButtonClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary
            )
        ) {
            Text(
                modifier = modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Finish"
            )
        }
    }
}

private fun timerTextAnimation(duration: Int = 600): ContentTransform {
    return slideInVertically(animationSpec = tween(duration)) { fullHeight -> fullHeight } +
            fadeIn(animationSpec = tween(duration)) togetherWith
            slideOutVertically(animationSpec = tween(duration)) { fullHeight -> fullHeight } +
            fadeOut(animationSpec = tween(duration))
}


@Preview(apiLevel = 33)
@Preview(apiLevel = 33, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SessionPreview() {
    StudyTrackerTheme {
        SessionScreen(
            onBackButtonClick = {},
            timerService = StudySessionTimerService(),
            state = SessionState(),
            onEvent = {},
            snackbarEvent = MutableSharedFlow()
        )
    }
}