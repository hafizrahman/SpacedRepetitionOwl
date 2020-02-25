package black.old.spacedrepetitionowl.repositories

import android.app.Application
import black.old.spacedrepetitionowl.database.SroDatabase
import black.old.spacedrepetitionowl.models.Reminder
import black.old.spacedrepetitionowl.models.Subject

class SroRepository(application: Application) {
    var sroDatabase = SroDatabase.getDatabase(application.applicationContext)
    var subjectDao = sroDatabase?.subjectDao
    var reminderDao = sroDatabase?.reminderDao

    suspend fun insertSubject(subject: Subject) {
        subjectDao?.insert(subject)
    }

    suspend fun getSubjects(): List<Subject>? {
        return subjectDao?.getAllSubjects()
    }

    suspend fun insertReminder(reminder: Reminder) {
        reminderDao?.insert(reminder)
    }

    suspend fun getReminderBySubject(subjectId: Int) : List<Reminder>? {
        return reminderDao?.getRemindersBySubject(subjectId)
    }
}