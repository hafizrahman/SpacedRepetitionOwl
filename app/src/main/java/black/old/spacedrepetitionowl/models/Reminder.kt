package black.old.spacedrepetitionowl.models

import java.util.*

/*
   For version 1.0, each Reminder will have these:
   - id (of course)
   - subjectId          : The Subject that is tied to a particular Reminder. A Subject will have 5.
   - date               : Reminder date
   - checked            : This is to allow user to check a Reminder once they have done the learning
                          for that date
 */
class Reminder {
    var id: Int? = null
    var subjectId: Int? = null
    var date: Date? = null
    var checked: Boolean? = null
}