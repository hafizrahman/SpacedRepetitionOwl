package black.old.spacedrepetitionowl

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import black.old.spacedrepetitionowl.models.*
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_subject_view_edit.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubjectViewEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubjectViewEditFragment : Fragment() {
    val args: SubjectViewEditFragmentArgs by navArgs()
   // lateinit var reminders: MutableList<Reminder>

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

        // Fill in existing data

        var currentSubject: Subject? = null
        var currentReminders: List<Reminder>? = null

        mainViewModel.getSingleSubjectReminders(args.subjectId)?.observe(
            viewLifecycleOwner,
            Observer { singleSubjectReminders ->
                when(singleSubjectReminders) {
                    is singleSubject -> currentSubject = singleSubjectReminders.subject
                    is singleReminders -> currentReminders = singleSubjectReminders.reminders
                }
                // Check and work on everything only when both data are available
                if(currentSubject != null && currentReminders != null) {
                    // Display data //
                    sro_viewedit_subject_title.text = currentSubject!!.content
                    sro_viewedit_subject_title_editable.setText(currentSubject!!.content)
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

                    // TODO - edit subject title

                    // - Edit starting date
                    sro_viewedit_starting_date.setOnClickListener { view ->
                        picker.show(requireActivity().supportFragmentManager, picker.toString())
                    }
                    picker.addOnPositiveButtonClickListener { selectedTimestamp ->
                        // Alert asking for confirmation to save the date changes.
                        // Confirmation is needed because a date change by default resets all
                        // progresses.
                        val builder = AlertDialog.Builder(requireActivity())
                        builder.setTitle("Change Starting Date")
                        builder.setMessage("Save new starting date? This will reset all progress.")

                        // Positive button
                        builder.setPositiveButton("YES") { dialog, which ->
                            mainViewModel.updateSubjectStartDate(args.subjectId, selectedTimestamp)
                            sro_viewedit_starting_date.text = dateStringFormatter(
                                selectedTimestamp,
                                true
                            )

                            // Reset progress on all subsequent reminders.
                            mainViewModel.resetRemindersCheckedStateForSubject(args.subjectId)

                            // TODO update new reminder dates based on new starting date
                            // We have all four reminder id's, so now we need to:
                            // - recalculate dates
                            // - update all four reminders

                            // TODO delete existing notifications with the old dates

                            // TODO create new notifications with the new dates

                            // Let user know
                            Toast.makeText(
                                requireActivity(), "New date saved.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        // Neutral button
                        builder.setNeutralButton("Cancel") { dialog, which ->
                            // Do nothing here, just dismiss the alert.
                        }

                        val dialog: AlertDialog = builder.create()
                        // Show the alert only if the selected date from picker differs from the current
                        // date.
                        if (selectedTimestamp != currentSubject!!.startDateTimestamp) {
                            dialog.show()
                        }
                    }

                    // - Edit URL
                    sro_viewedit_subject_url.setOnClickListener { view ->
                        val action = SubjectViewEditFragmentDirections
                            .actionSubjectViewEditFragmentToSubjectURLBottomDialogFragment(
                                currentSubject!!.id,
                                currentSubject!!.url
                            )
                        findNavController().navigate(action)
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
}