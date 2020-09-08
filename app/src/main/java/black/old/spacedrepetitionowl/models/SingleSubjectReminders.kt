package black.old.spacedrepetitionowl.models

// This is a sealed class used for creating MutableLiveData that contains multiple type
// of data sources (in this case, (1) the Room query for single Subject and (2) the Room query
// for the Reminders tied to that single Subject.
// This method is outlined further on:
// https://code.luasoftware.com/tutorials/android/use-mediatorlivedata-to-query-and-merge-different-data-type/

sealed class SingleSubjectReminders
data class singleSubject(val subject: Subject) : SingleSubjectReminders()
data class singleReminders(val reminders: List<Reminder>) : SingleSubjectReminders()