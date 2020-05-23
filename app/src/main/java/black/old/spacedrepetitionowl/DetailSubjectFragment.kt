package black.old.spacedrepetitionowl

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            })
        // fill in existing data for the reminders
        mainViewModel.getRemindersBySubjectId(args.subjectId)?.observe(viewLifecycleOwner,
            Observer { listOfReminders ->
                if(listOfReminders.count() == 4) {
                    sro_detail_subject_reminders_date_1.text = dateStringFormatter(listOfReminders[0].dateTimestamp)
                    sro_detail_subject_reminders_date_2.text = dateStringFormatter(listOfReminders[1].dateTimestamp)
                    sro_detail_subject_reminders_date_3.text = dateStringFormatter(listOfReminders[2].dateTimestamp)
                    sro_detail_subject_reminders_date_4.text = dateStringFormatter(listOfReminders[3].dateTimestamp)
                }
            })
        return view
    }

    private fun dateStringFormatter(timestamp: Long) : String {
        val pattern = "d MMM"
        return SimpleDateFormat(pattern).format(timestamp)
    }

}
