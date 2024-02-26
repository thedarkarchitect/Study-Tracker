package com.example.studytracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.studytracker.data.local.dao.SessionDao
import com.example.studytracker.data.local.dao.SubjectDao
import com.example.studytracker.data.local.dao.TaskDao
import com.example.studytracker.domain.model.Session
import com.example.studytracker.domain.model.Subject
import com.example.studytracker.domain.model.Task

@Database(
    entities = [Subject::class, Session::class, Task::class],
    version = 1
)
@TypeConverters(ColorListConverter::class)
abstract class TrackerDatabase: RoomDatabase() {
    abstract fun subjectDao(): SubjectDao

    abstract fun taskDao(): TaskDao

    abstract fun sessionDao(): SessionDao
}