package black.old.spacedrepetitionowl.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/*
   For version 1.0, each Reminder will have these:
   - id (of course)
   - subjectId          : The Subject that is tied to a particular Reminder. A Subject will have 5.
   - date               : Reminder date
   - checked            : This is to allow user to check a Reminder once they have done the learning
                          for that date

   ---
   Notes on dates: When it comes to the date data, and its saving/passing, the timestamp will always
   be on UTC timezone. The app will figure out later on the UI on how to convert this timestamp to a
   proper date in the current user's timezone.
 */
@Entity(
    tableName="sro_reminders",
    foreignKeys = [ForeignKey(
        entity          = Subject::class,
        parentColumns   = arrayOf("id"),
        childColumns    = arrayOf("subjectId"),
        onDelete        = ForeignKey.CASCADE
        )]
    )
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var subjectId: Int,
    var dateTimestamp: Long,
    var checked: Boolean = false
)
