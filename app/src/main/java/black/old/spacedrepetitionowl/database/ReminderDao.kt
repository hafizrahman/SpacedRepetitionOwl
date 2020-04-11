package black.old.spacedrepetitionowl.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import black.old.spacedrepetitionowl.models.Reminder

@Dao
interface ReminderDao {
    @Insert
    suspend fun insert(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    // No need for coroutines when LiveData is used.
    @Query("SELECT * FROM sro_reminders")
    fun getAllReminders(): LiveData<List<Reminder>>

    // No need for coroutines when LiveData is used.
    @Query("SELECT * FROM sro_reminders WHERE subjectId = :subjectId ORDER BY dateTimestamp ASC")
    fun getRemindersBySubject(subjectId: Int): LiveData<List<Reminder>>

    // Empty out table
    @Query("DELETE FROM sro_reminders")
    suspend fun deleteAll()
}