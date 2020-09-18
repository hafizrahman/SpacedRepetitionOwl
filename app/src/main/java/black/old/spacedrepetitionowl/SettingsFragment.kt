package black.old.spacedrepetitionowl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private val DIALOG_FRAGMENT_TAG = "TimePickerDialog"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        if(preference is TimepickerPreference) {
            val timepickerdialog = TimePickerPreferenceDialog.newInstance(preference.key)
            timepickerdialog.setTargetFragment(this, 0)
            timepickerdialog.show(parentFragmentManager, DIALOG_FRAGMENT_TAG)
        }
        else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    /*
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }*/
}
