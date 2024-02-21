package com.example.studytracker.di

import com.example.studytracker.data.repository.SessionRepositoryImpl
import com.example.studytracker.data.repository.SubjectRepositoryImpl
import com.example.studytracker.data.repository.TaskRepositoryImpl
import com.example.studytracker.domain.repository.SessionRepository
import com.example.studytracker.domain.repository.SubjectRepository
import com.example.studytracker.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSubjectRepository(
        impl: SubjectRepositoryImpl
    ): SubjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(
        impl: SessionRepositoryImpl
    ): SessionRepository

}