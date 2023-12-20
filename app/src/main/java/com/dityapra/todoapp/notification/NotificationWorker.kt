package com.dityapra.todoapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dityapra.todoapp.R
import com.dityapra.todoapp.data.Task
import com.dityapra.todoapp.data.TaskRepository
import com.dityapra.todoapp.ui.detail.DetailTaskActivity
import com.dityapra.todoapp.utils.DateConverter
import com.dityapra.todoapp.utils.NOTIFICATION_CHANNEL_ID
import com.dityapra.todoapp.utils.TASK_ID

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID)
    private val taskRepository = TaskRepository.getInstance(applicationContext)

    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
    }

    override fun doWork(): Result {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val flag = sharedPreferences.getBoolean(
            applicationContext.getString(R.string.pref_key_notify),
            false
        )

        if (flag) {
            val activeTask = taskRepository.getNearestActiveTask()

            if (activeTask != null) {
                val pendingIntent = getPendingIntent(activeTask)
                val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification =
                    NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notifications)
                        .setContentIntent(pendingIntent)
                        .setContentTitle(activeTask.title)
                        .setContentText(
                            String.format(
                                applicationContext.getString(R.string.notify_content),
                                DateConverter.convertMillisToString(activeTask.dueDateMillis)
                            )
                        )
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setColor(
                            ContextCompat.getColor(
                                applicationContext,
                                android.R.color.transparent
                            )
                        )
                        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                        .setSound(alarmSound)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    channel.enableVibration(true)
                    channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
                    notification.setChannelId(NOTIFICATION_CHANNEL_ID)
                    notificationManager.createNotificationChannel(channel)
                }
                notificationManager.notify(1, notification.build())
            }
        }
        return Result.success()
    }
}