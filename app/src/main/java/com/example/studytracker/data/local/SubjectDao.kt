package com.example.studytracker.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.studytracker.domain.model.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
//we use suspend for one time events controlled bu action and FLow for continuous event that we want to be dynamic
    @Upsert
    suspend fun upsertSubject(subject: Subject)

    @Query("SELECT COUNT(*) FROM SUBJECT")
    fun getTotalSubjectCount(): Flow<Int>

    @Query("SELECT SUM(goalHours) FROM SUBJECT")
    fun getTotalGoalHours(): Flow<Float>

    @Query("SELECT * FROM SUBJECT WHERE subjectId = :subjectId")
    suspend fun getSubjectById(subjectId: Int): Subject?

    @Query("DELETE FROM SUBJECT WHERE subjectId = :subjectId")
    suspend fun deleteSubject(subjectId: Int)

    @Query("SELECT * FROM SUBJECT")
    fun getAllSubjects(): Flow<List<Subject>>

}