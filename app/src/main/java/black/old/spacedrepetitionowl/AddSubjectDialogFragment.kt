package black.old.spacedrepetitionowl

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_edit_subject.view.*
import kotlinx.android.synthetic.main.fragment_add_subject_dialog.view.*
import kotlinx.android.synthetic.main.main_content_area.*

/**
 * A simple [Fragment] subclass.
 * Use the [AddSubjectDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddSubjectDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_subject_dialog, container, false)
        // Dismiss dialog if user touched outside of the dialog
        // ref: https://stackoverflow.com/a/8761729
        dialog?.setCanceledOnTouchOutside(true)

        // Data submission
        val mainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)
        val submitButton : View = view.add_subject_dialog_submit
        val subjectField = view.add_subject_dialog_subject
        val subjectUrl = view.add_subject_dialog_uri
        val subjectNotes = view.add_subject_dialog_notes
        var customTimestamp = 0L


        submitButton.setOnClickListener { view ->
            Log.d("DIALOGSUSI", subjectField.text.toString() + " " + subjectUrl.text.toString())
            Log.d("picker: ", customTimestamp.toString())
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddSubjectDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddSubjectDialogFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
