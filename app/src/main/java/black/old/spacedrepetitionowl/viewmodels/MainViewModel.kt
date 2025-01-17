package black.old.spacedrepetitionowl.viewmodels

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import black.old.spacedrepetitionowl.*
import black.old.spacedrepetitionowl.R
import black.old.spacedrepetitionowl.database.SroDatabase
import black.old.spacedrepetitionowl.models.*
import black.old.spacedrepetitionowl.repositories.SroRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var sroRepository: SroRepository
    private var context: Context
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
            // When customTimestamp is 0, it means we want to use the value of "now" for
            // starting date/time. However, the app also has a preference where a user can pick a
            // time(hh:mm) for the notifications to show up. This time will likely not match the
            // "now" time.
            // So, what we want to do here is to grab the correct day/month/year only, then set
            // the timestamp millis for it to be at start of the day, and finally add the hour
            // preference count to that millis, so the end result is a timestamp at the correct
            // day/month/year, *plus* the correct hour/minute.
            //
            // To recap:
            // 1) get current now millis,
            // 2) run timestampToLocalDate() on it, get correct timezone LocalDate that ignores time
            // 3) convert that result back to millis at start of the day with
            //    timestampFromLocalDateAtStartOfDay()
            // 3) check notification preference and add the correct millis from it
            // 4) save this value as nowTimestamp
            val localDateNow = timestampToLocalDate(System.currentTimeMillis())
            val startDateNowMillis = timestampFromLocalDateAtStartOfDay(localDateNow)
            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplication())
            val notifTimePreferenceInMinutes = sharedPreferences.getInt(
                "pref_notification_time",
                TimepickerPreference.DEFAULT_MINUTES_FROM_MIDNIGHT)

            val notifTimePreferenceInMillis = notifTimePreferenceInMinutes * 60000
            nowTimestamp = startDateNowMillis + notifTimePreferenceInMillis
        }
        else {
            // At the current version of the app, this case never appears because we don't allow
            // picking custom date on new subject creation, only during edits.
            nowTimestamp = customTimestamp
        }

        // Generate one Subject
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
            val generatedSubjectId = sroRepository.insertSubject(subjectToEnter)

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

                createNotificationAlarm(
                    context,
                    subjectToEnter,
                    currentReminder.dateTimestamp,
                    currentReminderGeneratedId
                )
            }
        }
    }

    // Create example subjects
    fun createExampleSubjects() {
        insertSubject(
            context.getString(R.string.example_subject_1_title),
            context.getString(R.string.example_subject_1_url),
            context.getString(R.string.example_subject_1_notes)
        )

        insertSubject(
            context.getString(R.string.example_subject_2_title),
            context.getString(R.string.example_subject_2_url),
            context.getString(R.string.example_subject_2_notes)
        )

        insertSubject(
            context.getString(R.string.example_subject_3_title),
            context.getString(R.string.example_subject_3_url),
            context.getString(R.string.example_subject_3_notes)
        )
    }
    private fun createNotificationAlarm(context: Context,
                                        subject: Subject,
                                        reminder_timestamp: Long,
                                        reminder_id: Long
    ) {
        val notifPendingIntent = AlarmScheduler.createReminderPendingIntent(
            context,
            subject,
            reminder_timestamp,
            reminder_id
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        AlarmScheduler.scheduleAlarm(
            context,
            reminder_timestamp,
            notifPendingIntent,
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

    fun updateSubjectContent(subject_id: Long, content: String) {
        viewModelScope.launch {
            sroRepository.updateSubjectContent(subject_id, content)
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
        sroRepository.deleteAllData()
    }

}