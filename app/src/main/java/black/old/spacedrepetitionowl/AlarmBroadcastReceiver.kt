package black.old.spacedrepetitionowl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("FIFTEEN", "inside AlarmBroadcastReceiver")

        NotificationHelper.createSampleDataNotification(
            context,
            "It's time to learn!",
            "How to make coffee",
            "How to make coffee -- 2nd Phase",
            true
        )
    }

}