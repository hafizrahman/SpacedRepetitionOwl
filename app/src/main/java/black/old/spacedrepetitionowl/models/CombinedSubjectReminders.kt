package black.old.spacedrepetitionowl.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData


// This class ia a MediatorLiveData created since I have two different sources of data:
// - subjects table
// - reminders table
// Since they then become two separate LiveData, and I need both to fill in the data
// on the main RecyclerView, then we're using a MediatorLiveData to grab both LiveData
// and add them as sources, so that we can notify observers when any of the data gets
// updated.

class CombinedSubjectReminders(
    ldSubject: LiveData<List<Subject>>,
    ldReminder: LiveData<List<Reminder>>
    ) : MediatorLiveData<Pair<List<Subject>, List<Reminder>>>() {

    private var listSubject: List<Subject> = emptyList()
    private var listReminder: List<Reminder> = emptyList()

    init {
        value = Pair(listSubject, listReminder)

        addSource(ldSubject) {
            if( it != null ) listSubject = it
            value = Pair(listSubject, listReminder)
        }

        addSource(ldReminder) {
            if( it != null ) listReminder = it
            value = Pair(listSubject, listReminder)
        }
    }
}