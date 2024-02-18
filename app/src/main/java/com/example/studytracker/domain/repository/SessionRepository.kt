package com.example.studytracker.domain.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.studytracker.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun InsertSession(session: Session)

    suspend fun deleteSession(session: Session)

    fun getAllSessions(): Flow<List<Session>>

    fun getRecentFiveSessions(): Flow<List<Session>>

    fun getRecentTenSessionsForSubject(subjectId: Int): Flow<List<Session>>

    fun getTotalSessionsDuration(): Flow<Long>

    fun getTotalSessionsDurationBySubjectId(subjectId: Int): Flow<Long>
}