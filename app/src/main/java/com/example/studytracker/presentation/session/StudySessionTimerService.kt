package com.example.studytracker.presentation.session

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat.Builder
import com.example.studytracker.util.Constants.ACTION_SERVICE_CANCEL
import com.example.studytracker.util.Constants.ACTION_SERVICE_START
import com.example.studytracker.util.Constants.ACTION_SERVICE_STOP
import com.example.studytracker.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.studytracker.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.studytracker.util.Constants.NOTIFICATION_ID
import com.example.studytracker.util.pad
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class StudySessionTimerService: Service() {

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: Builder

    private lateinit var timer: Timer

    var duration: Duration = Duration.ZERO
        private set

    var seconds = mutableStateOf("00")
        private set

    var minutes = mutableStateOf("00")
        private set

    var hours = mutableStateOf("00")
        private set

    var currentTimerState = mutableStateOf(TimerState.IDLE)
        private set

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action.let {
            when(it){
                ACTION_SERVICE_START -> {
                    startForegroundService()
                    startTimer { hours, minutes, seconds ->

                    }
                }
                ACTION_SERVICE_STOP -> {}
                ACTION_SERVICE_CANCEL -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService(){
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW //because it's running for long more than an hour
        )

        notificationManager.createNotificationChannel(channel)
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder
                .setContentText("$hours:$minutes:$seconds")
                .build()
        )
    }

    private fun startTimer(
      onTick: (h: String, m: String, s: String) -> Unit
    ){
        currentTimerState.value = TimerState.STARTED
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L){
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    private fun updateTimeUnits() {
        duration.toComponents{ hours, minutes, seconds, _ ->
            this@StudySessionTimerService.hours.value = hours.toInt().pad()
            this@StudySessionTimerService.minutes.value = minutes.pad()
            this@StudySessionTimerService.seconds.value = seconds.pad()
        }
    }

}

enum class TimerState {
    IDLE,
    STARTED,
    STOPPED
}