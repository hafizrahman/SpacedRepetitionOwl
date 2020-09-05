package black.old.spacedrepetitionowl

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat

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