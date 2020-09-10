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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_subject_list.*
import kotlinx.android.synthetic.main.fragment_subject_list.view.*
import kotlinx.android.synthetic.main.fragment_subject_list.view.fab
import kotlinx.android.synthetic.main.fragment_subject_timeline.*
import kotlinx.android.synthetic.main.fragment_subject_timeline.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubjectTimelineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubjectTimelineFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var adapter: SubjectTimelineRecyclerViewAdapter
    lateinit var mainViewModel: MainViewModel


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
        val view = inflater.inflate(R.layout.fragment_subject_timeline, container, false)

        adapter = SubjectTimelineRecyclerViewAdapter()
        view.sro_subject_timeline_list.adapter = adapter

        view.sro_subject_timeline_list.layoutManager = LinearLayoutManager(context)

        // The ViewModel is already created on the Activity level (inside MainActivity.kt),
        // so here we are using the Activity's context
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // New observer
        mainViewModel.getAllData()?.observe(viewLifecycleOwner,
            Observer { subjectsAndRemindersPair ->
                adapter.setData(
                    subjectsAndRemindersPair.first,
                    subjectsAndRemindersPair.second)
            })


        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubjectTimelineFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubjectTimelineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}