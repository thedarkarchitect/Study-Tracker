package com.example.studytracker.presentation.session

import android.content.Context
import android.content.Intent

object ServiceHelper {

    fun triggerForegroundService(context: Context, action: String) { // this will be used to pass action to service class
        Intent(context, StudySessionTimerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}