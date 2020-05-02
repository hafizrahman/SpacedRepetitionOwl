package black.old.spacedrepetitionowl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

/**
 * A simple [Fragment] subclass.
 * Use the [AddSubjectDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddSubjectDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Dismiss dialog if user touched outside of the dialog
        // ref: https://stackoverflow.com/a/8761729
        dialog?.setCanceledOnTouchOutside(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_subject_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()

        // Make dialog be full width, and have its height match the content.
        // This is necessary because for some reason, setting it up in the XML does not work.
        // Without this, the dialogFragment shows up really small.
        // ref: https://stackoverflow.com/a/8991860
        val currentDialog = dialog
        if(currentDialog != null) {
            currentDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

    }

    override fun getTheme(): Int {
        return R.style.fullscreen_dialog
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddSubjectDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddSubjectDialogFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
