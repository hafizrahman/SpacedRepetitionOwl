package black.old.spacedrepetitionowl

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_dialog_subject_url_bottom.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [SubjectNoteEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubjectNoteEditFragment : Fragment() {
    val args: SubjectNoteEditFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        val mainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)

        // Handle the back button event
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Build alert dialog
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Note edited")
            builder.setMessage("Save note changes?")

            // Positive button
            builder.setPositiveButton("YES"){ dialog, which ->
                // Do something when user press the positive button
                Toast.makeText(requireActivity(),"OK, saving notes here. Going up...",Toast.LENGTH_SHORT).show()
                // Save note changes here, then go back.
                mainViewModel.updateSubjectNotes(args.subjectId, view.subject_note_edit.text.toString() )
                hideKeyboard(activity as MainActivity)
                findNavController().popBackStack()
            }

            // Negative button
            // Nothing to do here, just go back.
            builder.setNegativeButton("No"){ dialog, which ->
                // Toast.makeText(requireActivity(),"Not saving changes. Going up...",Toast.LENGTH_SHORT).show()
                hideKeyboard(activity as MainActivity)
                findNavController().popBackStack()
            }

            // Neutral button
            builder.setNeutralButton("Cancel"){ dialog, which ->
                // Toast.makeText(requireActivity(),"Cancel going up...",Toast.LENGTH_SHORT).show()
                // Do nothing here, just dismiss the alert.
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()


            Log.d("hafiz", args.subjectNote)
            Log.d("hafiz2", view.subject_note_edit.text.toString())

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