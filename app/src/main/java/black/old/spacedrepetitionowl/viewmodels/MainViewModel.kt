package black.old.spacedrepetitionowl.viewmodels

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import black.old.spacedrepetitionowl.AlarmScheduler
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
    private lateinit var context: Context

    init {
        val subjectDao = SroDatabase.getDatabase(application).subjectDao
        val reminderDao = SroDatabase.getDatabase(application).reminderDao
        sroRepository = SroRepository(subjectDao, reminderDao)
        context = application.applicationContext
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
            // Given the start date of a Subject entry, generate four upcoming dates
            // when someone is supposed to learn about that Subject again to strengthen
            // the learning memory.
            // --------------------------------------------------------------------------
            // The dates should be:
            // First repetition: 1 day after
            // Second repetition: 7 days after
            // Third repetition: 16 days after
            // Fourth repetition: 35 days after
            var currentReminder : Reminder
            val repDays = intArrayOf(1, 7, 16, 35)
            for(i in 0..3) {
                val dateTimestamp = nowTimestamp + dayToMilliseconds(repDays[i])
                currentReminder = Reminder(
                generatedSubjectId,
                dateTimestamp
                )
                val currentReminderGeneratedId = sroRepository.insertReminder(currentReminder)

                Log.d("HIOWL",
                    "Alarm: $subjectText for $repDays[$i] ($currentReminderGeneratedId)")

                addReminderNotification(context,
                    subjectText,
                    currentReminderGeneratedId,
                    dateTimestamp)

            }
        }
    }

    fun addReminderNotification(context: Context, subjectText: String, reminderId: Long, reminderTimestamp: Long) {
        // Create the unique pending intent for the particular reminder
        val pendingInt = AlarmScheduler.createPendingIntentForReminder(
            context,
            subjectText,
            reminderId)

        // Submit pending intent to Alarm Manager to schedule it
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        AlarmScheduler.scheduleAlarm(
            context,
            reminderTimestamp,
            pendingInt,
            alarmManager
        )
    }

    fun dayToMilliseconds(day: Int) : Long {
        return TimeUnit.DAYS.toMillis(day.toLong())
    }

    fun deleteSubjectBySubjectId(subject_id: Long) {
        viewModelScope.launch {
            sroRepository.deleteSubjectBySubjectId(subject_id)
        }
    }
    fun updateSubject(subject: Subject) {
        viewModelScope.launch {
            sroRepository.updateSubject(subject)
        }
    }
    fun getSubject(subject_id: Long): LiveData<Subject> {
        return sroRepository.getSubject(subject_id)
    }

    fun getSubjects(): LiveData<List<Subject>>? {
        return sroRepository.getSubjects()
    }

    fun getReminders(): LiveData<List<Reminder>>? {
        return sroRepository.getReminders()
    }

    fun getRemindersBySubjectId(subject_id: Long):LiveData<List<Reminder>>? {
        return sroRepository.getReminderBySubject(subject_id)
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