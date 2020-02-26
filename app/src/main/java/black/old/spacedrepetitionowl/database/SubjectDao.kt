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

    // No need for coroutines when LiveData is used.
    @Query("SELECT * FROM sro_subjects")
    fun getAllSubjects(): LiveData<List<Subject>>

    // Empty out table
    @Query("DELETE FROM sro_subjects")
    suspend fun deleteAll()

}