package black.old.spacedrepetitionowl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_dialog_subjects_bottom.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubjectsBottomDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
            alert.show(activity!!.supportFragmentManager, "alert_tag" )
        }

        return view
    }
}
