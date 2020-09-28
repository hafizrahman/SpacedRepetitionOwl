package black.old.spacedrepetitionowl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_dialog_subjects_bottom.view.*

class SubjectsBottomDialogFragment() : BottomSheetDialogFragment() {
    val args: SubjectsBottomDialogFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_dialog_subjects_bottom,
            container,
            false)
        view.subjects_bottom_dialog_title.text = args.subjectText


        val editSubjectText = view.subjects_bottom_dialog_edit
        editSubjectText.setOnClickListener { view ->
            val action = SubjectsBottomDialogFragmentDirections
                .actionSubjectsBottomDialogFragmentToViewEditSubjectFragment(args.subjectId, args.subjectText)
            findNavController().navigate(action)
        }

        val deleteSubjectText = view.subjects_bottom_dialog_delete
        deleteSubjectText.setOnClickListener { view ->
            val alert = DialogDeleteAlertFragment(args.subjectId)
            alert.show(
                requireActivity().supportFragmentManager,
                getString(R.string.key_tag_delete_dialog_alert) )
        }

        return view
    }
}
