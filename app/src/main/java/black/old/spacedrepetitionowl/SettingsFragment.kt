package black.old.spacedrepetitionowl

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        if(preference is TimepickerPreference) {
            val timepickerdialog = TimePickerPreferenceDialog.newInstance(preference.key)
            timepickerdialog.setTargetFragment(this, 0)
            timepickerdialog.show(parentFragmentManager, getString(R.string.key_tag_dialog_fragment))
        }
        else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}
