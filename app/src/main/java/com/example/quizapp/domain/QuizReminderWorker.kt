package com.example.quizapp.domain

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quizapp.MainActivity
import com.example.quizapp.R

class QuizReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val notification = NotificationCompat.Builder(
            applicationContext,
            "quiz_reminders"
        ).apply {
            setSmallIcon(R.drawable.ic_quiz_notification)
            setContentTitle("Пора пройти квиз!")
            setContentText("Не забудьте проверить свои знания сегодня")
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setContentIntent(PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            ))
            setAutoCancel(true)
        }.build()

        (applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .notify(1, notification)
    }
}