package black.old.spacedrepetitionowl.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/*
   For version 1.0, each Subject will have these:
   - id (of course)
   - content            : What the subject is about
   - url (optional)     : Link to a page that has more info about the content
   - start date         : When the first learning is started
 */
@Entity(tableName="sro_subjects")
data class Subject (
    @PrimaryKey(autoGenerate = true) val id: Int,
    var content: String,
    var url: String,
    var startDate: Date
)
