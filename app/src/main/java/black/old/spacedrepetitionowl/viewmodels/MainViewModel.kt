package black.old.spacedrepetitionowl.viewmodels

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import black.old.spacedrepetitionowl.AlarmScheduler
import black.old.spacedrepetitionowl.database.SroDatabase
import black.old.spacedrepetitionowl.models.*
import black.old.spacedrepetitionowl.repositories.SroRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var sroRepository: SroRepository
    private lateinit var context: Context
    val selectedSubject = MutableLiveData<SubjectPackage>()

    init {
        val subjectDao = SroDatabase.getDatabase(application).subjectDao
        val reminderDao = SroDatabase.getDatabase(application).reminderDao
        sroRepository = SroRepository(subjectDao, reminderDao)
        context = application.applicationContext
    }

    fun saveSelected(subjectPackage: SubjectPackage) {
        selectedSubject.value = subjectPackage
    }

    fun updateSelectedSubjectText(text: String) {
        selectedSubject.value?.subject?.content = text
    }

    // Entering
    fun insertSubject(subjectText: String, uriText: String, subjectNotes: String, customTimestamp: Long = 0) {
        val dateNow = Date()
        val simpleDateFormat = SimpleDateFormat("dHHmmsss")

        var nowTimestamp: Long
        if(customTimestamp == 0L) {
            nowTimestamp = System.currentTimeMillis()
        }
        else {
            nowTimestamp = customTimestamp
        }

        // Generate one dummy Subject
        val subjectToEnter = Subject(
            subjectText,
            uriText,
            subjectNotes,
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

    fun updateSubjectNotes(subject_id: Long, notes: String) {
        viewModelScope.launch {
            sroRepository.updateSubjectNotes(subject_id, notes)
        }
    }

    fun updateSubjectStartDate(subject_id: Long, timestamp: Long) {
        viewModelScope.launch {
            sroRepository.updateSubjectStartDate(subject_id, timestamp)
        }
    }

    fun updateSubjectUrl(subject_id: Long, url: String) {
        viewModelScope.launch {
            sroRepository.updateSubjectUrl(subject_id, url)
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

    fun updateReminder(reminder: Reminder) {
        viewModelScope.launch {
            sroRepository.updateReminder(reminder)
        }
    }

    fun getRemindersBySubjectId(subject_id: Long):LiveData<List<Reminder>>? {
        return sroRepository.getRemindersBySubject(subject_id)
    }

    fun resetRemindersCheckedStateForSubject(subject_id: Long) {
        viewModelScope.launch {
            sroRepository.resetRemindersCheckedStateForSubject(subject_id)
        }
    }

    fun updateDateTimestampAndReset(reminder_id: Long, new_timestamp: Long) {
        viewModelScope.launch {
            sroRepository.updateDateTimestampAndReset(reminder_id, new_timestamp)
        }

    }

    fun getAllData(): CombinedSubjectReminders? {
        var ldSubjects = getSubjects()
        var ldReminders = getReminders()

        if (ldSubjects != null && ldReminders != null) {
            return CombinedSubjectReminders(ldSubjects, ldReminders)
        }
        return null
    }

    // This is part of the method used to create a MutableLiveData that contains multiple type
    // of data sources (in this case, (1) the Room query for single Subject and (2) the Room query
    // for the Reminders tied to that single Subject.
    // This method is outlined further on:
    // https://code.luasoftware.com/tutorials/android/use-mediatorlivedata-to-query-and-merge-different-data-type/
    fun getSingleSubjectReminders(subject_id: Long) : MediatorLiveData<SingleSubjectReminders> {
        val singleSubjectReminders = MediatorLiveData<SingleSubjectReminders>()
        singleSubjectReminders.addSource(getSubject(subject_id)) {
            if( it != null ) {
                singleSubjectReminders.value = singleSubject(it)
            }
        }
        getRemindersBySubjectId(subject_id)?.let { ldRemindersList ->
            singleSubjectReminders.addSource(ldRemindersList) { reminderList ->
                if( reminderList != null ) {
                    singleSubjectReminders.value = singleReminders(reminderList)
                }
            }
        }
        return singleSubjectReminders
    }

    fun deleteAllData() = viewModelScope.launch {
        Log.d("SRO", "Deleting everything in the DB...")
        sroRepository.deleteAllData()
        Log.d("SRO", "All deleted.")
    }

}