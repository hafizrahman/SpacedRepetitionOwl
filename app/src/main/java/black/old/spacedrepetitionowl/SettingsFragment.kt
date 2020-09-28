package black.old.spacedrepetitionowl

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    private val DIALOG_FRAGMENT_TAG = getString(R.string.key_tag_dialog_fragment)

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
}
