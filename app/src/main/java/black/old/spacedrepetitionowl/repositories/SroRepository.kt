package black.old.spacedrepetitionowl.repositories

import androidx.lifecycle.LiveData
import black.old.spacedrepetitionowl.database.ReminderDao
import black.old.spacedrepetitionowl.database.SubjectDao
import black.old.spacedrepetitionowl.dateStringFormatter
import black.old.spacedrepetitionowl.models.Reminder
import black.old.spacedrepetitionowl.models.Subject

class SroRepository(private val subjectDao: SubjectDao, private val reminderDao: ReminderDao) {

    suspend fun insertSubject(subject: Subject) : Long {
        return subjectDao.insert(subject)
    }

    suspend fun updateSubject(subject: Subject) {
        return subjectDao.update(subject)
    }

    suspend fun updateSubjectContent(subject_id: Long, content: String) {
        return subjectDao.updateSubjectContent(subject_id, content)
    }

    suspend fun updateSubjectNotes(subject_id: Long, notes: String) {
        return subjectDao.updateSubjectNotes(subject_id, notes)
    }

    suspend fun updateSubjectStartDate(subject_id: Long, timestamp: Long) {
        return subjectDao.updateSubjectStartDate(subject_id, timestamp)
    }

    suspend fun updateSubjectUrl(subject_id: Long, url: String) {
        return subjectDao.updateSubjectUrl(subject_id, url)
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

    suspend fun updateReminder(reminder: Reminder) {
        return reminderDao.update(reminder)
    }

    fun getReminders(): LiveData<List<Reminder>>? {
        return reminderDao.getAllReminders()
    }

    fun getRemindersBySubject(subjectId: Long) : LiveData<List<Reminder>>? {
        return reminderDao.getRemindersBySubject(subjectId)
    }

    suspend fun resetRemindersCheckedStateForSubject(subject_id: Long) {
        return reminderDao.resetRemindersCheckedStateForSubject(subject_id)
    }

    suspend fun updateDateTimestampAndReset(reminder_id: Long, new_timestamp: Long) {
        reminderDao.updateDateTimestampAndReset(reminder_id, new_timestamp)
    }

    suspend fun deleteAllData()  {
        subjectDao.deleteAll()
        reminderDao.deleteAll()
    }

}