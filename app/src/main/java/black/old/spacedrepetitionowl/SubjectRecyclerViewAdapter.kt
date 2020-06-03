package black.old.spacedrepetitionowl

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import black.old.spacedrepetitionowl.models.Subject
import black.old.spacedrepetitionowl.models.Reminder
import kotlinx.android.synthetic.main.fragment_subject.view.*
import black.old.spacedrepetitionowl.SubjectFragment.OnListFragmentInteractionListener
import black.old.spacedrepetitionowl.constants.SORTBY_DEFAULT
import kotlinx.android.synthetic.main.fragment_subject_test.view.*
import java.text.SimpleDateFormat

class SubjectRecyclerViewAdapter(
    var sortingType: Int,
    val listener: OnListFragmentInteractionListener?,
    val mainClickListener: (Subject) -> Unit,                // clicklistener for the main card area.
    val reminderClickListener: (Reminder) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val onClickListener: View.OnClickListener
    private lateinit var subjects: List<Subject>
    private lateinit var reminders: List<Reminder>
    private lateinit var remindersOrderedByDate: List<Reminder>

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Subject
            // TODO: This still uses the DummyItem, replace it with actual stuff
            //listener?.onListFragmentInteraction(item)
        }
    }

    fun setData(subjectsList: List<Subject>, remindersList: List<Reminder>) {
        subjects = subjectsList
        reminders = remindersList
        remindersOrderedByDate = orderRemindersByUpcomingDate()
        Log.d("SRIRI A", subjects.toString())
        Log.d("SRIRI B", reminders.toString())
        Log.d("SRIRI C", remindersOrderedByDate.toString())
        notifyDataSetChanged()
    }

    // Here we want a list of Reminders ordered by earliest timestamp, but only
    // those with timestamps in the future, since this is for displaying upcoming reminders.
    private fun orderRemindersByUpcomingDate() : List<Reminder> {
        val nowTimestamp = System.currentTimeMillis()
        return reminders
            .sortedBy { it.dateTimestamp }      // 1. Sort `reminder` by its timestamp
            .filter { it ->                     // 2. Remove elements with past `dateTimestamp`
               it.dateTimestamp >= nowTimestamp
           }
    }

    inner class ViewHolderTest(val view: View) : RecyclerView.ViewHolder(view) {
        val contentTestView: TextView = view.content_test
    }

    // Necessary for having multiple ViewHolders in one RecyclerView
    // Here we're returning the id for the layout to be used, which is guaranteed to be Int
    override fun getItemViewType(position: Int): Int {
        if (sortingType == SORTBY_DEFAULT)
            return R.layout.fragment_subject
        else
            return R.layout.fragment_subject_test
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.layout.fragment_subject) {
            return ViewHolderReal(LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_subject, parent, false))
        }
        else {
            return ViewHolderTest(LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_subject_test, parent, false))

        }

        /*
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_subject, parent, false)
        return ViewHolder(view)
         */
    }

    override fun getItemCount(): Int {
        if(sortingType == SORTBY_DEFAULT)
            return subjects.size
        else
            return remindersOrderedByDate.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is ViewHolderReal) {
            val currentSubject = subjects[position]

            if(currentSubject != null) {
                holder.contentView.text = currentSubject.content

                // Get Reminders related to current Subject ID
                val reminderListForCurrentSubject = getRemindersListBySubject(currentSubject.id)

                if(reminderListForCurrentSubject.size == 4) {

                    holder.reminder0.text =
                        dateStringFormatter(reminderListForCurrentSubject[0].dateTimestamp)
                    holder.reminder0.setOnClickListener {
                        reminderClickListener(reminderListForCurrentSubject[0])
                    }

                    if(reminderListForCurrentSubject[0].checked)
                        holder.reminder0.setBackgroundColor((Color.parseColor("#00FF00")))
                    else
                        holder.reminder0.setBackgroundColor((Color.parseColor("#FFFFFF")))

                    holder.reminder1.text =
                        dateStringFormatter(reminderListForCurrentSubject[1].dateTimestamp)
                    holder.reminder1.setOnClickListener {
                        reminderClickListener(reminderListForCurrentSubject[1])
                    }

                    if(reminderListForCurrentSubject[1].checked)
                        holder.reminder1.setBackgroundColor((Color.parseColor("#00FF00")))
                    else
                        holder.reminder1.setBackgroundColor((Color.parseColor("#FFFFFF")))

                    holder.reminder2.text =
                        dateStringFormatter(reminderListForCurrentSubject[2].dateTimestamp)
                    holder.reminder2.setOnClickListener {
                        reminderClickListener(reminderListForCurrentSubject[2])
                    }
                    if(reminderListForCurrentSubject[2].checked)
                        holder.reminder2.setBackgroundColor((Color.parseColor("#00FF00")))
                    else
                        holder.reminder2.setBackgroundColor((Color.parseColor("#FFFFFF")))

                    holder.reminder3.text =
                        dateStringFormatter(reminderListForCurrentSubject[3].dateTimestamp)
                    holder.reminder3.setOnClickListener {
                        reminderClickListener(reminderListForCurrentSubject[3])
                    }

                    if(reminderListForCurrentSubject[3].checked)
                        holder.reminder3.setBackgroundColor((Color.parseColor("#00FF00")))
                    else
                        holder.reminder3.setBackgroundColor((Color.parseColor("#FFFFFF")))

                }

                // bind mainClickListener to the LinearLayout to make it clickable
                holder.mainBar.setOnClickListener { mainClickListener(currentSubject) }
            }
        }
        else if(holder is ViewHolderTest) {
            val currentReminder = remindersOrderedByDate[position]
            holder.contentTestView.text = dateStringFormatter(currentReminder.dateTimestamp) + " -- " + currentReminder.subjectId.toString()
        }

/*


        val currentSubject = subjects[position]
        // TODO: Fill in Reminders data
        holder.contentView.text = currentSubject.content

        // Get Reminders related to current Subject ID
        val reminderListForCurrentSubject = getRemindersListBySubject(currentSubject.id)
        Log.d("SROacnh ->", reminderListForCurrentSubject.elementAt(0).toString())

        holder.reminder_0.text =  dateStringFormatter(reminderListForCurrentSubject[0].dateTimestamp)
        holder.reminder_1.text =  dateStringFormatter(reminderListForCurrentSubject[1].dateTimestamp)
        holder.reminder_2.text =  dateStringFormatter(reminderListForCurrentSubject[2].dateTimestamp)
        holder.reminder_3.text =  dateStringFormatter(reminderListForCurrentSubject[3].dateTimestamp)

        with(holder.view) {
            // TODO: Set click listener here?

        }

 */
    }

    inner class ViewHolderReal(val view: View) : RecyclerView.ViewHolder(view) {
        val contentView: TextView = view.content
        val reminder0: TextView = view.reminder_0
        val reminder1: TextView = view.reminder_1
        val reminder2: TextView = view.reminder_2
        val reminder3: TextView = view.reminder_3
        val mainBar: LinearLayout = view.main_subject_bar
    }

    public fun changeOrder(ordertype: Int) {
        sortingType = ordertype
        notifyDataSetChanged()
    }

    private fun getRemindersListBySubject(subjectId: Long) : List<Reminder> {
        // Only get Reminders from the list that has the current subject ID
        var currentReminders = reminders.filter {
            it.subjectId == subjectId
        }
        return currentReminders.sortedBy { it.dateTimestamp } // Sort list based on timestamp, oldest first
    }

    private fun dateStringFormatter(timestamp: Long) : String {
        val pattern = "d MMM"
        return SimpleDateFormat(pattern).format(timestamp)
    }
}