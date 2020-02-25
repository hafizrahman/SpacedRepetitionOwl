package black.old.spacedrepetitionowl.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

/*
   For version 1.0, each Reminder will have these:
   - id (of course)
   - subjectId          : The Subject that is tied to a particular Reminder. A Subject will have 5.
   - date               : Reminder date
   - checked            : This is to allow user to check a Reminder once they have done the learning
                          for that date
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
    var date: Date,
    var checked: Boolean

)
