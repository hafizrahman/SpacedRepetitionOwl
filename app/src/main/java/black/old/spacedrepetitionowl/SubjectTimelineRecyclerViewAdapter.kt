package black.old.spacedrepetitionowl

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import black.old.spacedrepetitionowl.constants.SORTBY_DEFAULT
import black.old.spacedrepetitionowl.models.Reminder
import black.old.spacedrepetitionowl.models.Subject
import kotlinx.android.synthetic.main.fragment_subject_timeline_item.view.*

import java.text.SimpleDateFormat

class SubjectTimelineRecyclerViewAdapter() : RecyclerView.Adapter<SubjectTimelineRecyclerViewAdapter.ViewHolderTimelineItem>() {

    private lateinit var subjects: List<Subject>
    private lateinit var reminders: List<Reminder>
    private lateinit var remindersOrderedByDate: List<Reminder>

    fun setData(subjectsList: List<Subject>, remindersList: List<Reminder>) {
        subjects = subjectsList
        reminders = remindersList
        remindersOrderedByDate = orderRemindersByUpcomingDate()
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

    inner class ViewHolderTimelineItem(val view: View) : RecyclerView.ViewHolder(view) {
        val contentTimelineView: TextView = view.timeline_item_content
        val ddmmTextView: TextView = view.timeline_item_date_mmdd
        val yyyyTextView: TextView = view.timeline_item_date_yyyy
        // TODO attach click listener here to open the card details info
        val card: CardView = view.timeline_card
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.fragment_subject_timeline_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectTimelineRecyclerViewAdapter.ViewHolderTimelineItem {
        return ViewHolderTimelineItem(LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_subject_timeline_item, parent, false))
    }

    override fun getItemCount(): Int {
        return remindersOrderedByDate.size
    }

    override fun onBindViewHolder(holder: SubjectTimelineRecyclerViewAdapter.ViewHolderTimelineItem, position: Int) {
        val currentReminder = remindersOrderedByDate[position]
        val currentSubject = subjects.find { subject ->
            subject.id.equals(currentReminder.subjectId)
        }

        if ( currentSubject != null ) {
            holder.contentTimelineView.text = currentSubject.content
            holder.ddmmTextView.text = formatTimestamp(currentReminder.dateTimestamp, "dd MMM")
            holder.yyyyTextView.text = formatTimestamp(currentReminder.dateTimestamp, "YYYY")
        }



    }


    private fun dateStringFormatter(timestamp: Long) : String {
        val pattern = "d MMM"
        return SimpleDateFormat(pattern).format(timestamp)
    }
}