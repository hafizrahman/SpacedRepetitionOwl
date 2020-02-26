package black.old.spacedrepetitionowl.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import black.old.spacedrepetitionowl.models.Reminder
import black.old.spacedrepetitionowl.models.Subject
import black.old.spacedrepetitionowl.repositories.SroRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val sroRepository = SroRepository(application)

    // This is a dummy function called when the Add subject button is clicked.
    // Ignore everything and enter some dummy values into the database.
    fun dummyInsertSubject() {
        Log.d("SRO", "Entering but not for real")

        val dateNow = Date()
        val simpleDateFormat = SimpleDateFormat("dHHmmsss")

        // We use the current time's "ddHHmmss" as the dummy Subject's ID
        // to make sure they're unique.
        val dummySubjectId = simpleDateFormat.format(dateNow).toInt()
        val nowTimestamp = System.currentTimeMillis()

        // Generate one dummy Subject
        val dummySubject = Subject(
            dummySubjectId,
            "Subject #$dummySubjectId",
            "https://wordpress.com/",
            nowTimestamp
        )

        Log.d("SRO", "Content of Subject is $dummySubject")
        Log.d("SRO", "Hoot! Saving Subject to database.")

        // OKAY, actually entering data now.
        insertSubject(dummySubject)

        // Generate five dummy Reminders based on the Subject
        var currentReminder : Reminder
        for(i in 1..4) {
            // Wait for 1 second to make sure the reminder IDs are unique
            TimeUnit.SECONDS.sleep(1L)
            currentReminder = Reminder(
                simpleDateFormat.format(Date()).toInt(),
                dummySubjectId,
                System.currentTimeMillis()
            )
            insertReminder(currentReminder)
            Log.d("SRO", "Reminder $i is $currentReminder")
            Log.d("SRO", "Pew! Saving Reminder to database.")
        }
    }

    fun processAndEnterData() {
        // TODO: Here is the actual purpose of this app.
        // Given the start date of a Subject entry, generate four upcoming dates
        // where someone is supposed to learn about that Subject again to strengthen
        // the learning memory.
        // --------------------------------------------------------------------------
        // The dates should be:
        // First repetition: 1 day after
        // Second repetition: 7 days after
        // Third repetition: 16 days after
        // Fourth repetition: 35 days after
        // ...
    }

    fun insertSubject(subject: Subject) = viewModelScope.launch {
        sroRepository.insertSubject(subject)
    }

    fun getSubjects(): LiveData<List<Subject>>? {
        return sroRepository.getSubjects()
    }

    fun insertReminder(reminder: Reminder) = viewModelScope.launch {
        sroRepository.insertReminder(reminder)
    }

    fun deleteAllData() = viewModelScope.launch {
        Log.d("SRO", "Deleting everything in the DB...")
        sroRepository.deleteAllData()
    }
}