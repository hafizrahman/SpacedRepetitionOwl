package black.old.spacedrepetitionowl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_subject_note_edit.view.*

class SubjectNoteEditFragment : Fragment() {
    val args: SubjectNoteEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_subject_note_edit,
            container,
            false)

        view.subject_note_edit.setText(args.subjectNote)

        // The ViewModel is already created on the Activity level (inside MainActivity.kt),
        // so here we are using the Activity's context
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // Handle the back button event
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Build alert dialog
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(getString(R.string.dialog_note_edited_alert_heading))
            builder.setMessage(getString(R.string.dialog_note_edited_alert_message))

            // Positive button
            builder.setPositiveButton(getString(R.string.dialog_note_edited_alert_positive)){ dialog, which ->
                // Do something when user press the positive button
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.dialog_note_edited_alert_positive_confirm),
                    Toast.LENGTH_SHORT).show()
                // Save note changes here, then go back.
                mainViewModel.updateSubjectNotes(args.subjectId, view.subject_note_edit.text.toString() )
                hideKeyboard(activity as MainActivity)
                findNavController().popBackStack()
            }

            // Negative button
            // Nothing to do here, just go back.
            builder.setNegativeButton(getString(R.string.dialog_note_edited_alert_negative)){ dialog, which ->
                // Toast.makeText(requireActivity(),"Not saving changes. Going up...",Toast.LENGTH_SHORT).show()
                hideKeyboard(activity as MainActivity)
                findNavController().popBackStack()
            }

            // Neutral button
            builder.setNeutralButton(getString(R.string.dialog_note_edited_alert_neutral)){ dialog, which ->
                // Toast.makeText(requireActivity(),"Cancel going up...",Toast.LENGTH_SHORT).show()
                // Do nothing here, just dismiss the alert.
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display alert when we detect note changes, otherwise go back right away.
            if(args.subjectNote != view.subject_note_edit.text.toString()) {
                dialog.show()
            }
            else {
                hideKeyboard(activity as MainActivity)
                findNavController().popBackStack()
            }
        }
        callback.isEnabled = true

        return view
    }
}