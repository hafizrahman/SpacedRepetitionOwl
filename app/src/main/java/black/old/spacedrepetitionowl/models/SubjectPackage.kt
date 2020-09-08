package black.old.spacedrepetitionowl.models

// data class to be used as LiveData, for the purpose of sending this data across
// different fragments by utilizing this ViewModel. This is essentially a singular Subject
// with all the Reminders tied to it.
data class SubjectPackage(
    val subject: Subject,
    val reminder0: Reminder,
    val reminder1: Reminder,
    val reminder2: Reminder,
    val reminder3: Reminder)