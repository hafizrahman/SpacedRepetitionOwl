package black.old.spacedrepetitionowl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_dialog_view_edit_subject_element.view.*

class DialogViewEditSubjectElementFragment : DialogFragment() {

    val args: DialogViewEditSubjectElementFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_dialog_view_edit_subject_element,
            container,
            false
        )
        // Dismiss dialog if user touched outside of the dialog
        // ref: https://stackoverflow.com/a/8761729
        dialog?.setCanceledOnTouchOutside(true)

        // The ViewModel is already created on the Activity level (inside MainActivity.kt),
        // so here we are using the Activity's context
        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        when(args.subjectElementType) {
            1 -> view.sro_viewedit_element_heading.text = getString(R.string.dialog_heading_edit_subject_content)
            2 -> view.sro_viewedit_element_heading.text = getString(R.string.dialog_heading_edit_subject_url)
        }

        // populate the content
        view.sro_viewedit_element_input.setText(args.subjectElementToEdit)

        // Clicklistener for the save button
        view.sro_viewedit_element_submit.setOnClickListener {
            when(args.subjectElementType) {
                1 -> mainViewModel.updateSubjectContent(
                    args.subjectId,
                    view.sro_viewedit_element_input.text.toString()
                )

                2 -> mainViewModel.updateSubjectUrl(
                    args.subjectId,
                    view.sro_viewedit_element_input.text.toString()
                )
            }
            findNavController().popBackStack()
        }

        // Click listener for the cancel button
        view.sro_viewedit_element_cancel.setOnClickListener {
            findNavController().popBackStack()
        }

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
            currentDialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        }

    }
}