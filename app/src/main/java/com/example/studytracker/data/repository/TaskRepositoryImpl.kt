package com.example.studytracker.data.repository

import com.example.studytracker.data.local.TaskDao
import com.example.studytracker.domain.model.Task
import com.example.studytracker.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
): TaskRepository {
    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(taskId: Int): Task? {
        TODO("Not yet implemented")
    }

    override fun getUpcomingTasksForSubject(subjectId: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getCompletedTasksForSubject(subjectId: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getAllUpcomingTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
            .map { tasks -> tasks.filter { it.isComplete.not() } }//this will return all upcoming tasks that are not complete
            .map { tasks -> sortTasks(tasks) }//the not completed tasks will further be mapped/organized by the sort function
    }

    private fun sortTasks(tasks: List<Task>): List<Task> { //this will sort task according to due date and priority
        return tasks.sortedWith(
            compareBy<Task> {
                it.dueDate //tasks will be sorted by their due date in a chronological order from first to latest due date
            }.thenByDescending {
                it.priority //then by descending using the priority
            }
            //this will sort the task list in a way that duedate will come from smallest to highest in Long type setting
            // and the different due dates will also be organized in their priority heiracy
            //mean small and high due date will be organized in their priority levels
        )
    }

}