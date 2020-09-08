package black.old.spacedrepetitionowl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import black.old.spacedrepetitionowl.models.Reminder
import black.old.spacedrepetitionowl.models.Subject
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_edit_subject.*
import kotlinx.android.synthetic.main.fragment_edit_subject.view.*
import kotlinx.android.synthetic.main.fragment_edit_subject.view.edit_subject_date
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditSubjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditSubjectFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val args: EditSubjectFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_subject, container, false)

        // The ViewModel is already created on the Activity level (inside MainActivity.kt),
        // so here we are using the Activity's context
        val mainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)

        val submitButton = view.add_subject_submit_button
        val subjectField = view.layoutInputAddSubject
        val subjectUrl = view.layoutInputAddUri
        val subjectNotes = view.layoutInputAddNotes
        val subjectTimestamp = view.edit_subject_date

        var customTimestamp = 0L

        // Fill in existing data

        mainViewModel.selectedSubject.observe(viewLifecycleOwner,
            Observer { selectedSubject ->
                subjectField.setText(selectedSubject.subject.content)
                subjectUrl.setText(selectedSubject.subject.url)
                subjectNotes.setText(selectedSubject.subject.notes)
                subjectTimestamp.setText(selectedSubject.subject.startDateTimestamp.toString())

                submitButton.setOnClickListener { view ->
                    // Update Subject
                    val subjectToUpdate = Subject(
                        subjectField.text.toString(),
                        subjectUrl.text.toString(),
                        subjectNotes.text.toString(),
                        //currentSubject.startDateTimestamp,
                        if( customTimestamp != 0L) customTimestamp else selectedSubject.subject.startDateTimestamp,
                        false,
                        selectedSubject.subject.id
                    )
                    mainViewModel.updateSubject(subjectToUpdate)

                    // If the Subject's start date is changed, update all the reminder dates too.
                    if( customTimestamp != 0L ) {
                        var currentReminder : Reminder
                        val repDays = intArrayOf(1, 7, 16, 35)
                        val reminderIds = longArrayOf(
                            selectedSubject.reminder0.id,
                            selectedSubject.reminder1.id,
                            selectedSubject.reminder2.id,
                            selectedSubject.reminder3.id
                        )
                        for(i in 0..3) {
                            val dateTimestamp = customTimestamp + dayToMilliseconds(repDays[i])
                            currentReminder = Reminder(
                                selectedSubject.subject.id,
                                dateTimestamp,
                                false, // If we're editing dates, reset all checked states
                                reminderIds[i]
                            )
                            mainViewModel.updateReminder(currentReminder)
                        }
                    }
                    // All done, head back to previous fragment.
                    findNavController().popBackStack()
                }
            })

        val nukeDbButton = view.nuke_db_button
        nukeDbButton.setOnClickListener { view ->
            mainViewModel.deleteAllData()
        }

        // Date Picker
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        val dateButton = view.edit_subject_change_date

        dateButton.setOnClickListener {view ->
            picker.show(activity!!.supportFragmentManager, picker.toString())
        }
        picker.addOnPositiveButtonClickListener {
            edit_subject_date.text = it.toString()
            customTimestamp = it
        }


        return view
    }


    fun dayToMilliseconds(day: Int) : Long {
        return TimeUnit.DAYS.toMillis(day.toLong())
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditSubjectFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditSubjectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
