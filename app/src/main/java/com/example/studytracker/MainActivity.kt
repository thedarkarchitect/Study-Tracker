package com.example.studytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.toArgb
import com.example.studytracker.domain.model.Session
import com.example.studytracker.domain.model.Subject
import com.example.studytracker.domain.model.Task
import com.example.studytracker.presentation.NavGraphs
import com.example.studytracker.ui.theme.StudyTrackerTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyTrackerTheme {
               DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

val subjects = listOf(
    Subject(name = "Art", goalHours = 10f, colors = Subject.subjectCardColors.random().map{ it.toArgb() }, subjectId = 0),
    Subject(name = "Literature", goalHours = 10f, colors = Subject.subjectCardColors.random().map{ it.toArgb() }, subjectId = 0),
    Subject(name = "Geography", goalHours = 10f, colors = Subject.subjectCardColors.random().map{ it.toArgb() }, subjectId = 0),
    Subject(name = "Mathematics", goalHours = 10f, colors = Subject.subjectCardColors.random().map{ it.toArgb() }, subjectId = 0),
    Subject(name = "Biology", goalHours = 10f, colors = Subject.subjectCardColors.random().map{ it.toArgb() }, subjectId = 0),
    Subject(name = "Business", goalHours = 10f, colors = Subject.subjectCardColors.random().map{ it.toArgb() }, subjectId = 0)
)

val tasks = listOf(
    Task(
        title = "Prepare notes",
        description = "",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "",
        isComplete = false,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Do Homework",
        description = "",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "",
        isComplete = true,
        taskSubjectId = 0,
        taskId = 1
    ),
    Task(
        title = "Write essays",
        description = "",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "",
        isComplete = false,
        taskSubjectId = 0,
        taskId = 1
    ),
)

val sessions = listOf(
    Session(
        relatedToSubject = "English",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0
    ),
    Session(
        relatedToSubject = "English",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0
    ),
    Session(
        relatedToSubject = "English",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0
    ),
    Session(
        relatedToSubject = "English",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0
    ),
)