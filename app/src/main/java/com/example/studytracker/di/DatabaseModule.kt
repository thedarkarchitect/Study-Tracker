package com.example.studytracker.di

import android.app.Application
import androidx.room.Room
import com.example.studytracker.data.local.SessionDao
import com.example.studytracker.data.local.SubjectDao
import com.example.studytracker.data.local.TaskDao
import com.example.studytracker.data.local.TrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ): TrackerDatabase {
        return Room.databaseBuilder(
            application,
            TrackerDatabase::class.java,
            "studyTracker.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(
        database: TrackerDatabase
    ):SubjectDao {
        return database.subjectDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(
        database: TrackerDatabase
    ): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideSessionDao(
        database: TrackerDatabase
    ): SessionDao {
        return database.sessionDao()
    }
}