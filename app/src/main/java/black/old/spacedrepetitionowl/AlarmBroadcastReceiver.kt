package black.old.spacedrepetitionowl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationHelper.createSampleDataNotification(
            context,
            "It's time to learn!",
            "How to make coffee",
            "How to make coffee -- 2nd Phase",
            true
        )
    }

}