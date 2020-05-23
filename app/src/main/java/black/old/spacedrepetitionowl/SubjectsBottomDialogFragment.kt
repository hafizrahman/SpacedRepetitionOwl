package black.old.spacedrepetitionowl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_subjects_bottom_dialog.view.*

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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val args: SubjectsBottomDialogFragmentArgs by navArgs()

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
        val view = inflater.inflate(
            R.layout.fragment_subjects_bottom_dialog,
            container,
            false)
        view.subjects_bottom_dialog_title.text = args.subjectText

        val detailSubjectText = view.subjects_bottom_dialog_details
        detailSubjectText.setOnClickListener { view ->
            val action = SubjectsBottomDialogFragmentDirections
                .actionSubjectsBottomDialogFragmentToDetailSubjectFragment(args.subjectId)
            findNavController().navigate(action)
        }

        val editSubjectText = view.subjects_bottom_dialog_edit
        editSubjectText.setOnClickListener { view ->
            val action = SubjectsBottomDialogFragmentDirections
                .actionSubjectsBottomDialogFragmentToEditSubjectFragment(args.subjectId)
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
