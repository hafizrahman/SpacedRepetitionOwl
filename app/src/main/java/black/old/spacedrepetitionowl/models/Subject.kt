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
    @PrimaryKey val id: Int,
    var content: String,
    var url: String,
    var startDateTimestamp: Long
)
