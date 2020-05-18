package black.old.spacedrepetitionowl.repositories

import androidx.lifecycle.LiveData
import black.old.spacedrepetitionowl.database.ReminderDao
import black.old.spacedrepetitionowl.database.SubjectDao
import black.old.spacedrepetitionowl.models.Reminder
import black.old.spacedrepetitionowl.models.Subject

class SroRepository(private val subjectDao: SubjectDao, private val reminderDao: ReminderDao) {

    suspend fun insertSubject(subject: Subject) : Long {
        return subjectDao.insert(subject)
    }

    suspend fun updateSubject(subject: Subject) {
        subjectDao.update(subject)
    }

    suspend fun deleteSubjectBySubjectId(subject_id: Long) {
        subjectDao.deleteBySubjectId(subject_id)
    }

    fun getSubjects(): LiveData<List<Subject>>? {
        return subjectDao.getAllSubjects()
    }

    fun getSubject(subject_id: Long):LiveData<Subject> {
        return subjectDao.getSubject(subject_id)
    }

    suspend fun insertReminder(reminder: Reminder): Long {
        return reminderDao.insert(reminder)
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