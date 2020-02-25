package black.old.spacedrepetitionowl.models

import java.util.*

/*
   For version 1.0, each Subject will have these:
   - id (of course)
   - content            : What the subject is about
   - url (optional)     : Link to a page that has more info about the content
   - start date         : When the first learning is started
 */
class Subject {
    var id: Int? = null
    var content: String? = null
    var url: String? = null
    var startDate: Date? = null
}