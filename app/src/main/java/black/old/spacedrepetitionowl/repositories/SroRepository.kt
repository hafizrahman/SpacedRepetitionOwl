package black.old.spacedrepetitionowl.repositories

import androidx.lifecycle.LiveData
import black.old.spacedrepetitionowl.database.ReminderDao
import black.old.spacedrepetitionowl.database.SubjectDao
import black.old.spacedrepetitionowl.models.Reminder
import black.old.spacedrepetitionowl.models.Subject

class SroRepository(private val subjectDao: SubjectDao, private val reminderDao: ReminderDao) {

    suspend fun insertSubject(subject: Subject) {
        subjectDao.insert(subject)
    }

    fun getSubjects(): LiveData<List<Subject>>? {
        return subjectDao.getAllSubjects()
    }

    suspend fun insertReminder(reminder: Reminder) {
        reminderDao.insert(reminder)
    }

    fun getReminders(): LiveData<List<Reminder>>? {
        return reminderDao.getAllReminders()
    }

    fun getReminderBySubject(subjectId: Int) : LiveData<List<Reminder>>? {
        return reminderDao.getRemindersBySubject(subjectId)
    }

    suspend fun deleteAllData()  {
        subjectDao.deleteAll()
        reminderDao.deleteAll()
    }

}