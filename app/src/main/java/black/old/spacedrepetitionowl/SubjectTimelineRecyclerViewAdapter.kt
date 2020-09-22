package black.old.spacedrepetitionowl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import black.old.spacedrepetitionowl.constants.SORTBY_DEFAULT
import black.old.spacedrepetitionowl.models.Reminder
import black.old.spacedrepetitionowl.models.Subject
import black.old.spacedrepetitionowl.models.SubjectPackage
import kotlinx.android.synthetic.main.fragment_subject_timeline_item.view.*

import java.text.SimpleDateFormat

class SubjectTimelineRecyclerViewAdapter(
    val mainClickListener: (SubjectPackage) -> Unit
) : RecyclerView.Adapter<SubjectTimelineRecyclerViewAdapter.ViewHolderTimelineItem>() {

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
        val repetitionNumberTextView: TextView = view.timeline_item_repetition_number
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
            // Generate a SubjectPackage object to be used by mainClickListener
            val reminderListForCurrentSubject = getRemindersListBySubject(currentSubject.id)
            val currentSubjectPackage = SubjectPackage(
                currentSubject,
                reminderListForCurrentSubject[0],
                reminderListForCurrentSubject[1],
                reminderListForCurrentSubject[2],
                reminderListForCurrentSubject[3]
            )
            // Bind mainClickListener to the card to make it clickable.
            holder.card.setOnClickListener { mainClickListener(currentSubjectPackage) }

            var repetitionText = "Repetition "
            when(currentReminder.id) {
                reminderListForCurrentSubject[0].id -> repetitionText += "1"
                reminderListForCurrentSubject[1].id -> repetitionText += "2"
                reminderListForCurrentSubject[2].id -> repetitionText += "3"
                reminderListForCurrentSubject[3].id -> repetitionText += "4"
            }
            holder.repetitionNumberTextView.text = repetitionText


        }
    }

    private fun getRemindersListBySubject(subjectId: Long) : List<Reminder> {
        // Only get Reminders from the list that has the current subject ID
        var currentReminders = reminders.filter {
            it.subjectId == subjectId
        }
        return currentReminders.sortedBy { it.dateTimestamp } // Sort list based on timestamp, oldest first
    }
}