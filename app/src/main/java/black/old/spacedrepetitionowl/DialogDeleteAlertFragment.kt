package black.old.spacedrepetitionowl

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogDeleteAlertFragment(private val subjectToDeleteId: Long): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Subject?")
            .setMessage("Subject will be delete forever.")
            .setPositiveButton("Delete",
                DialogInterface.OnClickListener { dialog, id ->

                    // TODO Get the viewmodel and make it do the deletion
                    val mainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)
                    Log.d("LOVELIFE", "we about to delete subject id $subjectToDeleteId")
                    mainViewModel.deleteSubjectBySubjectId(subjectToDeleteId)


                    // This alert will be called by the Bottom Dialog Fragment using show()
                    // By default, after clicking Delete, it will hide the alert and show
                    // the Bottom Dialog Fragment. It does not make sense for the Bottom Dialog
                    // to show up again (because the Subject will have been deleted at that point),
                    // so we remove it from the backstack and go back to the previous destination
                    // (the subject list fragment)
                    findNavController().popBackStack()
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                }
            )
            .create()
    }
}