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
import kotlinx.android.synthetic.main.fragment_edit_subject.view.*

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

        // fill in existing data
        mainViewModel.getSubject(args.subjectId).observe(viewLifecycleOwner,
            Observer { currentSubject ->
                subjectField.setText(currentSubject.content)
                subjectUrl.setText(currentSubject.url)

                submitButton.setOnClickListener { view ->
                    val subjectToUpdate = Subject(
                        subjectField.text.toString(),
                        subjectUrl.text.toString(),
                        currentSubject.startDateTimestamp,
                        currentSubject.id
                        )
                    Log.d("HIKARU", "Updating these: $subjectToUpdate")
                    mainViewModel.updateSubject(subjectToUpdate)
                    findNavController().popBackStack()
                }
            })


        val nukeDbButton = view.nuke_db_button
        nukeDbButton.setOnClickListener { view ->
            mainViewModel.deleteAllData()
        }

        return view
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
