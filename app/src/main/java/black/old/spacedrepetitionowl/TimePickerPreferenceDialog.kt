package black.old.spacedrepetitionowl

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.preference.PreferenceDialogFragmentCompat

class TimePickerPreferenceDialog : PreferenceDialogFragmentCompat() {

    lateinit var timepicker: TimePicker

    override fun onCreateDialogView(context: Context?): View {
        timepicker = TimePicker(context)
        return timepicker
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)

        val minutesAfterMidnight = (preference as TimepickerPreference)
            .getPersistedMinutesFromMidnight()
        timepicker.setIs24HourView(true)
        timepicker.hour = minutesAfterMidnight / 60
        timepicker.minute = minutesAfterMidnight % 60
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        // TODO save settings here
        if(positiveResult) {
            val minutesAfterMidnight = (timepicker.hour * 60) + timepicker.minute
            (preference as TimepickerPreference).persistMinutesFromMidnight(minutesAfterMidnight)
            preference.summary = minutesFromMidnightToHourlyTime(minutesAfterMidnight)

        }
    }

    companion object {
        fun newInstance(key: String): TimePickerPreferenceDialog {
            val fragment = TimePickerPreferenceDialog()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle

            return fragment
        }
    }
}