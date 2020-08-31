package black.old.spacedrepetitionowl

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import black.old.spacedrepetitionowl.models.Subject
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_detail_subject.*
import kotlinx.android.synthetic.main.fragment_subject.*
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass.
 */
class DetailSubjectFragment : Fragment() {

    val args: DetailSubjectFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_subject, container, false)

        // The ViewModel is already created on the Activity level (inside MainActivity.kt),
        // so here we are using the Activity's context
        val mainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)

        // fill in existing data for the subject
        mainViewModel.getSubject(args.subjectId).observe(viewLifecycleOwner,
            Observer { currentSubject ->
                sro_detail_subject_title.text = currentSubject.content
                sro_detail_subject_url.text = currentSubject.url
                sro_detail_starting_date.text = dateStringFormatter(currentSubject.startDateTimestamp)

                if(currentSubject.url.isEmpty()) {
                    sro_detail_subject_url.visibility = View.GONE
                }

                if(currentSubject.notes.isNotEmpty()) {
                    sro_detail_subject_notes.text = currentSubject.notes
                }


            })
        // fill in existing data for the reminders
        mainViewModel.getRemindersBySubjectId(args.subjectId)?.observe(viewLifecycleOwner,
            Observer { listOfReminders ->
                if(listOfReminders.count() == 4) {
                    sro_detail_reminder_button_0.text = dateStringFormatter(listOfReminders[0].dateTimestamp)
                    sro_detail_reminder_button_1.text = dateStringFormatter(listOfReminders[1].dateTimestamp)
                    sro_detail_reminder_button_2.text = dateStringFormatter(listOfReminders[2].dateTimestamp)
                    sro_detail_reminder_button_3.text = dateStringFormatter(listOfReminders[3].dateTimestamp)

                    if(listOfReminders[0].checked) {
                        toggleReminderButtonChecked(sro_detail_reminder_button_0)
                    }
                    if(listOfReminders[1].checked) {
                        toggleReminderButtonChecked(sro_detail_reminder_button_1)
                    }
                    if(listOfReminders[2].checked) {
                        toggleReminderButtonChecked(sro_detail_reminder_button_2)
                    }
                    if(listOfReminders[3].checked) {
                        toggleReminderButtonChecked(sro_detail_reminder_button_3)
                    }

                }
            })
        return view
    }

    private fun dateStringFormatter(timestamp: Long) : String {
        val pattern = "d MMM"
        return SimpleDateFormat(pattern).format(timestamp)
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
