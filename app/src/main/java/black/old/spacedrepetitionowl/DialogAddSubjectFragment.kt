package black.old.spacedrepetitionowl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_dialog_add_subject.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [DialogAddSubjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialogAddSubjectFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dialog_add_subject, container, false)
        // Dismiss dialog if user touched outside of the dialog
        // ref: https://stackoverflow.com/a/8761729
        dialog?.setCanceledOnTouchOutside(true)

        // Data submission
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val submitButton : View = view.add_subject_dialog_submit
        val subjectField = view.add_subject_dialog_subject
        val subjectUrl = view.add_subject_dialog_uri
        val subjectNotes = view.add_subject_dialog_notes
        var customTimestamp = 0L


        submitButton.setOnClickListener { view ->
            mainViewModel.insertSubject(subjectField.text.toString(),
                subjectUrl.text.toString(),
                subjectNotes.text.toString(),
                customTimestamp
            )
            findNavController().popBackStack()
        }

        // Cancel button
        val cancelButton = view.add_subject_dialog_cancel
        cancelButton.setOnClickListener { view ->
            findNavController().popBackStack()
        }

        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        val dateButton = view.add_subject_change_date
        dateButton.setOnClickListener {view ->
            picker.show(activity!!.supportFragmentManager, picker.toString())
        }

        val dateText = view.add_subject_date
        picker.addOnPositiveButtonClickListener {
            dateText.text = it.toString()
            customTimestamp = it
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onStart() {
        super.onStart()

        // Make dialog be full width, and have its height match the content.
        // This is necessary because for some reason, setting it up in the XML does not work.
        // Without this, the dialogFragment shows up really small.
        // ref: https://stackoverflow.com/a/8991860
        val currentDialog = dialog
        if(currentDialog != null) {
            currentDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

    }

    override fun getTheme(): Int {
        return R.style.fullscreen_dialog
    }
}
