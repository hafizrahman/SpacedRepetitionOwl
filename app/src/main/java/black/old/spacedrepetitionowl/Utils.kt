package black.old.spacedrepetitionowl

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

// Function for hiding keyboard, mostly used when a keyboard is opened then user pressed back/up.
// Usage: hideKeyboard(activity as MainActivity)
// Source: https://stackoverflow.com/a/57279734
fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Check if no view has focus
    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun dateStringFormatter(timestamp: Long, withYear: Boolean = false) : String {
    return if(withYear) {
        SimpleDateFormat("d MMM, YYYY").format(timestamp)
    } else {
        SimpleDateFormat("d MMM").format(timestamp)
    }
}

fun formatTimestamp(timestamp: Long, pattern: String) : String {
    return SimpleDateFormat(pattern).format(timestamp)
}

fun getReminderDates(startDate: Long): Array<Long> {
    val repDays = intArrayOf(1, 7, 16, 35)
    return Array(4) { i ->
        startDate + dayToMilliseconds(repDays[i])
    }
}

fun dayToMilliseconds(day: Int) : Long {
    return TimeUnit.DAYS.toMillis(day.toLong())
}

fun minutesFromMidnightToHourlyTime(minutes: Int): String {
    val hour: Int = minutes / 60
    val minute: Int = minutes % 60

    // Formatting here is more for adding "0" in front of single digit numbers,
    // so that e.g it writes out the hour format as "01:05" instead of "1:5"
    // See https://stackoverflow.com/a/27857435
    return String.format("%02d:%02d", hour, minute)
}

// The two functions below are needed to get the current timestamp, then get the correct local date
// (that matches the user's timezone), and finally strip off the hour/minute off of it, so that we
// can add the user's preferred notification hours/minutes to it.
// They're mostly used to allow users to set their own custom notification hour for each reminder.

// Function 1 : We're using LocalDate because we want to ignore the hours and add it later on
// based on default value or preferences.
// See: http://www.java2s.com/Tutorials/Java/Data_Type_How_to/Date_Convert/Convert_long_type_timestamp_to_LocalDate_and_LocalDateTime.htm
fun timestampToLocalDate(timestamp: Long): LocalDate {
    val localTimezone = ZoneId.systemDefault()
    return LocalDateTime
        .ofInstant(Instant.ofEpochMilli(timestamp), localTimezone)
        .toLocalDate()
}

// Function 2 : Get the epoch timestamp of a localDate at the start of the day.
// See: https://www.concretepage.com/java/java-8/convert-between-java-localdate-epoch#Epoch-Milliseconds
fun timestampFromLocalDateAtStartOfDay(localDate: LocalDate): Long {
    val localTimezone = ZoneId.systemDefault()
    val startofdayInstant = localDate.atStartOfDay(localTimezone).toInstant()
    return startofdayInstant.toEpochMilli()
}
