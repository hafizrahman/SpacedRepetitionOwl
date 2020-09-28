package black.old.spacedrepetitionowl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {


        if(context != null && intent != null && intent.action != null) {
            if (intent.action!! == context.getString(R.string.key_bc_notif_action)) {
                if (intent.extras != null) {
                    val reminder_text = intent.extras!!.getString(context.getString(
                        R.string.key_notif_title))
                    val reminder_id = intent.extras!!.getLong(context.getString(
                        R.string.key_notif_subject_id
                    ))

                    if (reminder_text != null && reminder_id != null) {
                        NotificationHelper.createReminderNotification(
                            context,
                            reminder_id,
                            context.getString(R.string.notification_title),
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