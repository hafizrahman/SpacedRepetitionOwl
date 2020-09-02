package black.old.spacedrepetitionowl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_subject_url_bottom_dialog.view.*

class SubjectURLBottomDialogFragment : BottomSheetDialogFragment() {
    val args: SubjectURLBottomDialogFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_subject_url_bottom_dialog,
            container,
            false)

        view.subject_url_bottom_dialog_url.setText(args.subjectUrl)

        return view
    }
}