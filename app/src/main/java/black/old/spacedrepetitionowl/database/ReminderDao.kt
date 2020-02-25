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
    fun insert(reminder: Reminder)

    @Delete
    fun delete(reminder: Reminder)

    @Query("SELECT * FROM sro_reminders WHERE subjectId = :subjectId ORDER BY dateTimestamp ASC")
    fun getRemindersBySubject(subjectId: Int): LiveData<List<Reminder>>
}