package black.old.spacedrepetitionowl.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import black.old.spacedrepetitionowl.models.Subject

@Dao
interface SubjectDao {
    @Insert
    suspend fun insert(subject: Subject)

    @Delete
    suspend fun delete(subject: Subject)

    // Note: Current implementation of Room with coroutines does not support the use of LiveData,
    // so instead of using LiveData<List<Subject>> , I am using List<Subject> instead, and will
    // create the LiveData in the ViewModel instead.
    // See: https://stackoverflow.com/a/56603632
    @Query("SELECT * FROM sro_subjects")
    suspend fun getAllSubjects(): List<Subject>

}