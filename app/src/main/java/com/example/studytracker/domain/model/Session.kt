package com.example.studytracker.domain.model

data class Session(
    val sessionSubjectId: Int, //foreign key
    val relatedToSubject: String,
    val date: Long,
    val duration: Long,
    val sessionId: Int
)