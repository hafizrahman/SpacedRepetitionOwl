package black.old.spacedrepetitionowl.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
   For version 1.0, each Subject will have these:
   - id (of course)
   - content            : What the subject is about
   - url (optional)     : Link to a page that has more info about the content
   - start date         : When the first learning is started

   ---
   Notes on dates: When it comes to the date data, and its saving/passing, the timestamp will always
   be on UTC timezone. The app will figure out later on the UI on how to convert this timestamp to a
   proper date in the current user's timezone.
 */
@Entity(tableName="sro_subjects")
data class Subject (
    var content: String,
    var url: String,
    var notes: String,
    var startDateTimestamp: Long,
    var archived: Boolean = false,
    // The field below is used for primary key and automatically generated, so I'm giving it
    // a default value of 0 so that the object creation can ignore this parameter.
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)
