package black.old.spacedrepetitionowl

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

object AlarmScheduler {
    fun createPendingIntent(context: Context): PendingIntent {
        Log.d("FIFTEEN", "AlarmScheduler.createPendingIntent()")
        val intent = Intent(context.applicationContext, AlarmBroadcastReceiver::class.java).apply {
            putExtra("bodytext", "Hello from alarm")
        }

        return PendingIntent.getBroadcast(
            context,
            1338,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun scheduleAlarm(context: Context, alarmTime: Long, alarmIntent: PendingIntent, manager: AlarmManager) {
        Log.d("FIFTEEN", "AlarmScheduler.scheduleAlarm()")
        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            alarmIntent
        )

    }

}