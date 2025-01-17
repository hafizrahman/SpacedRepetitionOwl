package black.old.spacedrepetitionowl

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import black.old.spacedrepetitionowl.models.*
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_subject_view_edit.*

/**
 * A simple [Fragment] subclass.
 * Use the [SubjectViewEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubjectViewEditFragment : Fragment() {
    val args: SubjectViewEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as MainActivity).title = "My title"// Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_subject_view_edit, container, false)

        // The ViewModel is already created on the Activity level (inside MainActivity.kt),
        // so here we are using the Activity's context
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // Date Picker
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        var currentSubject: Subject? = null
        var currentReminders: List<Reminder>? = null
        mainViewModel.getSingleSubjectReminders(args.subjectId)?.observe(
            viewLifecycleOwner,
            Observer { singleSubjectReminders ->

                // Populate local variables with various data from MutableLiveData
                when(singleSubjectReminders) {
                    is singleSubject -> currentSubject = singleSubjectReminders.subject
                    is singleReminders -> currentReminders = singleSubjectReminders.reminders
                }

                // Check and work on everything only when both data are available
                if(currentSubject != null && currentReminders != null) {
                    // Display data //
                    sro_viewedit_subject_title.text = currentSubject!!.content
                    sro_viewedit_subject_content.text = currentSubject!!.content
                    sro_viewedit_starting_date.text = dateStringFormatter(
                        currentSubject!!.startDateTimestamp,
                        true
                    )
                    sro_viewedit_reminder_button_0.text =
                        dateStringFormatter(currentReminders!![0].dateTimestamp)
                    sro_viewedit_reminder_button_1.text =
                        dateStringFormatter(currentReminders!![1].dateTimestamp)
                    sro_viewedit_reminder_button_2.text =
                        dateStringFormatter(currentReminders!![2].dateTimestamp)
                    sro_viewedit_reminder_button_3.text =
                        dateStringFormatter(currentReminders!![3].dateTimestamp)
                    toggleButton(currentReminders!![0].checked, sro_viewedit_reminder_button_0)
                    toggleButton(currentReminders!![1].checked, sro_viewedit_reminder_button_1)
                    toggleButton(currentReminders!![2].checked, sro_viewedit_reminder_button_2)
                    toggleButton(currentReminders!![3].checked, sro_viewedit_reminder_button_3)

                    if(currentSubject!!.url.isNotEmpty()) {
                        sro_viewedit_subject_url.text = currentSubject!!.url
                    }
                    if(currentSubject!!.notes.isNotEmpty()) {
                        sro_viewedit_subject_notes.text = currentSubject!!.notes
                    }

                    // Edit data //

                    // edit subject content
                    sro_viewedit_subject_content.setOnClickListener { view ->
                        val action = SubjectViewEditFragmentDirections
                            .actionSubjectViewEditFragmentToDialogViewEditSubjectElementFragment(
                                currentSubject!!.id,
                                currentSubject!!.content,
                                1
                            )
                        findNavController().navigate(action)
                    }

                    // - Edit starting date
                    sro_viewedit_starting_date.setOnClickListener { view ->
                        picker.show(requireActivity().supportFragmentManager, picker.toString())
                    }
                    picker.addOnPositiveButtonClickListener { selectedTimestamp ->
                        // Alert asking for confirmation to save the date changes.
                        // Confirmation is needed because a date change by default resets all
                        // progresses.
                        val builder = AlertDialog.Builder(requireActivity())
                        builder.setTitle(getString(R.string.dialog_date_edited_alert_heading))
                        builder.setMessage(getString(R.string.dialog_date_edited_alert_message))

                        // Reminder: selectedTimestamp from the picker is the value of the selected
                        // day on 00:00 at UTC+0. Here we wrangle that so the selected date
                        // matches the user's timezone instead. More information in Utils.txt
                        // for the startDayUTCMillisToStartDaySystemTimezoneMillis()
                        // function.
                        val userTimezoneMillis =
                            startDayUTCMillisToStartDaySystemTimezoneMillis(selectedTimestamp)

                        // After getting the start date on the correct timezone, add in the
                        // correct time as set in the preference.
                        val sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(requireActivity())
                        val notifTimePreferenceInMinutes = sharedPreferences.getInt(
                            "pref_notification_time",
                            TimepickerPreference.DEFAULT_MINUTES_FROM_MIDNIGHT)
                        val notifTimePreferenceInMillis = notifTimePreferenceInMinutes * 60000
                        val finalMillis = userTimezoneMillis + notifTimePreferenceInMillis

                        // Positive button
                        builder.setPositiveButton(
                            getString(R.string.dialog_date_edited_alert_positive)) { dialog, which ->
                            mainViewModel.updateSubjectStartDate(args.subjectId, finalMillis)

                            sro_viewedit_starting_date.text = dateStringFormatter(
                                finalMillis,
                                true
                            )

                            // Reset progress on all subsequent reminders.
                            // mainViewModel.resetRemindersCheckedStateForSubject(args.subjectId)

                            // Update new reminder dates based on new starting date
                            val reminderDates = getReminderDates(finalMillis)
                            reminderDates.forEachIndexed { index, value ->
                                mainViewModel.updateDateTimestampAndReset(
                                    currentReminders!![index].id,
                                    value
                                )

                                // TODO update existing reminders with the new one
                                // our pending intents is created using "PendingIntent.FLAG_UPDATE_CURRENT"
                                // so it should be possible to update them with the new timestamps,
                                // provided we enter them with AlarmManager.createReminderPendingIntent()
                                // using identical parameters
                                // The function below does not work yet, and in fact it
                                // deletes everything :(
                                /*
                                updateNotificationAlarm(
                                    requireActivity(),
                                    currentSubject!!,
                                    value,
                                    currentReminders!![index].id
                                    )

                                 */
                            }

                            // Let user know
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.dialog_date_edited_alert_positive_confirm),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        // Neutral button
                        builder.setNeutralButton(
                            getString(R.string.dialog_note_edited_alert_neutral)) { dialog, which ->
                            // Do nothing here, just dismiss the alert.
                        }

                        val dialog: AlertDialog = builder.create()
                        // Show the alert only if the selected date from picker differs from the current
                        // date.
                        if (userTimezoneMillis != currentSubject!!.startDateTimestamp) {
                            dialog.show()
                        }
                    }

                    // - Edit and open URL
                    sro_viewedit_subject_url.setOnClickListener { view ->
                        val action = SubjectViewEditFragmentDirections
                            .actionSubjectViewEditFragmentToDialogViewEditSubjectElementFragment(
                                currentSubject!!.id,
                                currentSubject!!.url,
                                2
                            )
                        findNavController().navigate(action)
                    }
                    sro_viewedit_subject_url_view.setOnClickListener { view ->
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(currentSubject!!.url)
                        startActivity(intent)
                    }

                    // - Edit notes
                    sro_viewedit_subject_notes.setOnClickListener { view ->
                        val action = SubjectViewEditFragmentDirections
                            .actionSubjectViewEditFragmentToSubjectNoteEditFragment(
                                currentSubject!!.id,
                                currentSubject!!.notes
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        )

        return view
    }

    private fun toggleButton(checked: Boolean, button: Button) {
        if(checked)
            toggleReminderButtonChecked(button)
        else
            toggleReminderButtonUnchecked(button)
    }

    // Visual state for when the date buttons have the checked state
    private fun toggleReminderButtonChecked(button: Button) {
        button.setBackgroundColor(
            ContextCompat.getColor(button.context, R.color.colorPrimary ))
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            0,
            R.drawable.ic_baseline_check_24
        )
        button.setTextColor(
            ContextCompat.getColor(button.context, R.color.colorWhite ))
    }

    // Visual state for when the date buttons have the unchecked state
    private fun toggleReminderButtonUnchecked(button: Button) {
        button.setBackgroundColor((Color.parseColor("#FFFFFF")))
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            0,
            R.drawable.ic_baseline_alarm_on_24
        )
        button.setTextColor(
            ContextCompat.getColor(button.context, R.color.colorDarkGray ))
    }

    private fun updateNotificationAlarm(context: Context,
                                        subject: Subject,
                                        reminder_timestamp: Long,
                                        reminder_id: Long
    ) {
        val notifPendingIntent = AlarmScheduler.createReminderPendingIntent(
            context,
            subject,
            reminder_timestamp,
            reminder_id
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        AlarmScheduler.scheduleAlarm(
            context,
            reminder_timestamp,
            notifPendingIntent,
            alarmManager
        )
    }
}