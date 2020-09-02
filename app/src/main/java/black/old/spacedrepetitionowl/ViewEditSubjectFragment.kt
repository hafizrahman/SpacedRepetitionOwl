package black.old.spacedrepetitionowl

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_detail_subject.*
import kotlinx.android.synthetic.main.fragment_edit_subject.*
import kotlinx.android.synthetic.main.fragment_subject_view_edit.*
import java.text.SimpleDateFormat

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as MainActivity).title = "My title"// Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_subject_view_edit, container, false)

        // The ViewModel is already created on the Activity level (inside MainActivity.kt),
        // so here we are using the Activity's context
        val mainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)

        // Date Picker
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        // fill in existing data for the subject
        mainViewModel.getSubject(args.subjectId).observe(viewLifecycleOwner,
            Observer { currentSubject ->
                sro_viewedit_subject_title.text = currentSubject.content
                sro_viewedit_subject_title_editable.setText(currentSubject.content)
                sro_viewedit_starting_date.text = dateStringFormatter(currentSubject.startDateTimestamp, true)

                if(currentSubject.url.isNotEmpty()) {
                    sro_viewedit_subject_url.text = currentSubject.url
                }
                sro_viewedit_subject_url.setOnClickListener { view ->
                    val action = SubjectViewEditFragmentDirections
                        .actionSubjectViewEditFragmentToSubjectURLBottomDialogFragment(
                            currentSubject.id,
                            currentSubject.url
                        )
                    findNavController().navigate(action)
                }

                if(currentSubject.notes.isNotEmpty()) {
                    sro_viewedit_subject_notes.text = currentSubject.notes
                }

                sro_viewedit_starting_date.setOnClickListener {view ->
                    picker.show(activity!!.supportFragmentManager, picker.toString())
                }
                picker.addOnPositiveButtonClickListener {
                    sro_viewedit_starting_date.text = dateStringFormatter(it, true)
                }
            })
        // fill in existing data for the reminders
        mainViewModel.getRemindersBySubjectId(args.subjectId)?.observe(viewLifecycleOwner,
            Observer { listOfReminders ->
                if(listOfReminders.count() == 4) {
                    sro_viewedit_reminder_button_0.text = dateStringFormatter(listOfReminders[0].dateTimestamp)
                    sro_viewedit_reminder_button_1.text = dateStringFormatter(listOfReminders[1].dateTimestamp)
                    sro_viewedit_reminder_button_2.text = dateStringFormatter(listOfReminders[2].dateTimestamp)
                    sro_viewedit_reminder_button_3.text = dateStringFormatter(listOfReminders[3].dateTimestamp)

                    if(listOfReminders[0].checked) {
                        toggleReminderButtonChecked(sro_viewedit_reminder_button_0)
                    }
                    if(listOfReminders[1].checked) {
                        toggleReminderButtonChecked(sro_viewedit_reminder_button_1)
                    }
                    if(listOfReminders[2].checked) {
                        toggleReminderButtonChecked(sro_viewedit_reminder_button_2)
                    }
                    if(listOfReminders[3].checked) {
                        toggleReminderButtonChecked(sro_viewedit_reminder_button_3)
                    }

                }
            })

        // Listener, save data when out of focus
        /*
        sro_viewedit_subject_title_editable.setOnFocusChangeListener { view, hasFocus ->
            if(! hasFocus) {
                mainViewModel.getSubject(args.subjectId).observe(viewLifecycleOwner,
                    Observer { currentSubject ->
                        // Do something here to update the Reminder
                    })
            }
        }
        */

        return view
    }
    private fun saveSubjectTitleChange(view: View) {

    }
    private fun dateStringFormatter(timestamp: Long, withYear: Boolean = false) : String {
        return if(withYear) {
            SimpleDateFormat("d MMM, YYYY").format(timestamp)
        } else {
            SimpleDateFormat("d MMM").format(timestamp)
        }
    }

    private fun toggleReminderButtonChecked(button: Button) {
        button.setBackgroundColor((Color.parseColor("#00FF00")))
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
}