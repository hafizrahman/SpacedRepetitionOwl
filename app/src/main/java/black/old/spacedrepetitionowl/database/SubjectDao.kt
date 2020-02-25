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
    fun insert(subject: Subject)

    @Delete
    fun delete(subject: Subject)

    @Query("SELECT * FROM sro_subjects")
    fun getAllSubjects(): LiveData<List<Subject>>

}