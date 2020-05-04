package black.old.spacedrepetitionowl.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import black.old.spacedrepetitionowl.database.SroDatabase
import black.old.spacedrepetitionowl.models.CombinedSubjectReminders
import black.old.spacedrepetitionowl.models.Reminder
import black.old.spacedrepetitionowl.models.Subject
import black.old.spacedrepetitionowl.repositories.SroRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var sroRepository: SroRepository

    init {
        val subjectDao = SroDatabase.getDatabase(application).subjectDao
        val reminderDao = SroDatabase.getDatabase(application).reminderDao
        sroRepository = SroRepository(subjectDao, reminderDao)
    }

    // Entering
    fun insertSubject(subjectText: String, uriText: String) {
        val dateNow = Date()
        val simpleDateFormat = SimpleDateFormat("dHHmmsss")

        val nowTimestamp = System.currentTimeMillis()

        // Generate one dummy Subject
        val subjectToEnter = Subject(
            subjectText,
            uriText,
            nowTimestamp
        )

        // We are now entering the data.
        // We do this inside a viewModelScope since sroRepository.insertSubject() and
        // sroRepository.insertReminder() are both suspend functions.
        viewModelScope.launch {
            // Insert subject to database, and get subject id from the database that was
            // automatically generated.
            Log.d("SRIROTI", "entering this subject to db: $subjectToEnter")
            val generatedSubjectId = sroRepository.insertSubject(subjectToEnter)
            Log.d("SRIROTI", "generated subject id from db: $generatedSubjectId")

            // Generate four Reminders based on the Subject, make sure to enter the
            // generatedSubjectId for foreign key purposes.
            var currentReminder : Reminder
            val repDays = intArrayOf(1, 7, 16, 35)
            for(i in 0..3) {
                currentReminder = Reminder(
                    generatedSubjectId,
                    nowTimestamp + dayToMilliseconds(repDays[i])
                )
                sroRepository.insertReminder(currentReminder)
                Log.d("SRIROTI", "Reminder $i is $currentReminder")
                Log.d("SRIROTI", "Pew! Saving Reminder to database.")
            }
        }
    }

    // This is a dummy function called when the Add subject button is clicked.
    // Ignore everything and enter some dummy values into the database.
    fun dummyInsertSubject() {
        Log.d("SRO", "Entering but not for real")

        val dateNow = Date()
        val simpleDateFormat = SimpleDateFormat("dHHmmsss")

        // We use the current time's "ddHHmmss" as the dummy Subject's ID
        // to make sure they're unique.
        val dummySubjectId = 0.toLong()
        val nowTimestamp = System.currentTimeMillis()

        // Generate one dummy Subject
        val dummySubject = Subject(
            "Subject #$dummySubjectId",
            "https://wordpress.com/",
            nowTimestamp
        )

        Log.d("SRO", "Content of Subject is $dummySubject")
        Log.d("SRO", "Hoot! Saving Subject to database.")

        // We are now entering the data.
        // We do this inside a viewModelScope since sroRepository.insertSubject() and
        // sroRepository.insertReminder() are both suspend functions.
        viewModelScope.launch {
            // Insert subject to database, and get subject id from the database that was
            // automatically generated.
            Log.d("SRIRIRI", "entering this subject to db: $dummySubject")
            val generatedSubjectId = sroRepository.insertSubject(dummySubject)
            Log.d("SRIRIRI", "generated subject id from db: $generatedSubjectId")

            // Generate four Reminders based on the Subject, make sure to enter the
            // generatedSubjectId for foreign key purposes.
            var currentReminder : Reminder
            val repDays = intArrayOf(1, 7, 16, 35)
            for(i in 0..3) {
                currentReminder = Reminder(
                    generatedSubjectId,
                    nowTimestamp + dayToMilliseconds(repDays[i])
                )
                sroRepository.insertReminder(currentReminder)
                Log.d("SRIRIRI", "Reminder $i is $currentReminder")
                Log.d("SRIRIRI", "Pew! Saving Reminder to database.")
            }
        }
    }

    fun processAndEnterData() {
        // TODO: Here is the actual purpose of this app.
        // Given the start date of a Subject entry, generate four upcoming dates
        // when someone is supposed to learn about that Subject again to strengthen
        // the learning memory.
        // --------------------------------------------------------------------------
        // The dates should be:
        // First repetition: 1 day after
        // Second repetition: 7 days after
        // Third repetition: 16 days after
        // Fourth repetition: 35 days after
        // ...
        val repDays = intArrayOf(1, 7, 16, 35)
        val nowTimestamp = System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("dHHmmsss")

        var currentReminder : Reminder
        for(i in 0..3) {
            // TODO: Generate Reminders and insert here
            // Wait for 1 second to make sure the reminder IDs are unique
            TimeUnit.SECONDS.sleep(1L)
            currentReminder = Reminder(
                1,
                nowTimestamp + dayToMilliseconds(repDays[i]) // Add x day(s) from now
            )
        }
    }

    fun dayToMilliseconds(day: Int) : Long {
        return TimeUnit.DAYS.toMillis(day.toLong())
    }

    fun getSubjects(): LiveData<List<Subject>>? {
        return sroRepository.getSubjects()
    }

    fun getReminders(): LiveData<List<Reminder>>? {
        return sroRepository.getReminders()
    }

    fun getAllData(): CombinedSubjectReminders? {
        var ldSubjects = getSubjects()
        var ldReminders = getReminders()

        if (ldSubjects != null && ldReminders != null) {
            return CombinedSubjectReminders(ldSubjects, ldReminders)
        }
        return null
    }

    fun deleteAllData() = viewModelScope.launch {
        Log.d("SRO", "Deleting everything in the DB...")
        sroRepository.deleteAllData()
        Log.d("SRO", "All deleted.")
    }

}