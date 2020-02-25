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


    // Note: Current implementation of Room with coroutines does not support the use of LiveData,
    // so instead of using LiveData<List<Reminder>> , I am using List<Reminder> instead, and will
    // create the LiveData in the ViewModel instead.
    // See: https://stackoverflow.com/a/56603632
    @Query("SELECT * FROM sro_reminders WHERE subjectId = :subjectId ORDER BY dateTimestamp ASC")
    suspend fun getRemindersBySubject(subjectId: Int): List<Reminder>
}