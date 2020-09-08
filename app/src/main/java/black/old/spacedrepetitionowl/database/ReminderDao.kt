package black.old.spacedrepetitionowl.database

import androidx.lifecycle.LiveData
import androidx.room.*
import black.old.spacedrepetitionowl.models.Reminder

@Dao
interface ReminderDao {
    // The `long` return value for insert is used to get the autogenerated primary key id
    // of the Reminder that's being entered.
    // https://developer.android.com/training/data-storage/room/accessing-data#convenience-insert

    @Insert
    suspend fun insert(reminder: Reminder): Long

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    // No need for coroutines when LiveData is used.
    @Query("SELECT * FROM sro_reminders")
    fun getAllReminders(): LiveData<List<Reminder>>

    // No need for coroutines when LiveData is used.
    @Query("SELECT * FROM sro_reminders WHERE subjectId = :subjectId ORDER BY dateTimestamp ASC")
    fun getRemindersBySubject(subjectId: Long): LiveData<List<Reminder>>

    // Reset all checked state back to false. Used when user is changing starting date.
    // See ViewEditSubjectFragment.kt for more details.
    @Query("UPDATE sro_reminders SET checked = 0 where subjectId == :subject_id")
    suspend fun resetRemindersCheckedStateForSubject(subject_id: Long)


    // Empty out table
    @Query("DELETE FROM sro_reminders")
    suspend fun deleteAll()
}