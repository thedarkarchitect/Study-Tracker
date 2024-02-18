package com.example.studytracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Int,
    val relatedToSubject: String,
    val isComplete: Boolean,
    val taskSubjectId: Int, //foreign key from subject
    @PrimaryKey(autoGenerate = true)
    val taskId: Int? = null
)
