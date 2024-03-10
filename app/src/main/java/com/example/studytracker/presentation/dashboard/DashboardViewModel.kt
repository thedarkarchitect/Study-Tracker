package com.example.studytracker.presentation.dashboard

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studytracker.domain.model.Session
import com.example.studytracker.domain.model.Subject
import com.example.studytracker.domain.model.Task
import com.example.studytracker.domain.repository.SubjectRepository
import com.example.studytracker.domain.repository.SessionRepository
import com.example.studytracker.domain.repository.TaskRepository
import com.example.studytracker.util.SnackbarEvent
import com.example.studytracker.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())

    val state = combine( //combining flows to get emitted values
        _state,
        subjectRepository.getTotalSubjectCount(),
        subjectRepository.getTotalGoalHours(),
        subjectRepository.getAllSubjects(),
        sessionRepository.getTotalSessionsDuration()
    ){ state, subjectCount, goalHours, subjects, totalSessionDuration -> //update state values with these flows
        state.copy(
            totalSubjectCount = subjectCount,
            totalGoalStudyHours = goalHours,
            subjects = subjects,
            totalStudiedHours = totalSessionDuration.toHours()
        )
    }.stateIn( //makes the cold flow a hot flow
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )

    val tasks: StateFlow<List<Task>> = taskRepository.getAllUpcomingTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val recentSession: StateFlow<List<Session>> = sessionRepository.getRecentFiveSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    private val _snackbarEventFlow = MutableSharedFlow<SnackbarEvent>() // we using shareFlow because it doesn't hold any initial value only values emitted by action
    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()

    fun onEvent(event: DashboardEvent) {
        when (event){
            DashboardEvent.DeleteSession -> deleteSession()
            is DashboardEvent.OnDeleteSessionButtonClicked -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }
            is DashboardEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy( goalStudyHours = event.hours )
                }
            }
            is DashboardEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy( subjectName = event.name )
                }
            }
            is DashboardEvent.OnTaskIsCompleteChange -> {
                updateTask(event.task)
            }
            is DashboardEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(subjectCardColors = event.colors)
                }
            }
            DashboardEvent.SaveSubject -> saveSubject()
        }
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.upsertTask(
                    task = task.copy(isComplete = !task.isComplete)
                )
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar("Saved in completed tasks.")
                )
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        "Couldn't update subject ${e.message}",
                        SnackbarDuration.Long
                    )

                )
            }
        }
    }

    private fun saveSubject() {
        viewModelScope.launch {
            try {
                subjectRepository.upsertSubject( //because its a suspend function we use the coroutine
                    subject = Subject(
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.subjectCardColors.map { it.toArgb() }
                    )
                )
                _state.update {//after saving reset fields
                    it.copy(
                        subjectName = "",
                        goalStudyHours = "",
                        subjectCardColors = Subject.subjectCardColors.random()
                    )
                }
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar("Subject saved Successfully")
                )
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        "Couldn't save subject ${e.message}",
                        SnackbarDuration.Long
                        )

                )
            }
        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            try {
                state.value.session?.let { session ->
                    sessionRepository.deleteSession(session)
                }
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(message = "Session deleted successfully")
                )
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Couldn't delete session. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }
}