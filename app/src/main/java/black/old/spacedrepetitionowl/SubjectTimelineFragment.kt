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

/**
 * A simple [Fragment] subclass.
 * Use the [SubjectTimelineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubjectTimelineFragment : Fragment() {
    lateinit var adapter: SubjectTimelineRecyclerViewAdapter
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

}