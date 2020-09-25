package black.old.spacedrepetitionowl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if(context != null && intent != null && intent.action != null) {
            if (intent.action!! == "ACTION_SEND_LEARNING_REMINDER") {
                if (intent.extras != null) {
                    val reminder_text = intent.extras!!.getString("notification_title")
                    val reminder_id = intent.extras!!.getLong("notification_subject_id")
                    if (reminder_text != null && reminder_id != null) {
                        // 3
                        NotificationHelper.createReminderNotification(
                            context,
                            reminder_id,
                            "Spaced Repetition Owl: Time for learning",
                            reminder_text,
                            reminder_text,
                            true
                        )
                    }
                }
            }
        }
    }

}