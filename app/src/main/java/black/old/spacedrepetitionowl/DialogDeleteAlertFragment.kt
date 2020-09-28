package black.old.spacedrepetitionowl

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogDeleteAlertFragment(private val subjectToDeleteId: Long): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_alert_title))
            .setMessage(getString(R.string.delete_alert_message))
            .setPositiveButton(getString(R.string.button_label_delete)) { dialog, id ->

                // Get the viewmodel and make it do the deletion
                val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
                mainViewModel.deleteSubjectBySubjectId(subjectToDeleteId)

                // Show a toaster to confirm that subject has been deleted
                val toast = Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_delete_success),
                    Toast.LENGTH_LONG)
                toast.show()

                // This alert will be called by the Bottom Dialog Fragment using show()
                // By default, after clicking Delete, it will hide the alert and show
                // the Bottom Dialog Fragment. It does not make sense for the Bottom Dialog
                // to show up again (because the Subject will have been deleted at that point),
                // so we remove it from the backstack and go back to the previous destination
                // (the subject list fragment)
                findNavController().popBackStack()
            }

            .setNegativeButton(getText(R.string.button_label_cancel)) { dialog, id ->
                dialog.dismiss()
            }
            .create()
    }
}