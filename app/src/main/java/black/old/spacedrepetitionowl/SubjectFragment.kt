package black.old.spacedrepetitionowl

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import black.old.spacedrepetitionowl.constants.SORTBY_DEFAULT
import black.old.spacedrepetitionowl.constants.SORTBY_REMINDER
import black.old.spacedrepetitionowl.dummy.DummyContent
import black.old.spacedrepetitionowl.dummy.DummyContent.DummyItem
import black.old.spacedrepetitionowl.models.Reminder
import black.old.spacedrepetitionowl.models.Subject
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_subject_list.*
import kotlinx.android.synthetic.main.fragment_subject_list.view.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [SubjectFragment.OnListFragmentInteractionListener] interface.
 */
class SubjectFragment : Fragment() {

    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null
    lateinit var adapter: SubjectRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_subject_list, container, false)

        adapter = SubjectRecyclerViewAdapter(SORTBY_DEFAULT, listener)
        view.sro_subject_list.adapter = adapter

        // Set up layout manager
        view.sro_subject_list.layoutManager = LinearLayoutManager(context)
        // Send data into adapter then assign adapter to RecyclerView
        //view.sro_subject_list.adapter = MySubjectRecyclerViewAdapter(DummyContent.ITEMS, listener)

        // FAB click listener
        val addSubjectFab: View = view.fab
        addSubjectFab.setOnClickListener { view ->
            /* Snackbar test
            Snackbar.make(view, "Thanks for clicking me.", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
             */
            view.findNavController().navigate(R.id.action_subjectFragment_to_editSubjectFragment)
        }
        // Hide fab during scroll, and show again after scroll is finished.
        // source: https://stackoverflow.com/a/39813266
        view.sro_subject_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) fab.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) fab.show()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        // The ViewModel is already created on the Activity level (inside MainActivity.kt),
        // so here we are using the Activity's context
        var mainViewModel: MainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)

        // New observer
         mainViewModel.getAllData()?.observe(viewLifecycleOwner,
            Observer { subjectsAndRemindersPair ->
                Log.d("SRObert", subjectsAndRemindersPair.toString())
                adapter.setData(
                    subjectsAndRemindersPair.first,
                    subjectsAndRemindersPair.second)
        })

        // menu
        // Example from https://stackoverflow.com/a/50990935
        view.sro_subject_list_toolbar.inflateMenu(R.menu.main_menu)
        view.sro_subject_list_toolbar.setOnMenuItemClickListener { it ->
            onOptionsItemSelected(it)
        }

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var mainViewModel: MainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)
        when(item.itemId) {
            R.id.menu_sortby_default -> {
                adapter.changeOrder(SORTBY_DEFAULT)
                return true
            }
            R.id.menu_sortby_reminder -> {
                adapter.changeOrder(SORTBY_REMINDER)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            SubjectFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
