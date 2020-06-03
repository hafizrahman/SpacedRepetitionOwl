package black.old.spacedrepetitionowl

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
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
import kotlinx.android.synthetic.main.fragment_subject.view.*
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
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_subject_list, container, false)

        adapter = SubjectRecyclerViewAdapter(
            SORTBY_DEFAULT,
            listener,
            { subject : Subject -> mainSubjectBarClicked(subject) },
            { reminder : Reminder -> reminderIsClicked(reminder) }
        )
        view.sro_subject_list.adapter = adapter

        // Set up layout manager
        view.sro_subject_list.layoutManager = LinearLayoutManager(context)

        // FAB click listener
        val addSubjectFab: View = view.fab
        addSubjectFab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_subjectFragment_to_editSubjectDialogFragment)
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
        mainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)

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

        /*
        view.sro_subject_list_toolbar.inflateMenu(R.menu.main_menu)
        view.sro_subject_list_toolbar.setOnMenuItemClickListener { it ->
            onOptionsItemSelected(it)
        }
         */
        return view
    }
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var mainViewModel: MainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)
        when(item.itemId) {
            R.id.menu_sortby_default -> {
               // displaySampleNotification()
                setNotification15sFromNow()
                Log.d("FIFTEEN", "Setting notification alarm for 15 secs from now")
                adapter.changeOrder(SORTBY_DEFAULT)
                return true
            }
            R.id.menu_sortby_reminder -> {
                displaySampleNotification()
                adapter.changeOrder(SORTBY_REMINDER)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
*/
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

    fun displaySampleNotification() {
        // Display example notification
        NotificationHelper.createSampleDataNotification(
            activity!!.applicationContext,
            "It's time to learn!",
            "How to make coffee",
            "How to make coffee -- 2nd Phase",
            true)
    }

    fun setNotification15sFromNow() {
        val fifteenSecsFromNow = System.currentTimeMillis() + (5*1000)
        Log.d("FIFTEEN", System.currentTimeMillis().toString())
        Log.d("FIFTEEN", fifteenSecsFromNow.toString())
        val alarmManager = activity!!.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        AlarmScheduler.scheduleAlarm(
            activity!!.applicationContext,
            fifteenSecsFromNow,
            AlarmScheduler.createPendingIntent(activity!!.applicationContext),
            alarmManager
        )
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

    // The actual action that needs to be done when a main subject bar is clicked.
    private fun mainSubjectBarClicked(currentSubject : Subject) {
        Log.d("CLICKER", currentSubject.toString() + " is being clicked")
        val subject_id = currentSubject.id
        val subject_text = currentSubject.content
        val action = SubjectFragmentDirections.actionSubjectFragmentToSubjectsBottomDialogFragment(subject_id, subject_text)
       findNavController().navigate(action)
    }

    private fun reminderIsClicked(currentReminder: Reminder) {
        Log.d("Clicker", currentReminder.toString() + " is being clicked")
        // TODO:
        // Toggle Reminder's checked status and save to db
        if (currentReminder.checked == true) {
            currentReminder.checked = false
        }
        else {
            currentReminder.checked = true
        }
        mainViewModel.updateReminder(currentReminder)
        Log.d("Clicker", "Update result " + currentReminder.toString())
        adapter.notifyDataSetChanged()
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
