package black.old.spacedrepetitionowl

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_add_subject.view.*
import kotlinx.android.synthetic.main.fragment_subject.view.*

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
        val view = inflater.inflate(R.layout.fragment_add_subject, container, false)

        val mainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)

        val submitButton : View = view.add_subject_submit_button
        val subjectField = view.layoutInputAddSubject
        val subjectUrl = view.layoutInputAddUri
        submitButton.setOnClickListener { view ->
            Log.d("SRISUSI", subjectField.text.toString() + " " + subjectUrl.text.toString())
            mainViewModel.insertSubject(subjectField.text.toString(), subjectUrl.text.toString())
        }

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
