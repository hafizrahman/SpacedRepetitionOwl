package black.old.spacedrepetitionowl

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import black.old.spacedrepetitionowl.models.Subject
import black.old.spacedrepetitionowl.models.Reminder
import kotlinx.android.synthetic.main.fragment_subject.view.*
import black.old.spacedrepetitionowl.SubjectFragment.OnListFragmentInteractionListener
import black.old.spacedrepetitionowl.constants.SORTBY_DEFAULT
import black.old.spacedrepetitionowl.models.SubjectPackage
import kotlinx.android.synthetic.main.fragment_subject_test.view.*
import java.text.SimpleDateFormat

class SubjectRecyclerViewAdapter(
    var sortingType: Int,
    val listener: OnListFragmentInteractionListener?,
    val mainClickListener: (SubjectPackage) -> Unit,                // clicklistener for the main card area.
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

    // This method fills in the data to be used by the list. The data will be pulled from elsewhere
    // first, in the SubjectFragment in this case.
    fun setData(subjectsList: List<Subject>, remindersList: List<Reminder>) {
        // We want subjects to be sorted by starting date by default.
        subjects = subjectsList.sortedBy { it.startDateTimestamp }

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
                holder.startingDate.text = dateStringFormatter(currentSubject.startDateTimestamp)

                if(currentSubject.url.isNotEmpty()) {
                    holder.urlView.text = currentSubject.url
                    holder.urlView.visibility = View.VISIBLE
                }

                // Get Reminders related to current Subject ID
                val reminderListForCurrentSubject = getRemindersListBySubject(currentSubject.id)

                // This checker is needed because due to the async nature of the reminder saving,
                // onBindViewHolder already gets called when not all reminders are generated yet.
                // We only want to process more things once all Reminders are created.
                //
                // Check with the Log below to see the constantly updated number of reminderList
                // size while onBindViewHolder is being called.
                // Log.d("onBindViewHolder", "reminderList size " + reminderListForCurrentSubject.size )
                if(reminderListForCurrentSubject.size == 4) {

                    /* Attach button click listeners */
                    holder.reminderBtn0.setOnClickListener {
                        reminderClickListener(reminderListForCurrentSubject[0])
                    }
                    holder.reminderBtn1.setOnClickListener {
                        reminderClickListener(reminderListForCurrentSubject[1])
                    }
                    holder.reminderBtn2.setOnClickListener {
                        reminderClickListener(reminderListForCurrentSubject[2])
                    }
                    holder.reminderBtn3.setOnClickListener {
                        reminderClickListener(reminderListForCurrentSubject[3])
                    }

                    /* Populate and style buttons content */
                    holder.reminderBtn0.text =
                        dateStringFormatter(reminderListForCurrentSubject[0].dateTimestamp)
                    if(reminderListForCurrentSubject[0].checked)
                        toggleReminderButtonChecked(holder.reminderBtn0)
                    else
                        toggleReminderButtonUnchecked(holder.reminderBtn0)

                    holder.reminderBtn1.text =
                        dateStringFormatter(reminderListForCurrentSubject[1].dateTimestamp)
                    if(reminderListForCurrentSubject[1].checked)
                        toggleReminderButtonChecked(holder.reminderBtn1)
                    else
                        toggleReminderButtonUnchecked(holder.reminderBtn1)

                    holder.reminderBtn2.text =
                        dateStringFormatter(reminderListForCurrentSubject[2].dateTimestamp)
                    if(reminderListForCurrentSubject[2].checked)
                        toggleReminderButtonChecked(holder.reminderBtn2)
                    else
                        toggleReminderButtonUnchecked(holder.reminderBtn2)

                    holder.reminderBtn3.text =
                        dateStringFormatter(reminderListForCurrentSubject[3].dateTimestamp)
                    if(reminderListForCurrentSubject[3].checked)
                        toggleReminderButtonChecked(holder.reminderBtn3)
                    else
                        toggleReminderButtonUnchecked(holder.reminderBtn3)

                    // Generate a SubjectPackage object to be used by mainClickListener
                    val currentSubjectPackage = SubjectPackage(
                        currentSubject,
                        reminderListForCurrentSubject[0],
                        reminderListForCurrentSubject[1],
                        reminderListForCurrentSubject[2],
                        reminderListForCurrentSubject[3]
                    )
                    // Next, bind mainClickListener to the LinearLayout to make it clickable.
                    holder.mainBar.setOnClickListener { mainClickListener(currentSubjectPackage) }
                }
            }
        }
        else if(holder is ViewHolderTest) {
            val currentReminder = remindersOrderedByDate[position]
            holder.contentTestView.text = dateStringFormatter(currentReminder.dateTimestamp) + " -- " + currentReminder.subjectId.toString()
        }
    }

    inner class ViewHolderReal(val view: View) : RecyclerView.ViewHolder(view) {
        val contentView: TextView = view.content
        val startingDate: TextView = view.subject_card_starting_date
        val reminderBtn0: Button = view.subject_card_reminder_button_0
        val reminderBtn1: Button = view.subject_card_reminder_button_1
        val reminderBtn2: Button = view.subject_card_reminder_button_2
        val reminderBtn3: Button = view.subject_card_reminder_button_3
        val urlView: TextView = view.subject_card_url
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

    private fun toggleReminderButtonChecked(button: Button) {
        button.setBackgroundColor((Color.parseColor("#00FF00")))
        button.setBackgroundColor(
            ContextCompat.getColor(button.context, R.color.colorPrimary ))
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            0,
            R.drawable.ic_baseline_check_24
        )
        button.setTextColor(
            ContextCompat.getColor(button.context, R.color.colorWhite ))
    }

    private fun toggleReminderButtonUnchecked(button: Button) {
        button.setBackgroundColor(
            ContextCompat.getColor(button.context, R.color.colorWhite ))
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            0,
            R.drawable.ic_baseline_clear_24
        )
        button.setTextColor(
            ContextCompat.getColor(button.context, R.color.colorDarkGray ))
    }
}