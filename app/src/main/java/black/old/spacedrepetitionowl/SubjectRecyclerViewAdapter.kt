package black.old.spacedrepetitionowl

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import black.old.spacedrepetitionowl.models.Subject
import black.old.spacedrepetitionowl.models.Reminder
import kotlinx.android.synthetic.main.fragment_subject.view.*
import black.old.spacedrepetitionowl.SubjectFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_subject_test.view.*
import java.text.SimpleDateFormat

class SubjectRecyclerViewAdapter(
    val subjects: List<Subject>,
    val reminders: List<Reminder>,
    val listener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Subject
            // TODO: This still uses the DummyItem, replace it with actual stuff
            //listener?.onListFragmentInteraction(item)
        }
    }

    inner class ViewHolderReal(val view: View) : RecyclerView.ViewHolder(view) {
        val contentView: TextView = view.content
        val reminder_0: TextView = view.reminder_0
        val reminder_1: TextView = view.reminder_1
        val reminder_2: TextView = view.reminder_2
        val reminder_3: TextView = view.reminder_3

    }

    inner class ViewHolderTest(val view: View) : RecyclerView.ViewHolder(view) {
        val contentTestView: TextView = view.content_test

    }

    // Necessary for having multiple ViewHolders in one RecyclerView
    // Here we're returning the id for the layout to be used, which is guaranteed to be Int
    override fun getItemViewType(position: Int): Int {
        if(position % 2 == 0) {
            return R.layout.fragment_subject
        }
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

    override fun getItemCount(): Int = subjects.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is ViewHolderReal) {
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
        }
        else if(holder is ViewHolderTest) {
            val currentSubject = subjects[position]
            holder.contentTestView.text = currentSubject.content

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

    private fun getRemindersListBySubject(subjectId: Int) : List<Reminder> {
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