package com.example.studytracker.data.repository

import com.example.studytracker.data.local.dao.SessionDao
import com.example.studytracker.data.local.dao.SubjectDao
import com.example.studytracker.data.local.dao.TaskDao
import com.example.studytracker.domain.model.Subject
import com.example.studytracker.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao,
    private val taskDao: TaskDao,
    private val sessionDao: SessionDao
): SubjectRepository {

    override suspend fun upsertSubject(subject: Subject) {
        subjectDao.upsertSubject(subject = subject)
    }

    override fun getTotalSubjectCount(): Flow<Int> {
        return subjectDao.getTotalSubjectCount()
    }

    override fun getTotalGoalHours(): Flow<Float> {
        return subjectDao.getTotalGoalHours()
    }

    override suspend fun deleteSubject(subjectId: Int) {
        taskDao.deleteTasksBySubjectId(subjectId) //first delete all tasks related to subject
        sessionDao.deleteSessionsBySubjectId(subjectId) //then all sessions
        subjectDao.deleteSubject(subjectId) //then delete the subject itself
    }

    override suspend fun getSubjectById(subjectId: Int): Subject? {
        return subjectDao.getSubjectById(subjectId = subjectId)
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return subjectDao.getAllSubjects()
    }

}